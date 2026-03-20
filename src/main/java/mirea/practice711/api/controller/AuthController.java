package mirea.practice711.api.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public String login(
            @ModelAttribute AuthRequest request,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = authService.authenticate(request);

        addCookie(response, "accessToken", authResponse.getAccessToken(), 60 * 60);
        addCookie(response, "refreshToken", authResponse.getRefreshToken(), 60 * 60 * 24);

        return "redirect:/products";
    }

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public String register(
            @ModelAttribute User user,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = authService.register(user);

        addCookie(response, "accessToken", authResponse.getAccessToken(), 60 * 60);
        addCookie(response, "refreshToken", authResponse.getRefreshToken(), 60 * 60 * 24);

        return "redirect:/products";
    }

    // =========================
    // LOGOUT
    // =========================
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {

        deleteCookie(response, "accessToken");
        deleteCookie(response, "refreshToken");

        return "redirect:/login";
    }

    // =========================
    // ME (если нужно для UI)
    // =========================
    @GetMapping("/me")
    @ResponseBody
    public UserDto me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserDetails userDetails)) {
            throw new RuntimeException("Not authenticated");
        }

        if (userDetails instanceof CustomUserDetails custom) {
            User user = custom.getUser();
            return new UserDto(
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName()
            );
        }

        return new UserDto(null, userDetails.getUsername(), null, null);
    }

    // =========================
    // HELPERS
    // =========================
    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    private void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
