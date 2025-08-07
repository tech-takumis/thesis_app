package com.hashjosh.agripro.insurance.dto;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public record ApplicationRequest(
        Map<String, JsonNode> fieldValues,
        List<MultipartFile> files
        ) {
}
