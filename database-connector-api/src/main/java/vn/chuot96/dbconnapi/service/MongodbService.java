package vn.chuot96.dbconnapi.service;

import static vn.chuot96.dbconnapi.constant.MongodbURI.MONGODB;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.chuot96.dbconnapi.dto.NosqlRequestDTO;
import vn.chuot96.dbconnapi.util.MongodbHandler;

@Service
@RequiredArgsConstructor
public class MongodbService {
    private final MongodbHandler handler;

    private final ObjectMapper mapper;

    public ResponseEntity<?> insert(NosqlRequestDTO request) {
        return handler.handleMongoOperation(MONGODB.setUri(request), client -> {
            handler.getCollection(client, request.db().dbName(), request.collection())
                    .insertOne(new Document(request.data()));
            return ResponseEntity.ok("Inserted successfully");
        });
    }

    public ResponseEntity<?> find(NosqlRequestDTO request) {
        return handler.handleMongoOperation(MONGODB.setUri(request), client -> {
            FindIterable<Document> docs = handler.getCollection(
                            client, request.db().dbName(), request.collection())
                    .find(new Document(request.filter()));
            List<Map<String, Object>> results = new ArrayList<>();
            for (Document doc : docs) {
                results.add(doc);
            }
            return ResponseEntity.ok(results);
        });
    }

    public ResponseEntity<?> update(NosqlRequestDTO request) {
        return handler.handleMongoOperation(MONGODB.setUri(request), client -> {
            Object filterObj = request.data().get("filter");
            Object updateObj = request.data().get("update");
            if (filterObj == null || updateObj == null) {
                return ResponseEntity.badRequest().body("Missing 'filter' or 'update'");
            }
            Map<String, Object> filter;
            Map<String, Object> update;
            try {
                filter = mapper.convertValue(filterObj, new TypeReference<>() {});
                update = mapper.convertValue(updateObj, new TypeReference<>() {});
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid 'filter' or 'update' format: " + e.getMessage());
            }
            UpdateResult result = handler.getCollection(client, request.db().dbName(), request.collection())
                    .updateMany(new Document(filter), new Document("$set", new Document(update)));
            return ResponseEntity.ok("Updated documents: " + result.getModifiedCount());
        });
    }

    public ResponseEntity<?> delete(NosqlRequestDTO request) {
        return handler.handleMongoOperation(MONGODB.setUri(request), client -> {
            DeleteResult result = handler.getCollection(client, request.db().dbName(), request.collection())
                    .deleteMany(new Document(request.filter()));
            return ResponseEntity.ok("Deleted documents: " + result.getDeletedCount());
        });
    }
}
