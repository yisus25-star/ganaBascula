package com.ganabascula.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/admin/test")
    public String admin() {

        return "Ruta protegida ADMIN";
    }

    @GetMapping("/ganadero/test")
    public String ganadero() {

        return "Ruta protegida GANADERO";
    }
}