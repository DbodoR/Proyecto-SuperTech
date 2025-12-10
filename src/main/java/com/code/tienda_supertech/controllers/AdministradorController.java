package com.code.tienda_supertech.controllers;

import com.code.tienda_supertech.model.Orden;
import com.code.tienda_supertech.services.IOrdenService;
import com.code.tienda_supertech.services.IUsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.code.tienda_supertech.model.Producto;
import com.code.tienda_supertech.services.IProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

    private Logger logger = LoggerFactory.getLogger(AdministradorController.class);

    @Autowired
    private IProductoService productoService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IOrdenService ordenService;

    @GetMapping("")
    public String home(Model model) {

        List<Producto> productos = productoService.findAll();
        model.addAttribute("productos", productos);

        return "admin/home";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model){
        model.addAttribute("usuarios", usuarioService.findAll());

        return "admin/usuarios";
    }

    @GetMapping("/ordenes")
    public String ordenes(Model model){
        model.addAttribute("ordenes", ordenService.findAll());

        return "admin/ordenes";
    }

    @GetMapping("/detalleorden/{id}")
    public String detalleOrden(Model model, @PathVariable Integer id){
        logger.info("detalle orden: {}", id);
        Orden orden = ordenService.findById(id).get();
        model.addAttribute("detalle", orden.getDetalle());

        return "admin/detalleorden";
    }
}
