package vn.chuot96.verify3rdapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rdapi.dto.RequestDTO;
import vn.chuot96.verify3rdapi.service.OauthService;
import vn.chuot96.verify3rdapi.service.OtpService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/verify")
public class Controller {
    private final OauthService oauthService;

    private final OtpService otpService;

    @PostMapping("/{provider}")
    public Mono<ResponseEntity<?>> oauth(@PathVariable String provider, @RequestBody RequestDTO request) {
        return oauthService.forward(provider, request);
    }

    @PostMapping("/otp")
    public Mono<ResponseEntity<?>> otp(@RequestBody RequestDTO request) {
        return otpService.forward(request);
    }
}
