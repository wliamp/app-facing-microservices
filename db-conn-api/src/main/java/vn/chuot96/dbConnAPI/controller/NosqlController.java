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
    @PostMapping("/query")
    public ResponseEntity<?> query(@RequestBody NosqlRequestDTO request) {
        String type = request.getType().toLowerCase();
        String operation = request.getOperation();
        String schema = request.getSchema();
        String collection = request.getQuery();
        Map<String, Object> data = request.getData();
        String uri = String.format("mongodb://%s:%s@%s:%s",
                request.getUsername(), request.getPassword(), request.getHost(), request.getPort());
        switch (type) {
            case "mongodb":
                return switch (operation) {
                    case "insert" -> MongodbHandler.insert(data, uri, schema, collection);
//                    case "find" -> MongodbHandler.find(data, uri, schema, collection);
//                    case "update" -> MongodbHandler.update(data, uri, schema, collection);
//                    case "delete" -> MongodbHandler.delete(data, uri, schema, collection);
                    default -> ResponseEntity.badRequest().body("Unsupported operation: " + operation);
                };
            default:
                return ResponseEntity.badRequest().body("Unsupported NoSQL type: " + type);
        }
    }
}
