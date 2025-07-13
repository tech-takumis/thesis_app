package com.hashjosh.agripro.authority;

import com.hashjosh.agripro.authority.dto.AuthorityResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authorities")
public class AuthorityController {

    private final AuthorityService service;

    public AuthorityController(AuthorityService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AuthorityResponseDto>> getAuthorities() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthority(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
