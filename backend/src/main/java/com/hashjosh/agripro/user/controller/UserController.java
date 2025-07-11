package com.hashjosh.agripro.user.controller;

import com.hashjosh.agripro.user.dto.FarmerRegistrationRequestDto;
import com.hashjosh.agripro.user.dto.StaffRegistrationRequestDto;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.user.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService   userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @PreAuthorize("hasRole('STAFF_ADMIN')")
    @PostMapping("/register/staffs")
    public ResponseEntity<String> registerStaff(
            @RequestBody @Valid  StaffRegistrationRequestDto dto
    ) throws MessagingException {
        return ResponseEntity.ok(userService.registerStaff(dto));
    }

    @PostMapping("/register/farmers")
    public ResponseEntity<String> registerFarmer(
            @RequestBody @Valid FarmerRegistrationRequestDto dto
    ) throws MessagingException {
        return new ResponseEntity<>(userService.registerFarmer(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable int id
    ){
        try{
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }catch(EmptyResultDataAccessException ex){
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
     }
}
