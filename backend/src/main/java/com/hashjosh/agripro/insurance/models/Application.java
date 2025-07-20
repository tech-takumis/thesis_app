package com.hashjosh.agripro.insurance.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hashjosh.agripro.insurance.enums.Status;
import com.hashjosh.agripro.user.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Map;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(columnDefinition = "timestamp(6) without time zone")
    private Timestamp createdAt;
    @Column(columnDefinition = "timestamp(6) without time zone")
    private Timestamp updatedAt;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_type_id", referencedColumnName = "id", nullable = false)
    private InsuranceType insuranceType; //Type of application used

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference("user-applications")
    private User user;

    @ElementCollection
    @CollectionTable(name = "application_field_values",
            joinColumns = @JoinColumn(name = "application_id"))
    @MapKeyColumn(name = "field_name")
    @Column(name = "field_value")
    private Map<String, String> fieldValues; //// Stores submitted field values, mapped by field name
}
