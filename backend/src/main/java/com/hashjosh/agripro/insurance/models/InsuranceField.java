package com.hashjosh.agripro.insurance.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hashjosh.agripro.insurance.enums.Datatype;
import com.hashjosh.agripro.insurance.enums.FormField;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "insurance_fields")
public class InsuranceField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String key;
    @Enumerated(EnumType.STRING)
    @Column(name = "field_type")
    private Datatype fieldType;
    private String displayName;
    private String note;
    private boolean is_required;
    private boolean hasCoordinate;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "insurance_type_id", referencedColumnName = "id")
   @JsonBackReference("insurance-type")
    private InsuranceType insuranceType;
}
