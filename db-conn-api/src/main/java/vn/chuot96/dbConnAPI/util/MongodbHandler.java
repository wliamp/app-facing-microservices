package vn.chuot96.dbConnAPI.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class MongodbHandler {

    public static ResponseEntity<?> insert(Map<String, Object> requestData, String uri, String dbName, String collectionName) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoCollection<Document> collection = getCollection(mongoClient, dbName, collectionName);
            Document doc = new Document(requestData);
            collection.insertOne(doc);
            return ResponseEntity.ok("Inserted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Insert error: " + e.getMessage());
        }
    }

    private static MongoCollection<Document> getCollection(MongoClient client, String dbName, String collectionName) {
        MongoDatabase db = client.getDatabase(dbName);
        return db.getCollection(collectionName);
    }

}
