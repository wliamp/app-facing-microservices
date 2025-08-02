package vn.chuot96.jwtissapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.chuot96.jwtissapi.dto.RequestDTO;
import vn.chuot96.jwtissapi.dto.ResponseDTO;
import vn.chuot96.jwtissapi.service.TokenService;

@RestController
@RequestMapping("/issuer")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService service;

    @PostMapping("/tokens")
    public ResponseEntity<?> issue(@RequestBody RequestDTO request) {
        return ResponseEntity.ok(new ResponseDTO(service.generateAccess(request), service.generateRefresh(request)));
    }

    @PostMapping("/access")
    public ResponseEntity<?> issueAccess(@RequestBody RequestDTO request) {
        return ResponseEntity.ok(service.generateAccess(request));
    }

    // --> more Token type here

}