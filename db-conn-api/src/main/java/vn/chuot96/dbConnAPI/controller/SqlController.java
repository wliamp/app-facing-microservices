package vn.chuot96.dbConnAPI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.chuot96.dbConnAPI.dto.SqlRequestDTO;
import vn.chuot96.dbConnAPI.service.SqlService;

@RestController
@RequestMapping("/api")
public class SqlController {

    @PostMapping("/sql/mysql")
    public ResponseEntity<?> mysql(@RequestBody SqlRequestDTO request) {
        return SqlService.mysqlQuery(request);
    }

    @PostMapping("/sql/postgres")
    public ResponseEntity<?> postgres(@RequestBody SqlRequestDTO request) {
        return SqlService.postgresQuery(request);
    }

    @PostMapping("/sql/mssql")
    public ResponseEntity<?> mssql(@RequestBody SqlRequestDTO request) {
        return SqlService.mssqlQuery(request);
    }

}
