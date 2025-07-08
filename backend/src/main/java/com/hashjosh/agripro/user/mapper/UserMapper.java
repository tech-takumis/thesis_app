package com.hashjosh.agripro.user.mapper;

import com.hashjosh.agripro.rsbsa.RsbsaModel;
import com.hashjosh.agripro.user.dto.AuthenticatedStaffResponseDto;
import com.hashjosh.agripro.user.dto.StaffRegistrationRequestDto;
import com.hashjosh.agripro.user.models.FarmerProfile;
import com.hashjosh.agripro.user.models.StaffProfile;
import com.hashjosh.agripro.user.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toRegisterStaff(StaffRegistrationRequestDto dto) {
        return User.builder()
                .fullname(dto.fullname())
                .username(dto.email().split("@")[0])
                .password(passwordEncoder.encode("123456789"))
                .gender(dto.gender())
                .civilStatus(dto.civilStatus())
                .contactNumber(dto.contactNumber())
                .address(dto.address())
                .email(dto.email())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
    }


    public StaffProfile toRegisterStaffProfileDto(StaffRegistrationRequestDto dto, User user) {
        return StaffProfile.builder()
                .position(dto.position())
                .department(dto.department())
                .role(dto.role())
                .location(dto.location())
                .user(User.builder()
                        .id(user.getId())
                        .build())
                .build();
    }

    public User rsbsaToFarmer(RsbsaModel rsbsa) {
        return  User.builder()
                .username(rsbsa.getRsbsaId())
                .email(rsbsa.getEmail())
                .password(passwordEncoder.encode(rsbsa.getDateOfBirth().toString()))
                .fullname(rsbsa.getFirstName() + " " + rsbsa.getLastName())
                .gender(rsbsa.getGender())
                .civilStatus(rsbsa.getCivilStatus())
                .contactNumber(rsbsa.getContactNumber())
                .address(rsbsa.getAddress())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public FarmerProfile rsbsaToFarmerProfile(RsbsaModel rsbsa, User user) {
        return FarmerProfile.builder()
                .dateOfBirth(rsbsa.getDateOfBirth())
                .farmingType(rsbsa.getFarmingType())
                .primaryCrop(rsbsa.getPrimaryCrop())
                .secondaryCrop(rsbsa.getSecondaryCrop())
                .farmArea(rsbsa.getFarmArea())
                .farmLocation(rsbsa.getFarmLocation())
                .tenureStatus(rsbsa.getTenureStatus())
                .sourceOfIncome(rsbsa.getSourceOfIncome())
                .estimatedIncome(rsbsa.getEstimatedIncome())
                .householdSize(rsbsa.getHouseholdSize())
                .educationLevel(rsbsa.getEducationLevel())
                .withDisability(rsbsa.isWithDisability())
                .user(User.builder()
                        .id(user.getId())
                        .build())
                .build();
    }

    public AuthenticatedStaffResponseDto toAuthenticatedDto(User user) {
        return new AuthenticatedStaffResponseDto(user.getFullname(), user.getEmail(),
                user.getStaffProfile().getRole(), user.getGender(), user.getContactNumber(),
                user.getCivilStatus(),user.getAddress(),user.getStaffProfile().getPosition(),
                user.getStaffProfile().getDepartment(),user.getStaffProfile().getLocation());
    }
}
