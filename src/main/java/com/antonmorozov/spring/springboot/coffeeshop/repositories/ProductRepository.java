package com.antonmorozov.spring.springboot.coffeeshop.repositories;

import com.antonmorozov.spring.springboot.coffeeshop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitle(String title);
}
