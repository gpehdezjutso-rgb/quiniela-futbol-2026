package com.quiniela.service;

import java.util.List;

import com.quiniela.pojo.Prediccion;
import com.quiniela.pojo.Usuario;

public interface UsuarioService {
    boolean registrarUsuario(Usuario usuario); // Aplica validaciones de registro
    Usuario obtenerUsuarioPorId(Long id);
    List<Usuario> obtenerTablaPosiciones();     // Devuelve los usuarios ordenados por puntos
    void actualizarPerfil(Usuario usuario);
    Usuario autenticar(String correo, String password); 
    void procesarPuntosGlobales(List<Prediccion> todasLasPredicciones);
    List<Usuario> listarTodosLosUsuarios();
    void eliminarUsuario(Long id);
}
