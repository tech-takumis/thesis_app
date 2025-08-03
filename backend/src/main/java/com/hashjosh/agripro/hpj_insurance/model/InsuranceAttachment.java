package com.hashjosh.agripro.hpj_insurance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "insurance_attachment")
public class InsuranceAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private InsuranceApplication application;

    private String fieldName;

    private String filePath;

    @CreationTimestamp
    private LocalDateTime uploadedAt;

}