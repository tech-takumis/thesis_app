package com.hashjosh.agripro.user.mapper;

import com.hashjosh.agripro.authority.Authority;
import com.hashjosh.agripro.role.Role;
import com.hashjosh.agripro.user.dto.StaffResponseDto;
import com.hashjosh.agripro.user.models.User;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthMapper {

    public StaffResponseDto toAuthenticatedDto(User user) {
        return new StaffResponseDto(user.getFullname(), user.getEmail(),
                userRoles(user),
                user.getRoles().stream().flatMap(role -> role.getAuthorities().stream()
                        .map(Authority::getName)).collect(Collectors.toSet()),
                user.getGender(), user.getContactNumber(),
                user.getCivilStatus(),user.getAddress(),user.getStaffProfile().getPosition(),
                user.getStaffProfile().getDepartment(),user.getStaffProfile().getLocation());
    }
    private static Set<String> userRoles(User user){
        return user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
    }

    private static Set<String> userAuthorities(User user){
        return user.getRoles().stream().flatMap(role -> role.getAuthorities()
        .stream().map(Authority::getName)).collect(Collectors.toSet());
    }
}
