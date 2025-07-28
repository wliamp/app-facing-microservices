package vn.chuot96.tokenIssuerAPI.dto;
public record TokenRequestDTO(String subject, long duration, String audience, String scope) { }
