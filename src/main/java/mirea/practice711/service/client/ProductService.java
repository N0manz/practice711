package mirea.practice711.service.client;

import lombok.RequiredArgsConstructor;
import mirea.practice711.dao.entity.Product;
import mirea.practice711.dao.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(Product product) {
        product.setId(UUID.randomUUID());
        return productRepository.save(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product update(UUID id, Product updated) {
        Product product = getById(id);
        product.setTitle(updated.getTitle());
        product.setCategory(updated.getCategory());
        product.setDescription(updated.getDescription());
        product.setPrice(updated.getPrice());
        return productRepository.save(product);
    }

    public void delete(UUID id) {
        productRepository.deleteById(id);
    }
}