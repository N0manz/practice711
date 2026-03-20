package mirea.practice711.api.controller;

import mirea.practice711.dao.entity.CustomUserDetails;
import mirea.practice711.dao.entity.User;
import mirea.practice711.service.client.ClientService;
import mirea.practice711.service.client.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final ClientService userService;
    private final ProductService productService;


    public PageController(ClientService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    @GetMapping("/me")
    public String me(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        model.addAttribute("user", user);

        return "me";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model, Authentication authentication) {
        System.out.println("getAllUSers "+ authentication.getAuthorities());
        model.addAttribute("users", userService.getAll());
        return "users";
    }

    @GetMapping("/products")
    public String getAll(Model model, Authentication authentication) {
        System.out.println("getAll "+ authentication);
        model.addAttribute("products", productService.getAll());
        return "products/list";
    }
}
