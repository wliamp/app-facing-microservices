package vn.chuot96.jwtissapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.chuot96.jwtissapi.dto.RequestDTO;
import vn.chuot96.jwtissapi.dto.ResponseDTO;
import vn.chuot96.jwtissapi.service.TokenService;

@RestController
@RequestMapping("/issue")
@RequiredArgsConstructor
public class Controller {
    private final TokenService tokenservice;

    @PostMapping("/access-refresh")
    public ResponseEntity<?> issue(@RequestBody RequestDTO request) {
        return ResponseEntity.ok(
                new ResponseDTO(tokenservice.issueAccess(request), tokenservice.issueRefresh(request)));
    }

    @PostMapping("/access")
    public ResponseEntity<?> issueAccess(@RequestBody RequestDTO request) {
        return ResponseEntity.ok(tokenservice.issueAccess(request));
    }
}
