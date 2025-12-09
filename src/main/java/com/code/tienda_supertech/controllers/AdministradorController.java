package com.code.tienda_supertech.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.code.tienda_supertech.model.Producto;
import com.code.tienda_supertech.services.IProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

    @Autowired
    private IProductoService productoService;

    @GetMapping("")
    public String home(Model model) {

        List<Producto> productos = productoService.findAll();
        model.addAttribute("productos", productos);

        return "admin/home";
    }
}
