package vn.chuot96.authservice.dto;

import lombok.Builder;

@Builder
public record Tokens(String access, String refresh) {}
