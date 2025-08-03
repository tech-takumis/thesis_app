package com.hashjosh.agripro.hpj_insurance.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.hpj_insurance.enums.FieldType;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "insurance_fields")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceField{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_type_id")
    private InsuranceType insuranceType;

    @Column(name = "field_name")
    private String fieldName;

    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "field_type")
    private FieldType fieldType;

    private boolean required;

    @Type(JsonNodeBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb",name = "choices")
    private JsonNode choices;

    private String defaultValue;

    private String validationRegex;
}