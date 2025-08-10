package vn.chuot96.jwtiss.dto;

import java.util.List;

public record Request(String party, String subject, String scope, List<String> audiences) {}
