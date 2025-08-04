package vn.chuot96.verify3rdapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rdapi.service.OauthService;
import vn.chuot96.verify3rdapi.service.OtpService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/verify")
public class AuthController {
    private final OauthService oauth;

    private final OtpService otp;

    @PostMapping("/{provider}")
    public Mono<ResponseEntity<?>> oauth(@PathVariable String provider, @RequestBody String token) {
        return oauth.forward(provider, token);
    }

    @PostMapping("/otp")
    public Mono<ResponseEntity<?>> otp(@RequestBody String token) {
        return otp.forward(token);
    }
}
