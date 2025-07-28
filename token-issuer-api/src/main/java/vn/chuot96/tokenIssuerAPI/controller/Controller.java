package vn.chuot96.tokenIssuerAPI.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.chuot96.tokenIssuerAPI.dto.TokenRequestDTO;
import vn.chuot96.tokenIssuerAPI.service.JwtService;

@RestController
@RequestMapping("/api/token")
public class Controller {

    private final JwtService service;

    public Controller(JwtService service) {
        this.service = service;
    }

    @PostMapping("/access")
    public ResponseEntity<String> access(@RequestBody TokenRequestDTO request) {
        return ResponseEntity.ok(service.generateAccess(request));
    }

}
