package com.hashjosh.agripro.user.mapper;

import com.hashjosh.agripro.authority.Authority;
import com.hashjosh.agripro.authority.AuthorityRepository;
import com.hashjosh.agripro.role.Role;
import com.hashjosh.agripro.role.RoleRepository;
import com.hashjosh.agripro.role.dtos.RoleRequestDto;
import com.hashjosh.agripro.rsbsa.RsbsaModel;
import com.hashjosh.agripro.user.dto.AuthorityDto;
import com.hashjosh.agripro.user.dto.RoleDto;
import com.hashjosh.agripro.user.dto.StaffRegistrationRequestDto;
import com.hashjosh.agripro.user.dto.StaffResponseDto;
import com.hashjosh.agripro.user.models.FarmerProfile;
import com.hashjosh.agripro.user.models.StaffProfile;
import com.hashjosh.agripro.user.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    public UserMapper(PasswordEncoder passwordEncoder, RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    public User toRegisterStaff(StaffRegistrationRequestDto dto, Set<Role> roles) {
        return User.builder()
                .fullname(dto.fullname())
                .username(dto.email().split("@")[0])
                .password(passwordEncoder.encode("123456789"))
                .gender(dto.gender())
                .civilStatus(dto.civilStatus())
                .roles(roles)
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
                .location(dto.location())
                .user(user)
                .build();
    }

    public User rsbsaToFarmer(RsbsaModel rsbsa, Set<Role> roles) {
        return  User.builder()
                .username(rsbsa.getRsbsaId())
                .email(rsbsa.getEmail())
                .roles(roles)
                .password(passwordEncoder.encode(rsbsa.getDateOfBirth().toString()))
                .fullname(rsbsa.getFirstName() + " " + rsbsa.getLastName())
                .gender(rsbsa.getGender())
                .civilStatus(rsbsa.getCivilStatus())
                .contactNumber(rsbsa.getContactNumber())
                .address(rsbsa.getAddress())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(null)
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
                .user(user)
                .build();
    }


    private Set<Authority> getAuthoritiesFromRequest(RoleRequestDto roleRequestDto) {
        return roleRequestDto.authorities().stream().map(

                this::getOrCreateAuthority
        ).collect(Collectors.toSet());
    }

    private Authority getOrCreateAuthority(Authority authorityRequestDto) {
        return authorityRepository.findByName(authorityRequestDto.getName()).orElseGet(() -> authorityRepository.save(
                Authority.builder()
                        .name(authorityRequestDto.getName())
                        .build()
        ));
    }

    private Role getOrCreateRole(RoleRequestDto roleRequestDto) {
        return roleRepository.findByName(roleRequestDto.name()).orElseGet(() -> roleRepository.save(
                Role.builder()
                        .name(roleRequestDto.name())
                        .build()
        ));
    }

    public StaffResponseDto toStaffResponse(User user) {
        return new StaffResponseDto(user.getFullname(), user.getEmail(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                user.getRoles().stream().flatMap(
                        role -> role.getAuthorities().stream().map(
                                Authority::getName
                        )
                ).collect(Collectors.toSet()),
                user.getGender(), user.getContactNumber(),
                user.getCivilStatus(),user.getAddress(),user.getStaffProfile().getPosition(),
                user.getStaffProfile().getDepartment(),user.getStaffProfile().getLocation());
    }

    private Authority getOrCreateAuthority(AuthorityDto authorityDto) {
        return authorityRepository.findByName(authorityDto.name()).orElseGet(() -> authorityRepository.save(
                Authority.builder()
                        .name(authorityDto.name())
                        .build()
        ));
    }

    private Role getOrCreateRole(RoleDto roleDto) {
        return roleRepository.findByName(roleDto.name()).orElseGet(() -> roleRepository.save(
                Role.builder()
                        .name(roleDto.name())
                        .build()
        ));
    }

}
