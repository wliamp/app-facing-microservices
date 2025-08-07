package vn.chuot96.verify3rd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rd.dto.RequestDTO;
import vn.chuot96.verify3rd.service.OauthService;
import vn.chuot96.verify3rd.service.OtpService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/verify")
public class Controller {
    private final OauthService oauthService;

    private final OtpService otpService;

    @GetMapping("/{provider}")
    public Mono<ResponseEntity<?>> oauth(@PathVariable String provider, @RequestBody RequestDTO request) {
        return oauthService.forward(provider, request);
    }

    @PostMapping("/otp")
    public Mono<ResponseEntity<?>> otp(@RequestBody RequestDTO request) {
        return otpService.forward(request);
    }
}
