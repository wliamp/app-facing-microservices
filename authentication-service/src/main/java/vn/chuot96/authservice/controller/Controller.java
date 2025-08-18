package vn.chuot96.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.UserToken;
import vn.chuot96.authservice.service.authenticate.AuthenticateService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class Controller {
    private final AuthenticateService authenticateService;

    @PostMapping("/guest")
    public Mono<ResponseEntity<UserToken>> guest() {
        return authenticateService.guestLogin();
    }

    @PostMapping("/{party}/login")
    public Mono<ResponseEntity<UserToken>> login(@PathVariable String party, @RequestBody String external) {
        return authenticateService.loginWithoutBearer(party, external);
    }

    @PostMapping("/relog")
    public Mono<ResponseEntity<UserToken>> relog(@RequestHeader("Authorization") String internal) {
        return authenticateService.loginWithBearer(internal);
    }

    @PostMapping("/{party}/link")
    public Mono<ResponseEntity<UserToken>> link(
            @RequestHeader("Authorization") String internal, @PathVariable String party, @RequestBody String external) {
        return authenticateService.linkAccount(internal, party, external);
    }

    @PostMapping("/logout")
    public Mono<Void> logout(@RequestHeader("Authorization") String internal) {
        return authenticateService.logout(internal);
    }
}
