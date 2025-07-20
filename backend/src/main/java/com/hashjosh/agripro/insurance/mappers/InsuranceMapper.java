package com.hashjosh.agripro.insurance.mappers;

import com.hashjosh.agripro.insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceResponseDto;
import com.hashjosh.agripro.insurance.dto.PaginatedResponseDto;
import com.hashjosh.agripro.insurance.models.InsuranceField;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsuranceMapper {

    public InsuranceType toInsuranceType(InsuranceRequestDto dto) {
        return InsuranceType.builder()
                .displayName(dto.displayName())
                .description(dto.description())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .requiredAiAnalyses(dto.requiredAiAnalyses())
                .build();
    }

    public List<InsuranceField> requestToInsuranceFields(InsuranceRequestDto dto,
                                                         InsuranceType insuranceType) {

        return dto.fields().stream().map(
                field -> InsuranceField.builder()
                        .key(toSlug(field.displayName()))
                        .displayName(field.displayName())
                        .fieldType(field.fieldType())
                        .note(field.note())
                        .is_required(field.is_required())
                        .insuranceType(insuranceType)
                        .hasCoordinate(field.hasCoordinate())
                        .build()
        ).collect(Collectors.toList());
    }


    public InsuranceResponseDto toInsuranceTypeResponse(InsuranceType insurance) {
        return new InsuranceResponseDto(
                insurance.getId(),
                insurance.getDisplayName(), insurance.getDescription(),
                insurance.isRequiredAiAnalyses(),
                insurance.getFields()
        );
    }

    private String toSlug(String input) {
        return input.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .trim()
                .replaceAll("\\s+", "-");
    }

    public <T> PaginatedResponseDto<T> wrapPage(Page<T> page,
                                             String baseUrl) {
        int pageNumber = page.getNumber(); // 1
        int pageSize = page.getSize();   // 2
        int totalPages = page.getTotalPages(); // 2
        //             1
        String next = pageNumber + 1 < totalPages
                ? String.format("%s?page=%d&size=%d", baseUrl, pageNumber + 1, pageSize)
                : null;
        String prev = pageNumber > 0
                ? String.format("%s?page=%d&size=%d", baseUrl, pageNumber - 1, pageSize)
                : null;
        return  new PaginatedResponseDto<>(
                page.getContent(),
                pageNumber,
                pageSize,
                page.getTotalElements(),
                totalPages,
                page.isLast(),
                page.isFirst(),
                next,
                prev
        );
    }
}
