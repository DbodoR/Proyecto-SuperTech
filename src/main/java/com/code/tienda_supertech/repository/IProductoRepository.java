package com.code.tienda_supertech.repository;

import com.code.tienda_supertech.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoRepository extends JpaRepository<Producto,Integer> {

}
