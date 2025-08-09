package vn.chuot96.jwtiss.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.chuot96.jwtiss.dto.Request;
import vn.chuot96.jwtiss.service.IssueService;

@RestController
@RequestMapping("/issue")
@RequiredArgsConstructor
public class Controller {
    private final IssueService issueService;

    @PostMapping("/non-refresh")
    public ResponseEntity<?> issue(@RequestBody Request request) {
        return ResponseEntity.ok(issueService.firstLogin(request));
    }

    @PostMapping("/with-refresh")
    public ResponseEntity<?> issueAccess(@RequestBody String refresh) {
        return ResponseEntity.ok(issueService.rememberLogin(refresh));
    }
}
