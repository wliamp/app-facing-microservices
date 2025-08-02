package vn.chuot96.dbconnapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.chuot96.dbconnapi.dto.NosqlRequestDTO;
import vn.chuot96.dbconnapi.dto.SqlRequestDTO;
import vn.chuot96.dbconnapi.service.MongodbService;
import vn.chuot96.dbconnapi.service.SqlService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/db")
public class DbController {

    private final SqlService sqlService;

    private final MongodbService mongodbService;

    @PostMapping("/sql/mysql")
    public ResponseEntity<?> mysql(@RequestBody SqlRequestDTO request) {
        return sqlService.mysqlQuery(request);
    }

    @PostMapping("/sql/postgres")
    public ResponseEntity<?> postgres(@RequestBody SqlRequestDTO request) {
        return sqlService.postgresQuery(request);
    }

    @PostMapping("/sql/mssql")
    public ResponseEntity<?> mssql(@RequestBody SqlRequestDTO request) {
        return sqlService.mssqlQuery(request);
    }

    // --> more SQL type here

    @PostMapping("/mongodb/insert")
    public ResponseEntity<?> mongodbInsert(@RequestBody NosqlRequestDTO request) {
        return mongodbService.insert(request);
    }

    @PostMapping("/mongodb/find")
    public ResponseEntity<?> mongodbFind(@RequestBody NosqlRequestDTO request) {
        return mongodbService.find(request);
    }

    @PostMapping("/mongodb/update")
    public ResponseEntity<?> mongodbUpdate(@RequestBody NosqlRequestDTO request) {
        return mongodbService.update(request);
    }

    @PostMapping("/mongodb/delete")
    public ResponseEntity<?> mongodbDelete(@RequestBody NosqlRequestDTO request) {
        return mongodbService.delete(request);
    }

    // --> More operation here

    // --> More NoSQL type here

}
