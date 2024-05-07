package com.antonmorozov.spring.springboot.coffeeshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHomePage() {
        return "welcome"; // Возвращаем имя представления без расширения
    }
}
