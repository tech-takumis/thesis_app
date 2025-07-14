package com.hashjosh.agripro.user.controller;

import com.hashjosh.agripro.user.dto.FarmerRegistrationRequestDto;
import com.hashjosh.agripro.user.dto.StaffRegistrationRequestDto;

import com.hashjosh.agripro.user.dto.StaffResponseDto;

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

import java.io.Serializable;
import java.util.Map;

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

    @GetMapping("/users/{id}")
    public ResponseEntity<StaffResponseDto> getUser(@PathVariable int id) {
        return new ResponseEntity<>(userService.findUser(id), HttpStatus.FOUND);
    }

    @PutMapping("/staff/{id}/role/{roleId}")
    public ResponseEntity<String> updateStaffRole(
            @PathVariable Long roleId, @PathVariable int id
            ){
        return new ResponseEntity<>(userService.updateStaffRole(roleId, id), HttpStatus.ACCEPTED);
    }

    @PostMapping("/register/farmers")
    public ResponseEntity<Map<String, Serializable>> registerFarmer(
            @RequestBody @Valid FarmerRegistrationRequestDto dto
    ) throws MessagingException {
        User user  = userService.registerFarmer(dto);
        return new ResponseEntity<>(Map.of(
                "message", "Farmer created sucessfully!",
                "username", user.getUsername()
        ), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
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
