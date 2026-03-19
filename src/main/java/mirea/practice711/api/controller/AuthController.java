package mirea.practice711.api.controller;

import lombok.RequiredArgsConstructor;
import mirea.practice711.api.dto.AuthRequest;
import mirea.practice711.api.dto.AuthResponse;
import mirea.practice711.api.dto.LoginRequest;
import mirea.practice711.api.dto.UserDto;
import mirea.practice711.dao.entity.CustomUserDetails;
import mirea.practice711.dao.entity.User;
import mirea.practice711.security.jwt.JwtService;
import mirea.practice711.service.auth.AuthService;
import mirea.practice711.service.client.ClientService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestHeader("Authorization") String authHeader) {
        String refreshToken = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        return authService.refresh(refreshToken);
    }

    @GetMapping("/me")
    public UserDto me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserDetails userDetails)) {
            throw new RuntimeException("User not authenticated");
        }

        // Если используется CustomUserDetails
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            User user = customUserDetails.getUser();
            return new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName());
        }

        // Если обычный UserDetails
        return new UserDto(null, userDetails.getUsername(), null, null);
    }
}
