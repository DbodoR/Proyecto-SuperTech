package com.code.tienda_supertech.services;

import com.code.tienda_supertech.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    Optional<Usuario> findById(Integer id);
    Usuario save(Usuario usuario);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findAll();
}
