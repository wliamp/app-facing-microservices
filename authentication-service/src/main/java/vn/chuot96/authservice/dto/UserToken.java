package vn.chuot96.authservice.dto;

import lombok.Builder;

@Builder
public record UserToken(String access, String refresh) {}
