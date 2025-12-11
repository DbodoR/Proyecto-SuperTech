package com.code.tienda_supertech.controllers;

import com.code.tienda_supertech.model.DetalleOrden;
import com.code.tienda_supertech.model.Orden;
import com.code.tienda_supertech.model.Producto;
import com.code.tienda_supertech.model.Usuario;
import com.code.tienda_supertech.services.IDetalleOrdenService;
import com.code.tienda_supertech.services.IOrdenService;
import com.code.tienda_supertech.services.IProductoService;
import com.code.tienda_supertech.services.IUsuarioService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private IProductoService productoService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IOrdenService ordenService;

    @Autowired
    private IDetalleOrdenService detalleOrdenService;

    private List<DetalleOrden> detalleOrdenes = new ArrayList<DetalleOrden>();

    private Orden orden = new Orden();

    @GetMapping("")
    public String home(Model model, HttpSession session) {

        logger.info("Sesión del usuario: {}", session.getAttribute("idusuario"));
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        return "usuario/home";
    }

    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id, Model model, HttpSession session) {
        logger.info("Id producto enviado como parámetro {}",id);
        Producto producto = new Producto();
        Optional<Producto> productoOptional = productoService.get(id);
        producto = productoOptional.get();

        model.addAttribute("producto", producto);
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        return "usuario/productohome";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model, HttpSession session) {
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

        Integer idProducto = producto.getId();
        boolean productoExiste = detalleOrdenes.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
        if (!productoExiste) {
            detalleOrdenes.add(detalleOrden);
        }

        sumaTotal = detalleOrdenes.stream().mapToDouble(dt -> dt.getTotal()).sum();

        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalleOrdenes);
        model.addAttribute("orden", orden);
        model.addAttribute("sesion", session.getAttribute("idusuario"));

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

    @GetMapping("/getCart")
    public String getCart(Model model, HttpSession session) {
        model.addAttribute("cart", detalleOrdenes);
        model.addAttribute("orden", orden);

        model.addAttribute("sesion", session.getAttribute("idusuario"));

        return "usuario/carrito";
    }

    @GetMapping("/order")
    public String order(Model model, HttpSession session) {
        Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

        model.addAttribute("cart", detalleOrdenes);
        model.addAttribute("orden", orden);
        model.addAttribute("usuario", usuario);

        return "usuario/resumenorden";
    }

    @GetMapping("/saveOrder")
    public String saveOrder(Model model, HttpSession session) {
        Date fechaCreacion = new Date();
        orden.setFechaCreacion(fechaCreacion);
        orden.setNumero(ordenService.generarNumeroOrden());

        Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

        orden.setUsuario(usuario);
        ordenService.save(orden);

        for (DetalleOrden dt : detalleOrdenes) {
            dt.setOrden(orden);

            Integer idProducto = dt.getProducto().getId();
            Optional<Producto> prodOpt = productoService.get(idProducto);
            if (prodOpt.isPresent()) {
                Producto producto = prodOpt.get();

                int stockActual = producto.getCantidad();
                if (stockActual >= dt.getCantidad()) {
                    producto.setCantidad((int) (stockActual - dt.getCantidad()));
                    productoService.save(producto);
                    detalleOrdenService.save(dt);
                } else {
                    // marcar rollback y retornar al carrito con mensaje
                    org.springframework.transaction.interceptor.TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    model.addAttribute("error", "No hay suficiente stock para: " + producto.getNombre());
                    model.addAttribute("cart", detalleOrdenes);
                    model.addAttribute("orden", orden);
                    model.addAttribute("usuario", usuario);
                    return "usuario/carrito";
                }
            } else {
                org.springframework.transaction.interceptor.TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                model.addAttribute("error", "Producto no encontrado.");
                model.addAttribute("cart", detalleOrdenes);
                model.addAttribute("orden", orden);
                model.addAttribute("usuario", usuario);
                return "usuario/carrito";
            }
        }

        orden = new Orden();
        detalleOrdenes.clear();

        return "redirect:/";
    }

    @PostMapping("/search")
    public String searchProducto(@RequestParam String t, Model model){
        logger.info("nombre del producto: {}", t);
        List<Producto> productos = productoService.findAll().stream().filter(p -> p.getNombre().contains(t)).collect(Collectors.toList());
        model.addAttribute("productos", productos);

        return "usuario/home";
    }
}
