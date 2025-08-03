package com.hashjosh.agripro.hpj_insurance.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "insurance_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "insurance_type",nullable = false)
    private String InsuranceType;
    private String description;

    @Column(name = "required_ai_analysis")
    private Boolean requiredAIAnalysis;

    @OneToMany(mappedBy = "insuranceType")
    List<InsuranceField> insuranceField;


    @OneToMany(mappedBy = "insuranceType")
    List<InsuranceApplication> insuranceApplication;
}
