package com.hashjosh.agripro.user.controller;

import com.hashjosh.agripro.config.CustomUserDetails;
import com.hashjosh.agripro.user.dto.LoginRequestDTO;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.authority.AuthorityRepository;
import com.hashjosh.agripro.user.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final AuthorityRepository   authorityRepository;


    public AuthController(AuthService authService,
                          AuthorityRepository authorityRepository) {
        this.authService = authService;
        this.authorityRepository = authorityRepository;
    }

    @PostMapping("/web/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequest,
                                   HttpServletRequest request, HttpServletResponse response) {
       try{
           AuthService.LoginResult result = authService.login(
                   loginRequest.username(),
                   loginRequest.password(),
                   loginRequest.rememberMe()
           );

           Cookie jwtCookie = new Cookie("jwt", result.jwt());
           jwtCookie.setHttpOnly(true);
           jwtCookie.setSecure(false);
           jwtCookie.setPath("/");
           jwtCookie.setMaxAge(result.expiry());
           jwtCookie.setAttribute("SameSite", "Lax");
           response.addCookie(jwtCookie);

           return ResponseEntity.ok(Map.of("message", "Login successful"));

       }catch (IllegalStateException e){
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
       }
    }

    @PostMapping("/mobile/login")
    public ResponseEntity<?> loginMobile(@RequestBody @Valid LoginRequestDTO loginRequest,
                                         HttpServletRequest request, HttpServletResponse response) {
        try{
            AuthService.LoginResult result = authService.login(
                    loginRequest.username(),
                    loginRequest.password(),
                    loginRequest.rememberMe()
            );

            return ResponseEntity.ok(Map.of("message", "Login successful", "jwt", result.jwt()));
        }catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/token")
    public ResponseEntity<?> getToken(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || auth.getPrincipal() == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid token"));
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();

        String jwt = authService.generateToken(user);

        return ResponseEntity.ok(Map.of("token", jwt));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String userAgent = request.getHeader("User-Agent");
        boolean isMobile = userAgent != null && userAgent.toLowerCase().contains("android");

        if (isMobile) {
            return ResponseEntity.ok(Map.of("message", "Logged out successfully (mobile)"));
        } else {
            Cookie jwtCookie = new Cookie("jwt", "");
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(0);
            jwtCookie.setAttribute("SameSite", "Lax");
            response.addCookie(jwtCookie);
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        }
    }

    @GetMapping("/auth/user")
    public ResponseEntity<?> user(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null){
            return ResponseEntity.status(404).body(Map.of("message","Not Authenticated"));
        }

        String username = authentication.getName();

        return ResponseEntity.ok(authService.authenticatedUser(username));
    }
}
