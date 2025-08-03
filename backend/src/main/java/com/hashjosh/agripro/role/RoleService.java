package com.hashjosh.agripro.role;

import com.hashjosh.agripro.authority.Authority;
import com.hashjosh.agripro.authority.AuthorityRepository;
import com.hashjosh.agripro.role.dtos.RoleCreationResponseDto;
import com.hashjosh.agripro.role.dtos.RoleRequestDto;
import com.hashjosh.agripro.role.dtos.RoleResponseDto;
import com.hashjosh.agripro.role.exceptions.RoleNotFoundException;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    private  final RoleMapper mapper;

    public RoleService(RoleRepository roleRepository, AuthorityRepository authorityRepository, UserRepository userRepository, RoleMapper mapper) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public RoleCreationResponseDto createRole(RoleRequestDto roleRequestDto) {

        // Get or create the authorities if does not exist in the database
        // to avoid redundant value of authority in the database
        Set<Authority> authority = getAuthorities(roleRequestDto.authorities());

        // Same as the role we check first the database if the role already exist to avoid redundant
        // insertion of role in the database
        Role role = getOrCreateRole(roleRequestDto);

        role.setAuthorities(authority);
        role = roleRepository.save(role);

        return mapper.toRoleCreationResponse(role);
    }


    public List<RoleResponseDto> findAll() {
        return roleRepository.findAll().stream()
                    .map(mapper::toRoleResponse)
                    .collect(Collectors.toList());
    }

    public RoleResponseDto update(Long id, RoleRequestDto dto){

        roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(
                        "Role not found",
                        HttpStatus.NOT_FOUND.value(),
                        String.format("Role id %d not found.", id)
                ));

        Role role = mapper.updateRole(id, dto);
        return mapper.toRoleResponse(role);
    }

    public void delete(Long id){

        Role role = roleRepository.findById(id)
                        .orElseThrow(() -> new RoleNotFoundException(
                                "Role not found",
                                HttpStatus.NOT_FOUND.value(),
                                String.format("Role id %d not found.", id)
                        ));

        List<User> users = userRepository.findAll();

        for (User user : users) {
            user.getRoles().remove(role);
        }

        userRepository.saveAll(users);
        roleRepository.deleteById(id);
    }


    // private function
    private Set<Authority> getAuthorities(Set<Authority> authorities) {
        return authorities.stream().map(
                this::getOrCreateAuthority
        ).collect(Collectors.toSet());
    }
    private  Authority getOrCreateAuthority(Authority authority) {
        return authorityRepository.findByName(authority.getName()).orElseGet(
                () -> authorityRepository.save(
                        Authority.builder()
                                .name(authority.getName())
                                .build()
                ));
    }

    private Role getOrCreateRole(RoleRequestDto role) {
        return roleRepository.findByName(role.name()).orElseGet(
                () -> roleRepository.save(
                        Role.builder()
                                .name(role.name())
                                .authorities(getAuthorities(role.authorities()))
                                .build()
                )
        );
    }


    public RoleResponseDto findRoleById(Long id) {

        Role role = roleRepository.findById(id).orElseThrow(
                () -> new RoleNotFoundException(
                        "Role not found",
                        HttpStatus.NOT_FOUND.value(),
                        String.format("Role id %d not found.", id)
                )
        );

        return mapper.toRoleResponse(role);
    }

    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll()
                .stream().map(mapper::toRoleResponse)
                .collect(Collectors.toList());
    }
}
