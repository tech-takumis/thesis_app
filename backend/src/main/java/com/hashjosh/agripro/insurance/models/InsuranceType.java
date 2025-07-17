package com.hashjosh.agripro.insurance.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "insurance_type")
public class InsuranceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private String displayName;
    @Column(columnDefinition = "timestamp(6) without time zone")
    private Timestamp createdAt;
    @Column(columnDefinition = "timestamp(6) without time zone")
    private Timestamp updatedAt;
    private  boolean requiredAiAnalyses;

    @OneToMany(mappedBy = "insuranceType", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("insurance-type")
    private List<InsuranceField> fields;

    @OneToMany(mappedBy = "insuranceType")
    private List<InsuranceApplication> applications;
}
