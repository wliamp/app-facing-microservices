package vn.chuot96.authservice.dto;

import java.util.List;

public record UserInfo(String provider, String subject, String scope, List<String> audience) {}
