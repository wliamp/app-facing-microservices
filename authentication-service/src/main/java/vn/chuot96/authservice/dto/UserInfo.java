package vn.chuot96.authservice.dto;

import java.util.List;

public record UserInfo(String credential, String scope, List<String> audience) {}
