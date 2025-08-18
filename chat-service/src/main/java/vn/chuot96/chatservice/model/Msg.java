package vn.chuot96.chatservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "messages")
public class Msg {
    @Id
    private String id;

    private String sender;
    private String content;
    private String channel;
    private long timestamp;
}
