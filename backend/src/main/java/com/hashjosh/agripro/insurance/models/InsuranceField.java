package com.hashjosh.agripro.insurance.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hashjosh.agripro.insurance.enums.FormField;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String keyName;
    private FormField fieldType;
    private String displayName;
    private FormField ui_input_type;
    private String note;
    private boolean is_required;
    private Integer field_order;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "insurance_type_id", referencedColumnName = "id")
   @JsonBackReference("insurance-type")
    private InsuranceType insuranceType;
}
