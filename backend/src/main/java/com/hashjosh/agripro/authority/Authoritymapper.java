package com.hashjosh.agripro.authority;

import com.hashjosh.agripro.authority.dto.AuthorityDto;
import com.hashjosh.agripro.authority.dto.AuthorityResponseDto;
import com.hashjosh.agripro.role.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Authoritymapper {
    public AuthorityResponseDto toAuthorityResponseDto(Authority authority) {
        return new AuthorityResponseDto(
                authority.getId(), authority.getName()
        );
    }

    public List<Authority> toAuthorityList(List<AuthorityDto> authorities) {
        return authorities.stream().map(
            authority -> Authority.builder()
                    .name(authority.name())
                    .build()
        ).collect(Collectors.toList());
    }
}
