package com.hashjosh.agripro.hpj_insurance.dto;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public record InsuranceApplicationRequest(
        Map<String, JsonNode> fieldValues,
        List<MultipartFile> files
        ) {
}
