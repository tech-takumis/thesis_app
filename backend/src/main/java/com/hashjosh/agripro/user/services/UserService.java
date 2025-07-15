package com.hashjosh.agripro.user.services;

import com.hashjosh.agripro.exception.UserRoleNotFoundException;
import com.hashjosh.agripro.role.Role;
import com.hashjosh.agripro.role.RoleRepository;
import com.hashjosh.agripro.role.Roles;
import com.hashjosh.agripro.rsbsa.RsbsaModel;
import com.hashjosh.agripro.rsbsa.RsbsaRepository;
import com.hashjosh.agripro.user.dto.FarmerRegistrationRequestDto;
import com.hashjosh.agripro.user.dto.StaffRegistrationRequestDto;
import com.hashjosh.agripro.user.dto.StaffResponseDto;
import com.hashjosh.agripro.user.mapper.UserMapper;
import com.hashjosh.agripro.user.models.FarmerProfile;
import com.hashjosh.agripro.user.models.StaffProfile;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.user.repository.FarmerProfileRepository;
import com.hashjosh.agripro.user.repository.StaffProfileRepository;
import com.hashjosh.agripro.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StaffProfileRepository   staffProfileRepository;
    private final FarmerProfileRepository farmerProfileRepository;
    private final RoleRepository roleRepository;
    private final RsbsaRepository rsbsaRepository;
    private final UserMapper mapper;
    private final UserEmailService service;

    public UserService(UserRepository userRepository, StaffProfileRepository staffProfileRepository, UserMapper mapper, FarmerProfileRepository farmerProfileRepository, RoleRepository roleRepository, RsbsaRepository rsbsaRepository, UserMapper mapper1, UserEmailService service) {
        this.userRepository = userRepository;
        this.staffProfileRepository = staffProfileRepository;
        this.farmerProfileRepository = farmerProfileRepository;
        this.roleRepository = roleRepository;
        this.rsbsaRepository = rsbsaRepository;
        this.mapper = mapper1;
        this.service = service;
    }


    @Transactional
    @Async
    public CompletableFuture<String> registerStaff(StaffRegistrationRequestDto dto) throws MessagingException {

        Set<Role> roles = Collections.singleton(roleRepository.findByName(dto.role())
                .orElseThrow(() -> new UserRoleNotFoundException(String.format("Role %s not found", dto.role()))));

        User user = userRepository.save(mapper.toRegisterStaff(dto, roles));

        StaffProfile staffProfile = staffProfileRepository.save(mapper.toRegisterStaffProfileDto(dto, user));

        service.sendStaffRegistrationMail(user);
        return CompletableFuture.completedFuture(String.format("Staff %s created successfully", user));
    }

    @Transactional
    @Async
    public CompletableFuture<User> registerFarmer(FarmerRegistrationRequestDto dto) throws MessagingException {
        RsbsaModel rsbsa = rsbsaRepository.findByRsbsaIdEqualsIgnoreCase(dto.referenceNumber())
                .orElseThrow(() -> new EntityNotFoundException(String.format("RSBSA Control No. %s  not found!", dto.referenceNumber())));
        if(userRepository.findByUsername(dto.referenceNumber()).isPresent()){
            throw new IllegalStateException(String.format("User with Rsbsa Id: %s already exist!", dto.referenceNumber()));
        }

        Set<Role> roles = Collections.singleton(roleRepository.findByName("Farmers")
                .orElseThrow(() -> new UserRoleNotFoundException(String.format("User with Rsbsa Id: %s not found!", dto.referenceNumber()))));

        User farmer = userRepository.save(mapper.rsbsaToFarmer(rsbsa, roles));
        FarmerProfile farmerProfile = farmerProfileRepository.save(mapper.rsbsaToFarmerProfile(rsbsa, farmer));
        service.sendFarmerRegistrationMail(farmer, rsbsa, "Farmer registration");

        return CompletableFuture.completedFuture(farmer);
    }


    public void deleteUser(int id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id::: "+ id));
        userRepository.delete(user);

    }

    @Transactional
    public String updateStaffRole(Long roleId, int id) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new UserRoleNotFoundException("User role: "+roleId+ "not found"));

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found "));

        Set<Role> roles = new HashSet<>();

        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        return "Role updated successfully";
    }

    public StaffResponseDto findUser(int id) {
        User  user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return mapper.toStaffResponse(user);

    }
}
