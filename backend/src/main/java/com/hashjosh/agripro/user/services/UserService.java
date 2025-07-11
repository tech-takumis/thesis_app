package com.hashjosh.agripro.user.services;

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
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StaffProfileRepository   staffProfileRepository;
    private final FarmerProfileRepository farmerProfileRepository;
    private final RsbsaRepository rsbsaRepository;
    private final UserMapper mapper;
    private final UserEmailService service;

    public UserService(UserRepository userRepository, StaffProfileRepository staffProfileRepository, UserMapper mapper, FarmerProfileRepository farmerProfileRepository, RsbsaRepository rsbsaRepository, UserMapper mapper1, UserEmailService service) {
        this.userRepository = userRepository;
        this.staffProfileRepository = staffProfileRepository;
        this.farmerProfileRepository = farmerProfileRepository;
        this.rsbsaRepository = rsbsaRepository;
        this.mapper = mapper1;
        this.service = service;
    }

    @Transactional
    public String registerStaff(StaffRegistrationRequestDto dto) throws MessagingException {
        User user  = mapper.toRegisterStaff(dto);
        var savedStaff = userRepository.save(user);

        StaffProfile profile = mapper.toRegisterStaffProfileDto(dto, savedStaff);

        staffProfileRepository.save(profile);

        service.sendStaffRegistrationMail(savedStaff);

        return "Staff " + savedStaff.getUsername() + "created successfully";
    }

    @Transactional
    public String registerFarmer(FarmerRegistrationRequestDto dto) throws MessagingException {

        RsbsaModel rsbsa = rsbsaRepository.findByRsbsaIdEqualsIgnoreCase(dto.rsbsaId())
                .orElse(null);

        if(rsbsa == null) {
            return "Failed to create farmer rsbsa id does not exist";
        }

        User farmer = mapper.rsbsaToFarmer(rsbsa, dto.roles());
        var savedFarmer = userRepository.save(farmer);

        FarmerProfile farmerProfile = mapper.rsbsaToFarmerProfile(rsbsa, savedFarmer);

        var savedFarmerProfile = farmerProfileRepository.save(farmerProfile);

        service.sendFarmerRegistrationMail(savedFarmer,rsbsa, "Farmer registration");

        return "Farmer created successfully";
    }

    public void deleteUser(int id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id::: "+ id));
        userRepository.delete(user);

    }
}
