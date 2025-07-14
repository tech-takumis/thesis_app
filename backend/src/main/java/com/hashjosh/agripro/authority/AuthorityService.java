package com.hashjosh.agripro.authority;

import com.hashjosh.agripro.authority.dto.AuthorityResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final Authoritymapper authoritymapper;

    public AuthorityService(AuthorityRepository authorityRepository, Authoritymapper authoritymapper) {
        this.authorityRepository = authorityRepository;
        this.authoritymapper = authoritymapper;
    }

    public List<AuthorityResponseDto> findAll() {
        return authorityRepository.findAll().stream().map(
                authoritymapper::toAuthorityResponseDto
        ).collect(Collectors.toList());
    }


    public void delete(Long id) {
        authorityRepository.deleteById(id);
    }
}
