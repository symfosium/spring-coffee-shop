package com.antonmorozov.spring.springboot.coffeeshop.controllers;

import com.antonmorozov.spring.springboot.coffeeshop.models.Product;
import com.antonmorozov.spring.springboot.coffeeshop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.antonmorozov.spring.springboot.coffeeshop.models.Image;
import com.antonmorozov.spring.springboot.coffeeshop.repositories.ImageRepository;

import java.util.Optional;

@Controller
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/new")
    public String showImageForm(Model model) {
        model.addAttribute("image", new Image());
        model.addAttribute("products", productRepository.findAll());
        return "image_form"; // Представление для загрузки нового изображения
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<Image> imageOptional = imageRepository.findById(id);
        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(image.getContentType()));
            headers.setContentLength(image.getSize());
            return new ResponseEntity<>(image.getBytes(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/save")
    public String saveImage(@ModelAttribute("image") Image image, @RequestParam("productId") Long productId) {
        // Получение продукта по его идентификатору
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            // Установка продукта для изображения
            image.setProduct(product);
            // Сохранение изображения
            imageRepository.save(image);
        } else {
            // Обработка случая, если продукт не найден
            // Можно выбросить исключение или выполнить другую логику
        }
        return "redirect:/images";
    }

    @GetMapping("")
    public String listImages(Model model) {
        model.addAttribute("images", imageRepository.findAll());
        return "image_list"; // Представление для отображения списка изображений
    }
}