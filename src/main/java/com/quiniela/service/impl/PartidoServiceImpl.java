package com.quiniela.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiniela.dao.PartidoDao;
import com.quiniela.dao.PosicionesMundialDao;
import com.quiniela.pojo.EstadisticaEquipo;
import com.quiniela.pojo.Partido;
import com.quiniela.service.PartidoService;

@Service
public class PartidoServiceImpl implements PartidoService {

    @Autowired
    private PartidoDao partidoDao;
    
    @Autowired
    private PosicionesMundialDao posicionesMundialDao;

    @Override
    public void registrarPartido(Partido partido) {
        partidoDao.guardar(partido);
    }

    @Override
    public List<Partido> listarPartidos() {
        return partidoDao.obtenerTodos();
    }
    
    @Override
    public List<Partido> obtenerPorFase(Long faseId){
        return partidoDao.obtenerPorFase(faseId);
    }

    @Override
    public void actualizarResultado(Long id, Integer golesLocal, Integer golesVisitante) {
        Partido partido = partidoDao.buscarPorId(id);
        if (partido != null) {
            partido.setGolesLocal(golesLocal);
            partido.setGolesVisitante(golesVisitante);
            partidoDao.actualizar(partido);
        }
    }
    
    @Override
    public void eliminarResultado(Long id) {
        Partido partido = partidoDao.buscarPorId(id);
        if (partido != null) {    
        	partido.setGolesLocal(null);
        	partido.setGolesVisitante(null);
            partidoDao.actualizar(partido);
        }
    }
    
    @Override
    public Partido obtenerPartidoPorId(Long id) {
        return partidoDao.buscarPorId(id);
    }

    @Override
    public void eliminarPartido(Long id) {
        partidoDao.eliminar(id);
    }
    
    @Override
    @Transactional
    public List<EstadisticaEquipo> obtenerTablaGeneralMundial() {
    	System.out.println("PartidoServiceImpl -> 1");
    	
        // 1. Si el administrador ya guardó datos en MySQL, los leemos directamente sin procesar código pesado
        //List<EstadisticaEquipo> tablaGuardada = posicionesMundialDao.obtenerTablaOrdenada();
        //if (tablaGuardada != null && !tablaGuardada.isEmpty()) {
        //    return tablaGuardada;
        //}
        
        System.out.println("PartidoServiceImpl -> 2");

        // 2. Si la tabla en la BD está vacía (primer arranque o recalculo), procesamos los partidos
        List<Partido> partidos = partidoDao.obtenerTodos();
        java.util.Map<String, EstadisticaEquipo> mapaEstadisticas = new java.util.HashMap<>();

        if (partidos == null || partidos.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        System.out.println("PartidoServiceImpl -> 3");

        for (Partido p : partidos) {
            if (p.getGolesLocal() != null && p.getGolesVisitante() != null) {
            	System.out.println("PartidoServiceImpl -> 4");
                String local = p.getEquipoLocal();
                String visitante = p.getEquipoVisitante();
                int gLocal = p.getGolesLocal();
                int gVisitante = p.getGolesVisitante();

                mapaEstadisticas.putIfAbsent(local, new EstadisticaEquipo(local));
                mapaEstadisticas.putIfAbsent(visitante, new EstadisticaEquipo(visitante));

                mapaEstadisticas.get(local).acumularPartido(gLocal, gVisitante);
                mapaEstadisticas.get(visitante).acumularPartido(gVisitante, gLocal);
            }
        }
        System.out.println("PartidoServiceImpl -> 5");
        // 3. PERSISTENCIA: Guardamos de forma física cada equipo procesado en MySQL
        for (EstadisticaEquipo est : mapaEstadisticas.values()) {
            posicionesMundialDao.guardarOActualizar(est);
        }
        System.out.println("PartidoServiceImpl -> 6");
        // 4. Retornamos la lista final ordenada
        return posicionesMundialDao.obtenerTablaOrdenada();
    }
   
 
}
