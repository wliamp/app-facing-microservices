package vn.chuot96.verify3rdapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rdapi.service.ForwardService;
import vn.chuot96.verify3rdapi.service.OauthService;
import vn.chuot96.verify3rdapi.service.OtpService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/verify")
public class AuthController {

    private final ForwardService forward;

    private final OauthService oauth;

    private final OtpService otp;

    @PostMapping("/{provider}")
    public Mono<ResponseEntity<?>> responseOauth(@PathVariable String provider, @RequestBody String token) {
        return forward.forwardAuthService(oauth.verifyToken(provider, token))
                .then(forward.forwardJwtJssApiTokens())
                .map(ResponseEntity::ok);
    }

    @PostMapping("/otp")
    public Mono<ResponseEntity<?>> responseOtp(@RequestBody String token) {
        return forward.forwardAuthService(otp.verifyToken(token))
                .then(forward.forwardJwtJssApiTokens())
                .map(ResponseEntity::ok);
    }

}
