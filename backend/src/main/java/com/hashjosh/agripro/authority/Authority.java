package com.hashjosh.agripro.authority;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hashjosh.agripro.role.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles;
}
