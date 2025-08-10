package vn.chuot96.authservice.dto;

import java.util.List;

public record UserInfo(String cred, String scope, List<String> auds) {}
