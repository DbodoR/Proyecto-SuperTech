package com.code.tienda_supertech.services;

import com.code.tienda_supertech.model.Orden;

import java.util.List;

public interface IOrdenService {
    Orden save(Orden orden);
    List<Orden> findAll();
    String generarNumeroOrden();
}
