package vn.chuot96.verify3rdapi.component;

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

    private final WebClient.Builder webClient;

    public <T, R> Mono<R> post(String sub, String uri, String headerValue, T requestBody, Class<R> responseType) {
        return webClient
                .baseUrl("http://" + sub)
                .build()
                .post()
                .uri(uri)
                .header("X-Service-Token",headerValue)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(errorBody -> {
                                    log.error("HTTP error from http://{}{}: {}", sub, uri, errorBody);
                                    return new RuntimeException("Downstream error: " + errorBody);
                                })
                )
                .bodyToMono(responseType)
                .doOnError(error -> log.error("Error during POST to http://{}{}: {}", sub, uri, error.getMessage()));
    }

}
