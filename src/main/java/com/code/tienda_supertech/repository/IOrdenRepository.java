package com.code.tienda_supertech.repository;

import com.code.tienda_supertech.model.Orden;
import com.code.tienda_supertech.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Integer> {
    List<Orden> findByUsuario(Usuario usuario);
}
