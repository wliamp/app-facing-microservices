package vn.chuot96.verify3rd.compo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ForwardHelper {
    private final ExternalFileReader externalFileReader;

    private final WebClient.Builder webBuilder;

    public <T, R> Mono<R> post(
            String addName, String endpoint, String headerValue, T requestBody, Class<R> responseType) {
        return webBuilder
                .baseUrl("http://" + addName)
                .build()
                .post()
                .uri(endpoint)
                .header(externalFileReader.string("Verify3rdHeaderName"), headerValue)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse
                        .bodyToMono(String.class)
                        .map(errorBody -> {
                            log.error("HTTP error from http://{}{}: {}", addName, endpoint, errorBody);
                            return new RuntimeException("Downstream error: " + errorBody);
                        }))
                .bodyToMono(responseType)
                .doOnError(error ->
                        log.error("Error during POST to http://{}{}: {}", addName, endpoint, error.getMessage()));
    }
}
