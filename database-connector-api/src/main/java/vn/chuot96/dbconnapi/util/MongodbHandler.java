package vn.chuot96.dbconnapi.util;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MongodbHandler {

    public ResponseEntity<?> handleMongoOperation(String uri, MongoOperation operation) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            return operation.execute(mongoClient);
        } catch (MongoException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("MongoDB Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error: " + e.getMessage());
        }
    }

    public MongoCollection<Document> getCollection(MongoClient client, String dbName, String collectionName) {
        return client.getDatabase(dbName).getCollection(collectionName);
    }

    @FunctionalInterface
    public interface MongoOperation {
        ResponseEntity<?> execute(MongoClient client);
    }
}
