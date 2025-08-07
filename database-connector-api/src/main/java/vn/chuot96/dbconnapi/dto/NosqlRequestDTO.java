package vn.chuot96.dbconnapi.dto;

import java.util.Map;

public record NosqlRequestDTO(
        DbRequestDTO db, String collection, Map<String, Object> data, Map<String, Object> filter) {}
