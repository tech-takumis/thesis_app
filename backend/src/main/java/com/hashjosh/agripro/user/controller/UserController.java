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

import javax.management.relation.RoleNotFoundException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService  userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/staffs")
    public ResponseEntity<Map<String,String>> registerStaff(
            @RequestBody @Valid  StaffRegistrationRequestDto dto
    ) throws MessagingException {
        userService.registerStaff(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Staff registered successfully"));
    }

    @PostMapping("/farmers")
    public ResponseEntity<Map<String,String>> registerFarmer(
            @RequestBody @Valid FarmerRegistrationRequestDto dto
    ) throws MessagingException{

        userService.registerFarmer(dto);
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
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }catch(EmptyResultDataAccessException ex){
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

}
