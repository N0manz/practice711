package mirea.practice711.api.controller;

import lombok.RequiredArgsConstructor;
import mirea.practice711.api.dto.AuthRequest;
import mirea.practice711.api.dto.AuthResponse;
import mirea.practice711.api.dto.LoginRequest;
import mirea.practice711.dao.entity.CustomUserDetails;
import mirea.practice711.dao.entity.User;
import mirea.practice711.security.jwt.JwtService;
import mirea.practice711.service.auth.AuthService;
import mirea.practice711.service.client.ClientService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }

    @GetMapping("/me")
    public User me(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();  // берём внутренний User
    }
}
