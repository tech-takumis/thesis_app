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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
    public String registerStaff(StaffRegistrationRequestDto dto) throws MessagingException {

        Set<Role> roles = Collections.singleton(roleRepository.findByName(dto.role())
                .orElseThrow(() -> new UserRoleNotFoundException("User role: "+dto.role() + "not found")));

        User user  = mapper.toRegisterStaff(dto, roles);
        var savedStaff = userRepository.save(user);

        StaffProfile profile = mapper.toRegisterStaffProfileDto(dto, savedStaff);

        staffProfileRepository.save(profile);

        service.sendStaffRegistrationMail(savedStaff);

        return "Staff " + savedStaff.getUsername() + "created successfully";
    }

    @Transactional
    public User registerFarmer(FarmerRegistrationRequestDto dto) throws MessagingException {

        RsbsaModel rsbsa = rsbsaRepository.findByRsbsaIdEqualsIgnoreCase(dto.rsbsaId())
                .orElseThrow(() -> new EntityNotFoundException("RSBSA ID not found: " + dto.rsbsaId()));

        if (userRepository.existsByUsername(dto.rsbsaId())) {
            throw new IllegalStateException("User with this RSBSA ID already exists.");
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalStateException("User with this email already exists.");
        }



        Set<Role> roles = Collections.singleton(roleRepository.findByName("Farmers")
                .orElseThrow(() -> new UserRoleNotFoundException("User role farmer does not exist, please try again")));

        User farmer = mapper.rsbsaToFarmer(rsbsa, roles);

        var savedFarmer = userRepository.save(farmer);

        FarmerProfile farmerProfile = mapper.rsbsaToFarmerProfile(rsbsa, savedFarmer);

        farmerProfileRepository.save(farmerProfile);

        service.sendFarmerRegistrationMail(savedFarmer,rsbsa, "Farmer registration");

        return savedFarmer;
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
