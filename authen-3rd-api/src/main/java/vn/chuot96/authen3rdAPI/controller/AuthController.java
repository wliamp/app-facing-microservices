package vn.chuot96.authen3rdAPI.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.chuot96.authen3rdAPI.service.AuthService;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @GetMapping("/{provider}")
    public ResponseEntity<?> verify(@PathVariable String providerName, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.verifyToken(providerName, jwt));
    }
}
