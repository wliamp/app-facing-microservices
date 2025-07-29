package vn.chuot96.authen3rdAPI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.chuot96.authen3rdAPI.service.GoogleAuthenService;

@RestController
@RequestMapping("/verify")
public class Controller {

    @GetMapping("/google")
    public ResponseEntity<?> verify(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(GoogleAuthenService.verifyToken(jwt));
    }
}
