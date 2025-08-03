package com.hashjosh.agripro.user.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FarmerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate dateOfBirth;
    private String farmingType;
    private String primaryCrop;
    private String secondaryCrop;
    private Float farmArea;
    private String farmLocation;
    private String tenureStatus;
    private String sourceOfIncome;
    private float estimatedIncome;
    private Integer householdSize;
    private String educationLevel;
    private boolean withDisability;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
//    @JsonBackReference("user-farmer")
    private User user;

}
