package com.quiniela.service;

import java.util.List;

import com.quiniela.pojo.Prediccion;

public interface PrediccionService {
    com.quiniela.pojo.ResultadoApuesta guardarApuesta(Long usuarioId, Long partidoId, Integer golesLocal, Integer golesVisitante);
    List<Prediccion> listarApuestasUsuario(Long usuarioId);
 // Devuelve un arreglo o mapa con las estadísticas [Aciertos Exactos, Aciertos Parciales]
    int[] obtenerEstadisticasUsuario(Long usuarioId);

}
