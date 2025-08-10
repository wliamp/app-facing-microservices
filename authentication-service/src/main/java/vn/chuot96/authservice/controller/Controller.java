package vn.chuot96.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.AuthRequest;
import vn.chuot96.authservice.dto.LinkRequest;
import vn.chuot96.authservice.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class Controller {
    private final AuthService authService;

    @PostMapping("/guest")
    public Mono<ResponseEntity<?>> guest() {
        return authService.handleGuest();
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest request) {
        return authService.handleLogin(request);
    }

    @PostMapping("/link")
    public Mono<ResponseEntity<?>> link(
            @RequestBody LinkRequest request, @RequestHeader("Authorization") String authorizationHeader) {
        return authService.handleLink(request, authorizationHeader);
    }

    @PostMapping("/logout")
    public Mono<Void> remove(
            @RequestBody AuthRequest request, @RequestHeader("Authorization") String authorizationHeader) {
        return authService.handleLogout(request, authorizationHeader);
    }
}
