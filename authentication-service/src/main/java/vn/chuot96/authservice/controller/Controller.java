package vn.chuot96.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.UserToken;
import vn.chuot96.authservice.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class Controller {
    private final AuthService authService;

    @PostMapping("/login/{party}")
    public Mono<ResponseEntity<UserToken>> login(@PathVariable String party, @RequestBody String external) {
        return authService.loginWithoutBearer(party, external);
    }

    @PostMapping("/re-login")
    public Mono<ResponseEntity<UserToken>> reLogin(@RequestHeader("Authorization") String internal) {
        return authService.loginWithBearer(internal);
    }

    @PostMapping("/link/{party}")
    public Mono<ResponseEntity<UserToken>> link(
            @RequestHeader("Authorization") String internal, @PathVariable String party, @RequestBody String external) {
        return authService.link(internal, party, external);
    }

    @PostMapping("/logout")
    public Mono<Void> logout(@RequestHeader("Authorization") String internal) {
        return authService.logout(internal);
    }
}
