package vn.chuot96.chatservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import vn.chuot96.chatservice.dto.ChatMsg;

@Slf4j
public class SafeHandler {
    public static String safeJson(ObjectMapper mapper, ChatMsg msg) {
        try {
            return mapper.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            log.error("JSON serialization failed", e);
            return "{\"sender\":\"" + safe(msg.sender()) + "\",\"content\":\"" + safe(msg.content()) + "\"}";
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
}
