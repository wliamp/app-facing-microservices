package vn.chuot96.jwtiss.dto;

import java.util.List;

public record RequestDTO(String provider, String subject, String scope, List<String> audience) {}
