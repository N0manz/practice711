package mirea.practice711.api.controller;

import lombok.RequiredArgsConstructor;
import mirea.practice711.dao.entity.User;
import mirea.practice711.service.client.ClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
class AuthController {

    private final ClientService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody Map<String, String> request) {
        return userService.login(request.get("email"), request.get("password"));
    }
}
