package vn.chuot96.authservice.compo;

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
    private final WebClient.Builder webBuilder;

    public <T, R> Mono<R> post(String appName, String endpoint, T requestBody, Class<R> responseType) {
        return webBuilder
                .baseUrl("http://" + appName)
                .build()
                .post()
                .uri(endpoint)
                .header("William-K+Devannis@Phan-Hoang-Nam+1996@Dev.vn", "turn+based-i+rpg-2d.authentication@service")
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
