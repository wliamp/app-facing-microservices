package vn.chuot96.authservice.compo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ForwardHelper {
    @Value("${app.code}")
    private final String appCode;

    private final ExternalFileReader externalFileReader;

    private final WebClient.Builder webBuilder;

    public <T, R> Mono<R> post(
            String appName, String endpoint, String headerValue, T requestBody, Class<R> responseType) {
        return webBuilder
                .baseUrl("http://" + appName + "-" + appCode)
                .build()
                .post()
                .uri(endpoint)
                .header(externalFileReader.string("InternalHeaderName"), headerValue)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse
                        .bodyToMono(String.class)
                        .map(errorBody -> {
                            log.error("HTTP error from http://{}{}: {}", appName, endpoint, errorBody);
                            return new RuntimeException("Downstream error: " + errorBody);
                        }))
                .bodyToMono(responseType)
                .doOnError(error ->
                        log.error("Error during POST to http://{}{}: {}", appName, endpoint, error.getMessage()));
    }
}
