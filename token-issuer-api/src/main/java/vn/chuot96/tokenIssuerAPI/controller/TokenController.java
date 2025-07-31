package vn.chuot96.tokenIssuerAPI.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.chuot96.tokenIssuerAPI.dto.TokenRequestDTO;
import vn.chuot96.tokenIssuerAPI.service.TokenService;

@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService service;

    @PostMapping("/access")
    public ResponseEntity<String> access(@RequestBody TokenRequestDTO request) {
        return ResponseEntity.ok(service.generateAccess(request));
    }

}
