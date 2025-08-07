package com.hashjosh.agripro.user.services;

import com.hashjosh.agripro.insurance.exception.InsuranceApplicationException;
import com.hashjosh.agripro.role.exceptions.RoleNotFoundException;
import com.hashjosh.agripro.global.dto.AuthenticatedUserDto;
import com.hashjosh.agripro.user.dto.FarmerRegistrationRequestDto;
import com.hashjosh.agripro.user.dto.StaffRegistrationRequestDto;
import com.hashjosh.agripro.user.dto.StaffResponseDto;
import com.hashjosh.agripro.user.exceptions.UserAlreadyExistException;
import com.hashjosh.agripro.user.mapper.UserMapper;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final InternalUserService internalUserService;
    public UserService(UserRepository userRepository,
                       UserMapper mapper1,
                       UserEmailService service, InternalUserService internalUserService) {
        this.userRepository = userRepository;
        this.mapper = mapper1;
        this.internalUserService = internalUserService;
    }

    public User registerStaff(StaffRegistrationRequestDto dto) throws MessagingException {

        if(userRepository.findByEmail(dto.email()).isPresent()){
            throw new UserAlreadyExistException(
                    "User with email "+ dto.email() + "already exist please try again",
                    HttpStatus.BAD_REQUEST.value(),
                    "User already exist"
            );
        }
        return internalUserService.registerStaff(dto);
    }

    public AuthenticatedUserDto getAuthenticateUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null){
            throw new InsuranceApplicationException(
                    "User is not authenticated",
                    "User is not authenticated",
                    HttpStatus.UNAUTHORIZED.value()
            );
        }

        String username = authentication.getName();

        User user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return mapper.toAuthenticatedUser(user);

    }

    public User registerFarmer(FarmerRegistrationRequestDto dto) throws MessagingException {
        return internalUserService.registerFarmer(dto);
    }

    public void softDelete(int id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
        }
    }


    @Transactional
    public void hardDelete(int id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(userRepository::delete);
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
