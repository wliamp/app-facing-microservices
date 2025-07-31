package vn.chuot96.dbconnapi.util;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import vn.chuot96.dbconnapi.dto.NosqlRequestDTO;

import java.util.*;

import static vn.chuot96.dbconnapi.constant.MongodbURI.PATTERN;

public class MongodbHandler {

    private static ResponseEntity<?> handleMongoOperation(String uri, MongoOperation operation) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            return operation.execute(mongoClient);
        } catch (MongoException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("MongoDB Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected Error: " + e.getMessage());
        }
    }

    public static ResponseEntity<?> insert(NosqlRequestDTO request) {
        return handleMongoOperation(PATTERN.setUri(request), client -> {
            getCollection(client, request.getDatabase(), request.getCollection()).insertOne(new Document(request.getData()));
            return ResponseEntity.ok("Inserted successfully");
        });
    }

    public static ResponseEntity<?> find(NosqlRequestDTO request) {
        return handleMongoOperation(PATTERN.setUri(request), client -> {
            FindIterable<Document> docs = getCollection(client, request.getDatabase(), request.getCollection()).find(new Document(request.getFilter()));
            List<Map<String, Object>> results = new ArrayList<>();
            for (Document doc : docs) {
                results.add(doc);
            }
            return ResponseEntity.ok(results);
        });
    }

    public static ResponseEntity<?> update(NosqlRequestDTO request) {
        return handleMongoOperation(PATTERN.setUri(request), client -> {
            Map<String, Object> filter = (Map<String, Object>) request.getData().get("filter");
            Map<String, Object> update = (Map<String, Object>) request.getData().get("update");

            if (filter == null || update == null) {
                return ResponseEntity.badRequest().body("Missing 'filter' or 'update'");
            }

            UpdateResult result = getCollection(client, request.getDatabase(), request.getCollection())
                    .updateMany(new Document(filter), new Document("$set", new Document(update)));
            return ResponseEntity.ok("Updated documents: " + result.getModifiedCount());
        });
    }

    public static ResponseEntity<?> delete(NosqlRequestDTO request) {
        return handleMongoOperation(PATTERN.setUri(request), client -> {
            DeleteResult result = getCollection(client, request.getDatabase(), request.getCollection()).deleteMany(new Document(request.getFilter()));
            return ResponseEntity.ok("Deleted documents: " + result.getDeletedCount());
        });
    }

    private static MongoCollection<Document> getCollection(MongoClient client, String dbName, String collectionName) {
        return client.getDatabase(dbName).getCollection(collectionName);
    }

    @FunctionalInterface
    private interface MongoOperation {
        ResponseEntity<?> execute(MongoClient client);
    }
}
