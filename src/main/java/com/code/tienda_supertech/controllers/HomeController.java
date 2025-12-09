package com.code.tienda_supertech.controllers;

import com.code.tienda_supertech.services.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ProductoService productoService;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "usuario/home";
    }

    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id){
        logger.info("Id producto enviado como parámetro {}",id);
        return "usuario/productohome";
    }
}
