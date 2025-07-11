package com.hashjosh.agripro.user.mapper;

import com.hashjosh.agripro.user.dto.AuthenticatedStaffResponseDto;
import com.hashjosh.agripro.user.models.Authority;
import com.hashjosh.agripro.user.models.Role;
import com.hashjosh.agripro.user.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthMapper {

    public AuthenticatedStaffResponseDto toAuthenticatedDto(User user) {
        return new AuthenticatedStaffResponseDto(user.getFullname(), user.getEmail(),
                userRoles(user), userAuthorities(user),
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
