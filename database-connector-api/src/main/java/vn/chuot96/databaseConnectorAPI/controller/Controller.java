package vn.chuot96.databaseConnectorAPI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.chuot96.databaseConnectorAPI.dto.NosqlRequestDTO;
import vn.chuot96.databaseConnectorAPI.dto.SqlRequestDTO;
import vn.chuot96.databaseConnectorAPI.service.MongodbService;
import vn.chuot96.databaseConnectorAPI.service.SqlService;

@RestController
@RequestMapping("/db")
public class Controller {

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

    // --> more SQL type here

    @PostMapping("/nosql/mongodb/insert")
    public ResponseEntity<?> mongodbInsert(@RequestBody NosqlRequestDTO request) {
        return MongodbService.insert(request);
    }

    @PostMapping("/nosql/mongodb/find")
    public ResponseEntity<?> mongodbFind(@RequestBody NosqlRequestDTO request) {
        return MongodbService.find(request);
    }

    @PostMapping("/nosql/mongodb/update")
    public ResponseEntity<?> mongodbUpdate(@RequestBody NosqlRequestDTO request) {
        return MongodbService.update(request);
    }

    @PostMapping("/nosql/mongodb/delete")
    public ResponseEntity<?> mongodbDelete(@RequestBody NosqlRequestDTO request) {
        return MongodbService.delete(request);
    }

    // --> More operation here

    // --> More NoSQL type here

}
