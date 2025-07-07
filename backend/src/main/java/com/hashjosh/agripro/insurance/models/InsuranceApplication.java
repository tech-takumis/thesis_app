package com.hashjosh.agripro.insurance.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hashjosh.agripro.insurance.enums.Status;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonManagedReference("insurance-application")
    private InsuranceType insuranceType;
    @Column(columnDefinition = "timestamp(6) without time zone")
    private Timestamp createdAt;
    @Column(columnDefinition = "timestamp(6) without time zone")
    private Timestamp updatedAt;
    private Status status;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> fieldValues;
}
