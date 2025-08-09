package vn.chuot96.verify3rd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rd.service.OauthService;
import vn.chuot96.verify3rd.service.OtpService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/verify")
public class Controller {
    private final OauthService oauthService;

    private final OtpService otpService;

    @GetMapping("/{party}")
    public Mono<ResponseEntity<?>> oauth(@PathVariable String party, @RequestBody String token) {
        return oauthService.forward(party, token);
    }

    @PostMapping("/otp")
    public Mono<ResponseEntity<?>> otp(@RequestBody String token) {
        return otpService.forward(token);
    }
}
