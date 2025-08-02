package vn.chuot96.jwtissapi.dto;

public record RequestDTO(String provider, String subject, String scope, String audience) {
}
