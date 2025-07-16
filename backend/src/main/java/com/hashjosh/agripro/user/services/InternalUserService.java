package com.hashjosh.agripro.user.services;

import com.hashjosh.agripro.exception.UserRoleNotFoundException;
import com.hashjosh.agripro.role.Role;
import com.hashjosh.agripro.role.RoleRepository;
import com.hashjosh.agripro.rsbsa.RsbsaModel;
import com.hashjosh.agripro.rsbsa.RsbsaRepository;
import com.hashjosh.agripro.user.dto.FarmerRegistrationRequestDto;
import com.hashjosh.agripro.user.dto.StaffRegistrationRequestDto;
import com.hashjosh.agripro.user.mapper.UserMapper;
import com.hashjosh.agripro.user.models.FarmerProfile;
import com.hashjosh.agripro.user.models.StaffProfile;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.user.repository.FarmerProfileRepository;
import com.hashjosh.agripro.user.repository.StaffProfileRepository;
import com.hashjosh.agripro.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Collections;
import java.util.Optional;
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
    public User registerStaff(StaffRegistrationRequestDto dto) {

        Set<Role> roles = Collections.singleton(roleRepository.findByName(dto.role())
                .orElseThrow(() -> new UserRoleNotFoundException(String.format("User role %s not found!", dto.role()))));

        User user = userRepository.save(userMapper.toRegisterStaff(dto, roles));
        StaffProfile profile = staffProfileRepository.save(userMapper.toRegisterStaffProfileDto(dto,user));

        return user;
    }

    @Transactional
    public User registerFarmer(FarmerRegistrationRequestDto dto) {

        RsbsaModel rsbsa = rsbsaRepository.findByRsbsaIdEqualsIgnoreCase(dto.referenceNumber())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("RSBSA Control No. %s  not found!", dto.referenceNumber())));

        if(userRepository.findByUsername(dto.referenceNumber()).isPresent()){
            throw new IllegalStateException(String.format("User with Rsbsa Id: %s already exist!", dto.referenceNumber()));
        }

        Set<Role> roles = Collections.singleton(roleRepository.findByName("Farmers")
                .orElseThrow(() -> new UserRoleNotFoundException(String.format("User with Rsbsa Id: %s not found!", dto.referenceNumber()))));

        User farmer = userRepository.save(userMapper.rsbsaToFarmer(rsbsa, roles));
        FarmerProfile farmerProfile = farmerProfileRepository.save(userMapper.rsbsaToFarmerProfile(rsbsa, farmer));
        farmer.setFarmerProfile(farmerProfile);
        return farmer;
    }

    @Transactional
    public User updateStaffRole(Long roleId, int id) throws RoleNotFoundException {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(String.format("Role %s not found!", roleId)));

        User user  = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        Set<Role> roles = Collections.singleton(role);
        user.setRoles(roles);

        return userRepository.save(user);
    }
}
