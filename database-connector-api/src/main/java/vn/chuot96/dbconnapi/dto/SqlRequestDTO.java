package vn.chuot96.dbconnapi.dto;

import java.util.List;

public record SqlRequestDTO(DbRequestDTO db, String query, List<Object> params) {}
