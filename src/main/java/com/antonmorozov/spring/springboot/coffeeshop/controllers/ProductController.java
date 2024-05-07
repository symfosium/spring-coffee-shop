package com.antonmorozov.spring.springboot.coffeeshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.antonmorozov.spring.springboot.coffeeshop.models.Image;
import com.antonmorozov.spring.springboot.coffeeshop.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.antonmorozov.spring.springboot.coffeeshop.models.Product;
import com.antonmorozov.spring.springboot.coffeeshop.repositories.ProductRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;


    @GetMapping("/new")
    public String showProductForm(Model model) {
        logger.info("Received request to show product form");
        model.addAttribute("product", new Product());
        model.addAttribute("title", ""); // добавляем пустые значения атрибутов модели
        model.addAttribute("description", "");
        model.addAttribute("price", 0);
        model.addAttribute("city", "");
        model.addAttribute("seller", "");
        return "product_form";
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable("id") Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + id));
        model.addAttribute("product", product);
        return "product_details"; // Создай соответствующее представление для отображения информации о товаре
    }

    @PostMapping("/save")
    public String createProduct(@ModelAttribute("product") Product product,
                                @RequestParam("files") List<MultipartFile> files) {
        try {
            // Присваиваем значения полям товара перед сохранением
            Product savedProduct = productRepository.save(product);

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // Сохраняем изображение в базу данных
                    Image image = new Image();
                    image.setName(file.getOriginalFilename());
                    image.setOriginalFileName(file.getOriginalFilename());
                    image.setSize(file.getSize());
                    image.setContentType(file.getContentType());
                    image.setBytes(file.getBytes());
                    image.setPreviewImage(true); // Устанавливаем, что это изображение предпросмотра

                    // Связываем изображение с сохраненным продуктом
                    image.setProduct(savedProduct);

                    // Сохраняем изображение в репозиторий
                    imageRepository.save(image);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибки
            // Можно добавить логирование или предупреждение для пользователя
        }
        return "redirect:/products"; // Перенаправляем на страницу со списком товаров
    }

    @GetMapping("")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "product_list"; // Представление для отображения списка продуктов
    }
}