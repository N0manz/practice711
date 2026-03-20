package mirea.practice711.api.controller;

import lombok.RequiredArgsConstructor;
import mirea.practice711.dao.entity.Product;
import mirea.practice711.service.client.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable UUID id, Model model) {
        model.addAttribute("product", productService.getById(id));
        return "products/details";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/create";
    }

    @PostMapping
    public String create(@ModelAttribute Product product) {
        productService.create(product);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable UUID id, Model model) {
        model.addAttribute("product", productService.getById(id));
        return "products/edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable UUID id, @ModelAttribute Product product) {
        productService.update(id, product);
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id) {
        productService.delete(id);
        return "redirect:/products";
    }
}
