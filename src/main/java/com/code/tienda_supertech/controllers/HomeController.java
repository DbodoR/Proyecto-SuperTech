package com.code.tienda_supertech.controllers;

import com.code.tienda_supertech.model.DetalleOrden;
import com.code.tienda_supertech.model.Orden;
import com.code.tienda_supertech.model.Producto;
import com.code.tienda_supertech.services.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ProductoService productoService;

    private List<DetalleOrden> detalleOrdenes = new ArrayList<DetalleOrden>();

    private Orden orden = new Orden();

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "usuario/home";
    }

    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id, Model model) {
        logger.info("Id producto enviado como parámetro {}",id);
        Producto producto = new Producto();
        Optional<Producto> productoOptional = productoService.get(id);
        producto = productoOptional.get();

        model.addAttribute("producto", producto);

        return "usuario/productohome";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;

        Optional<Producto> productoOptional = productoService.get(id);
        logger.info("Producto añadido: {}", productoOptional.get());
        logger.info("Cantidad: {}", cantidad);
        producto = productoOptional.get();

        detalleOrden.setCantidad(cantidad);
        detalleOrden.setPrecio(producto.getPrecio());
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio() * cantidad);
        detalleOrden.setProducto(producto);

        detalleOrdenes.add(detalleOrden);

        sumaTotal = detalleOrdenes.stream().mapToDouble(dt -> dt.getTotal()).sum();

        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalleOrdenes);
        model.addAttribute("orden", orden);

        return "usuario/carrito";
    }

    @GetMapping("/detele/cart/{id}")
    public String deleteProducto(@PathVariable Integer id, Model model) {
        List<DetalleOrden> ordenesNuevas = new ArrayList<DetalleOrden>();

        for (DetalleOrden detalleOrden : detalleOrdenes) {
            if (detalleOrden.getProducto().getId() != id) {
                ordenesNuevas.add(detalleOrden);
            }
        }

        detalleOrdenes = ordenesNuevas;

        double sumaTotal = 0;
        sumaTotal = detalleOrdenes.stream().mapToDouble(dt -> dt.getTotal()).sum();


        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalleOrdenes);
        model.addAttribute("orden", orden);

        return "usuario/carrito";
    }
}
