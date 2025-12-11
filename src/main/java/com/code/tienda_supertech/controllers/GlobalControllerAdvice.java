package com.code.tienda_supertech.controllers;

import com.code.tienda_supertech.model.Usuario;
import com.code.tienda_supertech.services.IUsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private IUsuarioService usuarioService;

    @ModelAttribute
    public void addUserName(Model model, HttpSession session) {
        Object idAttr = session.getAttribute("idusuario");
        if (idAttr != null) {
            try {
                Integer id = Integer.parseInt(idAttr.toString());
                Optional<Usuario> usuarioOpt = usuarioService.findById(id);
                if (usuarioOpt.isPresent()) {
                    String nombre = usuarioOpt.get().getNombre();
                    if (nombre != null && !nombre.isEmpty()) {
                        String primerNombre = nombre.split("\\s+")[0];
                        model.addAttribute("nombreUsuario", primerNombre);
                        return;
                    }
                }
            } catch (Exception e) {
                // ignorar y dejar nombreUsuario como null
            }
        }
        model.addAttribute("nombreUsuario", null);
    }
}
