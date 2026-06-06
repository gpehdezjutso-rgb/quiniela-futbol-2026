package com.quiniela.dao;

import java.util.List;

import com.quiniela.pojo.Prediccion;

public interface PrediccionDao {
    void guardar(Prediccion prediccion);
    List<Prediccion> buscarPorUsuario(Long usuarioId);
    Prediccion buscarPorUsuarioYPartido(Long usuarioId, Long partidoId);
    List<Prediccion> obtenerTodas();
}
