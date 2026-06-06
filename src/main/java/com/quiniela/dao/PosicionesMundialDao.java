package com.quiniela.dao;


import java.util.List;

import com.quiniela.pojo.EstadisticaEquipo;

public interface PosicionesMundialDao {
    void guardarOActualizar(EstadisticaEquipo estadistica);
    void vaciarTabla();
    List<EstadisticaEquipo> obtenerTablaOrdenada();
}
