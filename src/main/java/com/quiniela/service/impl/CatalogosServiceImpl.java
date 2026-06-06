package com.quiniela.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiniela.dao.CatalogosDao;
import com.quiniela.pojo.Estadios;
import com.quiniela.pojo.Fase;
import com.quiniela.pojo.Pais;
import com.quiniela.service.CatalogosService;

@Service
public class CatalogosServiceImpl implements CatalogosService {	
	
	@Autowired
	private CatalogosDao faseDao;
	 
    public void registrarFase(Fase fase) {
		 faseDao.guardar(fase);
    }
    
    public void registrarPais(Pais pais) {
 		 faseDao.guardar(pais);
     }
    
    public List<Fase> listarFases() {
        return faseDao.obtenerTodosFase();
    }
    
    public List<Fase> obtenerFasesActivas() {
    	return faseDao.obtenerFasesActivas();
    }
    
    public Fase obtenerFaseActiva() {
        return faseDao.obtenerFaseActiva();
    }
        
    public List<Pais> listarPaises() {
        return faseDao.obtenerTodosPais();
    }
    
    public List<Pais> obtenerActivosPais() {
        return faseDao.obtenerActivosPais();
    }  

    public Fase obtenerFasePorId(Long id) {
        return faseDao.buscarFasePorId(id);
    }
    
    public Pais obtenerPaisPorId(Long id) {
        return faseDao.buscarPaisPorId(id);
    }

    public void eliminarFase(Long id) {
    	faseDao.eliminarFase(id);
    }
    
    public void eliminarPais(Long id) {
    	faseDao.eliminarPais(id);
    }
    
    public List<Estadios> obtenerEstadiosActivos() {
        return faseDao.obtenerEstadiosActivos();
    }  
   
}