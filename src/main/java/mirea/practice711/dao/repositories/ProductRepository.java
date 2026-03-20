package mirea.practice711.dao.repositories;

import mirea.practice711.dao.entity.Product;
import mirea.practice711.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
}
