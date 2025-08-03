package com.hashjosh.agripro.user.services;

import com.hashjosh.agripro.role.Role;
import com.hashjosh.agripro.role.RoleRepository;
import com.hashjosh.agripro.role.exceptions.RoleNotFoundException;
import com.hashjosh.agripro.rsbsa.RsbsaModel;
import com.hashjosh.agripro.rsbsa.RsbsaRepository;
import com.hashjosh.agripro.user.dto.FarmerRegistrationRequestDto;
import com.hashjosh.agripro.user.dto.StaffRegistrationRequestDto;
import com.hashjosh.agripro.user.exceptions.FarmerRegistrationException;
import com.hashjosh.agripro.user.mapper.UserMapper;
import com.hashjosh.agripro.user.models.FarmerProfile;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.user.repository.FarmerProfileRepository;
import com.hashjosh.agripro.user.repository.StaffProfileRepository;
import com.hashjosh.agripro.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class InternalUserService {

    private final UserRepository userRepository;
    private final RsbsaRepository rsbsaRepository;
    private final UserMapper userMapper;
    private final FarmerProfileRepository farmerProfileRepository;
    private final StaffProfileRepository staffProfileRepository;
    private final RoleRepository roleRepository;

    public InternalUserService(UserRepository userRepository, RsbsaRepository rsbsaRepository, UserMapper userMapper, FarmerProfileRepository farmerProfileRepository, StaffProfileRepository staffProfileRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.rsbsaRepository = rsbsaRepository;
        this.userMapper = userMapper;
        this.farmerProfileRepository = farmerProfileRepository;
        this.staffProfileRepository = staffProfileRepository;
        this.roleRepository = roleRepository;
    }


    @Transactional
    public User registerStaff(StaffRegistrationRequestDto dto){

        Set<Role> role = Collections.singleton(roleRepository.findById(dto.roleId()).orElseThrow(
                () -> new RoleNotFoundException(
                        "Role id not found",
                        HttpStatus.NOT_FOUND.value(),
                        "Role not found"
                )
        ));
        User user = userRepository.save(userMapper.toRegisterStaff(dto, role));
        staffProfileRepository.save(userMapper.toRegisterStaffProfileDto(dto,user));

        return user;
    }

    @Transactional
    public User registerFarmer(FarmerRegistrationRequestDto dto){

        RsbsaModel rsbsa = rsbsaRepository.findByRsbsaIdEqualsIgnoreCase(dto.referenceNumber())
                .orElseThrow(() -> new FarmerRegistrationException(
                        "RSBSA Reference number " + dto.referenceNumber() + " not found",
                        HttpStatus.NOT_FOUND.value(),
                        "RSBSA Not Found"
                ));

        if(userRepository.findByUsername(dto.referenceNumber()).isPresent()){
            throw new FarmerRegistrationException(
                    "User with RSBSA number already exists.",
                    HttpStatus.CONFLICT.value(),
                    "Duplicate User"
            );
        }

        Set<Role> role = Collections.singleton(roleRepository.findByName("Farmer").orElseThrow(
                () -> new RoleNotFoundException(
                        "Role id not found",
                        HttpStatus.NOT_FOUND.value(),
                        "Role not found"
                )
        ));

        User farmer = userRepository.save(userMapper.rsbsaToFarmer(rsbsa,role));
        FarmerProfile farmerProfile = farmerProfileRepository.save(userMapper.rsbsaToFarmerProfile(rsbsa, farmer));
        farmer.setFarmerProfile(farmerProfile);
        return farmer;
    }

    @Transactional
    public User updateStaffRole(Long roleId, int id){
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(
                        "Role id "+roleId+ "not found",
                        HttpStatus.NOT_FOUND.value(),
                        "Role not found"
                ));
        User user  = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        Set<Role> roles = Collections.singleton(role);
        user.setRoles(roles);

        return userRepository.save(user);
    }
}
