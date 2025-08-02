package vn.chuot96.clientgwapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @RequestMapping("/fallback/db")
    public ResponseEntity<String> fallbackDb() {
        return ResponseEntity.status(503).body("DB service is unavailable");
    }

    @RequestMapping("/fallback/jwt")
    public ResponseEntity<String> fallbackJwt() {
        return ResponseEntity.status(503).body("JWT service is unavailable");
    }

    @RequestMapping("/fallback/verify")
    public ResponseEntity<String> fallbackVerify() {
        return ResponseEntity.status(503).body("Verify service is unavailable");
    }
}
