package com.hashjosh.agripro.user.controller;

import com.hashjosh.agripro.role.exceptions.RoleNotFoundException;
import com.hashjosh.agripro.user.dto.FarmerRegistrationRequestDto;
import com.hashjosh.agripro.user.dto.StaffRegistrationRequestDto;
import com.hashjosh.agripro.user.dto.StaffResponseDto;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.user.services.UserEmailService;
import com.hashjosh.agripro.user.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService  userService;
    private final UserEmailService emailService;

    public UserController(UserService userService, UserEmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }


//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/staffs")
    public ResponseEntity<Map<String,String>> registerStaff(
            @RequestBody @Valid  StaffRegistrationRequestDto dto
    ) throws MessagingException {
        // Save the user to the database
        User user = userService.registerStaff(dto);
        // Send the email to the user
        emailService.sendStaffRegistrationMail(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Staff registered successfully"));
    }

    @PostMapping("/farmers")
    public ResponseEntity<Map<String,String>> registerFarmer(
            @RequestBody @Valid FarmerRegistrationRequestDto dto
    ) throws MessagingException {
        User user = userService.registerFarmer(dto);
        emailService.sendFarmerRegistrationMail(user);
       return  ResponseEntity.status(HttpStatus.CREATED)
                       .body(Map.of(
                               "message", "Farmer created successfully!"
                       ));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<StaffResponseDto> getUser(@PathVariable int id) {
        return new ResponseEntity<>(userService.findUser(id), HttpStatus.FOUND);
    }

    // This update staff role
    @PutMapping("/staff/{id}/role/{roleId}")
    public ResponseEntity<CompletableFuture<String>> updateStaffRole(
            @PathVariable Long roleId, @PathVariable int id
            ) throws RoleNotFoundException {
        return new ResponseEntity<>(userService.updateStaffRole(roleId, id), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable int id
    ){
        try{
            userService.hardDelete(id);
            return ResponseEntity.noContent().build();
        }catch(EmptyResultDataAccessException ex){
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

}
