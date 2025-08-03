package com.hashjosh.agripro.role;

import com.hashjosh.agripro.authority.Authority;
import com.hashjosh.agripro.role.dtos.RoleCreationResponseDto;
import com.hashjosh.agripro.role.dtos.RoleRequestDto;
import com.hashjosh.agripro.role.dtos.RoleResponseDto;
import com.hashjosh.agripro.user.models.User;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleMapper {
    public Role dtoToRole(RoleRequestDto roleRequestDto, Set<Authority> authorities) {
        return Role.builder()
                .name(roleRequestDto.name())
                .authorities(authorities)
                .build();
    }


    public RoleResponseDto toRoleResponse(Role role) {
            return new RoleResponseDto(
                    role.getId(),
                    role.getName(),
                    role.getAuthorities().stream().map(
                        Authority::getName
                     ).collect(Collectors.toSet()));
    }

    public Role updateRole(Long id, RoleRequestDto dto) {
        return Role.builder()
                .id(id)
                .name(dto.name())
                .authorities(dto.authorities())
                .build();
    }


    public RoleCreationResponseDto toRoleCreationResponse(Role role) {
        return new RoleCreationResponseDto(
                role.getName(), role.getAuthorities().stream().map(
                Authority::getName
        ).collect(Collectors.toSet()));
    }
}
