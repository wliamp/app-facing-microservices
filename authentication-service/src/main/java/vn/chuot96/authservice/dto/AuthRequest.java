package vn.chuot96.authservice.dto;

public record AuthRequest(String provider, String subject, String objectCode // Account Credential or Application Code
        ) {}
