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


    @Column(columnDefinition = "timestamp(6) without time zone")
    private Timestamp createdAt;
    @Column(columnDefinition = "timestamp(6) without time zone")
    private Timestamp updatedAt;
    private Status status;
    private String coordinate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference("insurance-application")
    private InsuranceType insuranceType; //Type of application used

    @ElementCollection
    @CollectionTable(name = "application_field_values",
            joinColumns = @JoinColumn(name = "application_id"))
    @MapKeyColumn(name = "field_name")
    @Column(name = "field_value")
    private Map<String, String> fieldValues; //// Stores submitted field values, mapped by field name
}
