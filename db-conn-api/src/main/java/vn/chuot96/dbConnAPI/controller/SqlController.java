package vn.chuot96.dbConnAPI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.chuot96.dbConnAPI.dto.SqlRequestDTO;
import vn.chuot96.dbConnAPI.service.SqlService;
import vn.chuot96.dbConnAPI.util.SqlConverter;

import java.sql.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sql")
public class SqlController {
    private final SqlService service;

    public SqlController(SqlService service) {
        this.service = service;
    }

    @PostMapping("/query")
    public ResponseEntity<?> query(@RequestBody SqlRequestDTO request) {
        return service.executeQuery(request);
    }
}
