package com.hashjosh.agripro.role;

import com.hashjosh.agripro.authority.Authority;
import com.hashjosh.agripro.authority.AuthorityRepository;
import com.hashjosh.agripro.role.dtos.RoleCreationResponseDto;
import com.hashjosh.agripro.role.dtos.RoleRequestDto;
import com.hashjosh.agripro.role.dtos.RoleResponseDto;
import com.hashjosh.agripro.role.exceptions.RoleNotFoundException;
import com.hashjosh.agripro.user.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleMapper {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;

    public RoleMapper(AuthorityRepository authorityRepository, RoleRepository roleRepository) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
    }


    public RoleResponseDto toRoleResponse(Role role) {
            return new RoleResponseDto(
                    role.getId(),
                    role.getName(),
                    role.getAuthorities().stream().map(
                        Authority::getName
                     ).collect(Collectors.toSet()));
    }

    public Role updateRole(Long id,RoleRequestDto dto) {

        List<Authority> authorities = authorityRepository.findAllById(dto.permissionIds());

        return Role.builder()
                .id(id)
                .name(dto.name())
                .authorities(authorities)
                .build();
    }


    public RoleCreationResponseDto toRoleCreationResponse(Role role) {
        return new RoleCreationResponseDto(
                role.getName(), role.getAuthorities().stream().map(
                Authority::getName
        ).collect(Collectors.toSet()));
    }

    public List<Role> toRoleList(List<RoleRequestDto> roleRequestDtos) {
        List<Role> roles = new ArrayList<>();

        for (RoleRequestDto roleRequestDto : roleRequestDtos) {
            List<Authority> authorities = new ArrayList<>();
            for(Long id: roleRequestDto.permissionIds()){
                Optional<Authority> authority = authorityRepository.findById(id);
                if(authority.isEmpty()){
                    throw new RoleNotFoundException(
                            "Authority id "+ id + " not found.",
                            HttpStatus.NOT_FOUND.value(),
                            String.format("Authority id %d not found.", id)
                    );
                }

                authorities.add(authority.get());
            }

            Optional<Role> saveRole = roleRepository.findByName(roleRequestDto.name());

            if(saveRole.isEmpty()){
                Role role = Role.builder()
                        .name(roleRequestDto.name())
                        .authorities(authorities)
                        .build();

                roles.add(role);
            }
            else{
                saveRole.get().setAuthorities(authorities);
                roles.add(saveRole.get());
            }

        }
       return roles;
    }
}
