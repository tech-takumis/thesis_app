package com.hashjosh.agripro.user.services;

import com.hashjosh.agripro.role.RoleRepository;
import com.hashjosh.agripro.user.dto.FarmerRegistrationRequestDto;
import com.hashjosh.agripro.user.dto.StaffRegistrationRequestDto;
import com.hashjosh.agripro.user.dto.StaffResponseDto;
import com.hashjosh.agripro.user.mapper.UserMapper;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final UserEmailService service;
    private final InternalUserService internalUserService;
    public UserService(UserRepository userRepository,
                       UserMapper mapper1,
                       UserEmailService service, InternalUserService internalUserService) {
        this.userRepository = userRepository;
        this.mapper = mapper1;
        this.service = service;
        this.internalUserService = internalUserService;
    }


    @Async
    public void registerStaff(StaffRegistrationRequestDto dto) throws MessagingException {

        User user = internalUserService.registerStaff(dto);
        service.sendStaffRegistrationMail(user);
    }


    @Async
    public void registerFarmer(FarmerRegistrationRequestDto dto) throws MessagingException {

        User user = internalUserService.registerFarmer(dto);
        service.sendFarmerRegistrationMail(user);
    }


    public void deleteUser(int id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id::: "+ id));
        userRepository.delete(user);

    }

    @Async
    public CompletableFuture<String> updateStaffRole(Long roleId, int id)
            throws RoleNotFoundException {

        User user = internalUserService.updateStaffRole(roleId, id);
        return CompletableFuture.completedFuture(String.format("Staff %s updated successfully", user.getUsername()));
    }

    public StaffResponseDto findUser(int id) {
        User  user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return mapper.toStaffResponse(user);

    }
}
