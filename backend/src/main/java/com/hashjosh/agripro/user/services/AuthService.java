package com.hashjosh.agripro.user.services;

import com.hashjosh.agripro.config.CustomUserDetails;
import com.hashjosh.agripro.config.JwtUtil;
import com.hashjosh.agripro.user.dto.StaffResponseDto;
import com.hashjosh.agripro.user.mapper.AuthMapper;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, AuthMapper mapper, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public LoginResult login(String username, String password, boolean rememberMe) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username, password
                )
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();


        int expiry = rememberMe ? 60 * 60 * 24 *30 : -1;

        return new LoginResult(generateToken(user),expiry);
    }

    public String generateToken(User user) {
        List<String> authorities = user.getRoles().stream()
                .flatMap(role -> role.getAuthorities().stream().map(
                        authority -> authority.getName().toUpperCase().replace(" ", "_")
                ))
                .toList();
        List<String> roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName().toUpperCase().replace(" ", "_"))
                .toList();

        return jwtUtil.generateToken(user.getUsername(),roles, authorities);
    }
    public StaffResponseDto authenticatedUser(String username) {
        return userRepository.findByUsername(username)
                .map(mapper::toAuthenticatedDto)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }

    public record LoginResult(String jwt, int expiry) {

    }
}
