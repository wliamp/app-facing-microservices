package vn.chuot96.authservice.dto;

public record LinkRequest(String oldCred, AuthRequest newCred) {}
