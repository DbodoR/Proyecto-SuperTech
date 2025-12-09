package com.code.tienda_supertech.services;

import com.code.tienda_supertech.model.Orden;
import com.code.tienda_supertech.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IOrdenService {
    Orden save(Orden orden);
    List<Orden> findAll();
    String generarNumeroOrden();
    List<Orden> findByUsuario(Usuario usuario);
    Optional<Orden> findById(Integer id);
}
