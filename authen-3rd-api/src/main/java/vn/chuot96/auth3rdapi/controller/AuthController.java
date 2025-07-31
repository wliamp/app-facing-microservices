package vn.chuot96.auth3rdapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.chuot96.auth3rdapi.dto.RequestDTO;
import vn.chuot96.auth3rdapi.service.OauthService;
import vn.chuot96.auth3rdapi.service.OtpService;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
public class AuthController {

    private final OauthService oauthService;

    private final OtpService otpService;

    @PostMapping("/{provider}")
    public ResponseEntity<?> verifyOauth(@PathVariable String provider, @RequestBody RequestDTO request) {
        return ResponseEntity.ok(oauthService.verifyToken(provider, request.token()));
    }

    @PostMapping("/otp")
    public ResponseEntity<?> verifyOtp(@RequestBody RequestDTO request) {
        return ResponseEntity.ok(otpService.verifyToken(request.token()));
    }

}
