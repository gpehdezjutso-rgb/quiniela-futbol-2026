package com.quiniela.dao;

import java.util.List;

import com.quiniela.pojo.Partido;

public interface PartidoDao {
    void guardar(Partido partido);
    List<Partido> obtenerTodos();
    Partido buscarPorId(Long id);
    void actualizar(Partido partido);
    void eliminar(Long id);
    List<Partido> obtenerPorFase(Long faseId);
}
