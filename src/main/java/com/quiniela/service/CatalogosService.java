package com.quiniela.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.quiniela.pojo.Estadios;
import com.quiniela.pojo.Fase;
import com.quiniela.pojo.Pais;

@Service
public interface CatalogosService {
    
	void registrarFase(Fase fase);
    
    void registrarPais(Pais pais);
    
    List<Fase> listarFases();
    
    List<Pais> listarPaises();
    
    List<Pais> obtenerActivosPais();

    Fase obtenerFasePorId(Long id);
    
    Pais obtenerPaisPorId(Long id);

    void eliminarFase(Long id);
    
    void eliminarPais(Long id);  
    
    Fase obtenerFaseActiva();
    
    List<Estadios> obtenerEstadiosActivos();
    
    List<Fase> obtenerFasesActivas();
}