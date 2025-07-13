package com.hashjosh.agripro.authority;

import com.hashjosh.agripro.authority.dto.AuthorityResponseDto;
import com.hashjosh.agripro.role.Role;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class Authoritymapper {
    public AuthorityResponseDto toAuthorityResponseDto(Authority authority) {
        return new AuthorityResponseDto(
                authority.getName(), authority.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
    }
}
