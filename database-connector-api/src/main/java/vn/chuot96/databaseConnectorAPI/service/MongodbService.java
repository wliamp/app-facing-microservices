package vn.chuot96.databaseConnectorAPI.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import vn.chuot96.databaseConnectorAPI.dto.NosqlRequestDTO;
import vn.chuot96.databaseConnectorAPI.util.MongodbHandler;

import static vn.chuot96.databaseConnectorAPI.constant.MongodbURI.PATTERN;

@Service
public class MongodbService {

    public static ResponseEntity<?> insert(@RequestBody NosqlRequestDTO request) {
        return MongodbHandler.insert(request.getData(), PATTERN.setUri(request), request.getDatabase(), request.getCollection());
    }

    public static ResponseEntity<?> find(@RequestBody NosqlRequestDTO request) {
        return MongodbHandler.find(request.getData(), PATTERN.setUri(request), request.getDatabase(), request.getCollection());
    }

    public static ResponseEntity<?> update(@RequestBody NosqlRequestDTO request) {
        return MongodbHandler.update(request.getData(), PATTERN.setUri(request), request.getDatabase(), request.getCollection());
    }

    public static ResponseEntity<?> delete(@RequestBody NosqlRequestDTO request) {
        return MongodbHandler.delete(request.getData(), PATTERN.setUri(request), request.getDatabase(), request.getCollection());
    }

}
