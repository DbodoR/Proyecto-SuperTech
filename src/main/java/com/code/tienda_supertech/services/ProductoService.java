package com.code.tienda_supertech.services;

import com.code.tienda_supertech.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    public Producto save(Producto producto);
    public Optional<Producto> get(int id);
    public void update(Producto producto);
    public void delete(int id);
    public List<Producto> findAll();
}
