package com.quiniela.dao;


import java.util.List;

import com.quiniela.pojo.Usuario;

public interface UsuarioDao {
    void guardar(Usuario usuario);
    Usuario buscarPorId(Long id);
    Usuario buscarPorCorreo(String correo);
    List<Usuario> obtenerTodos();
    void actualizar(Usuario usuario);
    void eliminar(Long id);
}
