package com.hashjosh.agripro.insurance.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.user.models.User;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "insurance_applicaiton")
public class InsuranceApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_type_id")
    private Insurance insurance;

    private String status = "pending";

    @Column(name = "submitted_at",updatable = false)
    @CreationTimestamp
    private LocalDateTime submittedAt;

    @Type(JsonNodeBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb",name = "dynamic_fields")
    private JsonNode dynamicFields;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InsuranceAttachment> attachments;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InsuranceComment> comments;

}
