package com.hashjosh.agripro.user.controller;

import com.hashjosh.agripro.config.CustomUserDetails;
import com.hashjosh.agripro.config.JwtUtil;
import com.hashjosh.agripro.user.dto.LoginRequestDTO;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.user.repository.UserRepository;
import com.hashjosh.agripro.user.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UserRepository userRepository, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequest,
                                   HttpServletRequest request, HttpServletResponse response) {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            CustomUserDetails userDetails  = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();


            String jwt = jwtUtil.generateToken(user);
            boolean rememberMe = loginRequest.remember();  // From request JSON
            int expiry = rememberMe ? 60 * 60 * 24 * 30 : -1;

            String clientHeader = request.getHeader("X-Client");
            boolean isMobileApp = "mobile".equalsIgnoreCase(clientHeader);

            if (isMobileApp) {
                // Return JWT token in body for mobile app to store
                return ResponseEntity.ok(Map.of("token", jwt, "message", "Login successful"));
            } else {
                // Set JWT as HttpOnly cookie for browser-based web frontend
                Cookie jwtCookie = new Cookie("jwt", jwt);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setSecure(false); // Set to true in production with HTTPS
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(expiry);
                jwtCookie.setAttribute("SameSite", "Lax");
                response.addCookie(jwtCookie);
                return ResponseEntity.ok(Map.of("message", "Login successful"));
            }

    }

    @GetMapping("/token")
    public ResponseEntity<?> getToken(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Authentication failed"));
        }

        CustomUserDetails  userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        String jwt = jwtUtil.generateToken(user);


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

        return ResponseEntity.ok(userService.authenticatedUser(username));
    }
}
