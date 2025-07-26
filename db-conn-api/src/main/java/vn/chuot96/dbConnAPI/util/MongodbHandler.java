package vn.chuot96.dbConnAPI.util;

import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
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

    public static ResponseEntity<?> find(Map<String, Object> filterData, String uri, String dbName, String collectionName) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoCollection<Document> collection = getCollection(mongoClient, dbName, collectionName);
            Document filter = new Document(filterData);
            FindIterable<Document> result = collection.find(filter);

            List<Map<String, Object>> list = new ArrayList<>();
            for (Document doc : result) {
                list.add(doc);
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Find error: " + e.getMessage());
        }
    }

    public static ResponseEntity<?> update(Map<String, Object> requestData, String uri, String dbName, String collectionName) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoCollection<Document> collection = getCollection(mongoClient, dbName, collectionName);

            Map<String, Object> filterMap = (Map<String, Object>) requestData.get("filter");
            Map<String, Object> updateMap = (Map<String, Object>) requestData.get("update");

            if (filterMap == null || updateMap == null) {
                return ResponseEntity.badRequest().body("Update request must contain 'filter' and 'update' fields.");
            }

            Document filterDoc = new Document(filterMap);
            Document updateDoc = new Document("$set", new Document(updateMap));

            UpdateResult result = collection.updateMany(filterDoc, updateDoc);
            return ResponseEntity.ok("Updated documents: " + result.getModifiedCount());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Update error: " + e.getMessage());
        }
    }

    public static ResponseEntity<?> delete(Map<String, Object> filterData, String uri, String dbName, String collectionName) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoCollection<Document> collection = getCollection(mongoClient, dbName, collectionName);
            Document filter = new Document(filterData);
            DeleteResult result = collection.deleteMany(filter);
            return ResponseEntity.ok("Deleted documents: " + result.getDeletedCount());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Delete error: " + e.getMessage());
        }
    }

    private static MongoCollection<Document> getCollection(MongoClient client, String dbName, String collectionName) {
        MongoDatabase db = client.getDatabase(dbName);
        return db.getCollection(collectionName);
    }

}
