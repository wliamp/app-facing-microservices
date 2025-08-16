package vn.chuot96.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.UserToken;
import vn.chuot96.authservice.service.authenticate.LoginService;
import vn.chuot96.authservice.service.authenticate.LogoutService;
import vn.chuot96.authservice.service.authenticate.RelogService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class Controller {
    private final LoginService loginService;

    private final LogoutService logoutService;

    private final RelogService relogService;

    @PostMapping("/login/{party}")
    public Mono<ResponseEntity<UserToken>> login(@PathVariable String party, @RequestBody String external) {
        return loginService.loginWithoutBearer(party, external);
    }

    @PostMapping("/relog")
    public Mono<ResponseEntity<UserToken>> relog(@RequestHeader("Authorization") String internal) {
        return relogService.loginWithBearer(internal);
    }

    @PostMapping("/link/{party}")
    public Mono<ResponseEntity<UserToken>> link(
            @RequestHeader("Authorization") String internal, @PathVariable String party, @RequestBody String external) {
        return loginService.linkAccount(internal, party, external);
    }

    @PostMapping("/logout")
    public Mono<Void> logout(@RequestHeader("Authorization") String internal) {
        return logoutService.logout(internal);
    }
}
