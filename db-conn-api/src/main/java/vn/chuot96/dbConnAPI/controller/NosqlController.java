package vn.chuot96.dbConnAPI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.chuot96.dbConnAPI.dto.NosqlRequestDTO;
import vn.chuot96.dbConnAPI.util.MongodbHandler;

import java.util.Map;

@RestController
@RequestMapping("/api/nosql")
public class NosqlController {
    @PostMapping("/mongodb")
    public ResponseEntity<?> mongodb(@RequestBody NosqlRequestDTO request) {
        String database = request.getDatabase();
        String collection = request.getCollection();
        Map<String, Object> data = request.getData();
        String uri = String.format("mongodb://%s:%s@%s:%s",
                request.getUsername(), request.getPassword(), request.getHost(), request.getPort());
        return switch (request.getOperation()) {
            case INSERT -> MongodbHandler.insert(data, uri, database, collection);
            case FIND -> MongodbHandler.find(data, uri, database, collection);
            case UPDATE -> MongodbHandler.update(data, uri, database, collection);
            case DELETE -> MongodbHandler.delete(data, uri, database, collection);
            default -> ResponseEntity.badRequest().body("Unsupported operation: " + request.getOperation());
        };
    }
}

