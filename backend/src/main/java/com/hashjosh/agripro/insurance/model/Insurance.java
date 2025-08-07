package com.hashjosh.agripro.insurance.model;

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
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    private String layout;
    private String description;

    @Column(name = "required_ai_analysis")
    private Boolean requiredAIAnalysis;

    @OneToMany(mappedBy = "insurance")
    List<InsuranceSection> sections;

    @OneToMany(mappedBy = "insurance")
    List<InsuranceApplication> insuranceApplication;
}
