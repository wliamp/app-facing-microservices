package vn.chuot96.chatservice.dto;

import lombok.With;

@With
public record ChatMsg(String sender, String channel, String content, long timestamp) {}
