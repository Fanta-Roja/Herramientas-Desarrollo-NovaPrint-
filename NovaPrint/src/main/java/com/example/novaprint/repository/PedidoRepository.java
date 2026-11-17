package com.example.novaprint.repository;

import com.example.novaprint.entity.Pedido;
import com.example.novaprint.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioOrderByFechaDesc(Usuario usuario);
    List<Pedido> findAllByOrderByFechaDesc();
}
