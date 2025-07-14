package com.hashjosh.agripro.user.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.hashjosh.agripro.role.Role;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;


@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;
    private String fullname;
    private String gender;
    private String contactNumber;
    private String civilStatus;
    private String address;
    private Timestamp createdAt;
    private Timestamp updatedAt;


    @OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonManagedReference("user-farmer")
    private FarmerProfile farmerProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonManagedReference("user-staff")
    private StaffProfile staffProfile;



    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_Id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )

    private Set<Role> roles;
}
