package vn.chuot96.jwtissapi.dto;
public record TokenRequestDTO(String subject, long duration, String audience, String scope) { }
