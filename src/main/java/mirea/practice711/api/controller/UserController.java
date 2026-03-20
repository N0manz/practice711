package mirea.practice711.api.controller;

import lombok.RequiredArgsConstructor;
import mirea.practice711.service.client.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final ClientService userService;

    public UserController(ClientService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable UUID id, Model model) {
        model.addAttribute("user", userService.getById(id));
        return "users/details";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
