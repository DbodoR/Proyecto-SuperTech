package com.code.tienda_supertech.controllers;

import com.code.tienda_supertech.model.Orden;
import com.code.tienda_supertech.model.Usuario;
import com.code.tienda_supertech.services.IOrdenService;
import com.code.tienda_supertech.services.IUsuarioService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IOrdenService ordenService;

    @GetMapping("/registro")
    public String registro(){

        return "usuario/registro";
    }

    @PostMapping("/save")
    public String save(Usuario usuario){
        logger.info("Usuario registro: {}", usuario);
        usuario.setType("USER");
        usuarioService.save(usuario);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "usuario/login";
    }

    @PostMapping("/acceder")
    public String acceder(Usuario usuario, HttpSession session){
        logger.info("Usuario accede: {}", usuario);

        Optional<Usuario> user = usuarioService.findByEmail(usuario.getEmail());

        if (user.isPresent()){
            session.setAttribute("idusuario", user.get().getId());
            if (user.get().getType().equals("ADMIN")){
                return "redirect:/admin";
            }
        }else {
            logger.info("Usuario no encontrado");
        }

        return "redirect:/";
    }

    @GetMapping("/compras")
    public String obtenerCompra(Model model, HttpSession session){
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        List<Orden> ordenes = ordenService.findByUsuario(usuario);

        model.addAttribute("ordenes", ordenes);

        return "usuario/compras";
    }

}
