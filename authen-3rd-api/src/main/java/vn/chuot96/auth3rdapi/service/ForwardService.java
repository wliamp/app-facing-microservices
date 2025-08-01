package vn.chuot96.auth3rdapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import vn.chuot96.auth3rdapi.dto.UserDTO;

@Service
@RequiredArgsConstructor
public class ForwardService {

    private final WebClient.Builder webClient;

    public void authenticateUser(UserDTO userDTO) {
        webClient
                .baseUrl("http://authentication-service")
                .build()
                .post()
                .uri("/db")
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .subscribe(user -> {});
    }

    public void generateToken(UserDTO userDTO) {
        webClient
                .baseUrl("http://token-issuer-api")
                .build()
                .post()
                .uri("/jwt")
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .subscribe(user -> {});
    }

}
