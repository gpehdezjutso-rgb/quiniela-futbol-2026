package com.quiniela.dao;

import java.util.List;

import com.quiniela.pojo.Estadios;
import com.quiniela.pojo.Fase;
import com.quiniela.pojo.Pais;


public interface CatalogosDao {
	 	void guardar(Fase fase);    	    
	    void actualizar(Fase fase);	    
		void guardar(Pais pais);
		List<Fase> obtenerTodosFase();
		List<Pais> obtenerTodosPais();
		Fase buscarFasePorId(Long id);
		Pais buscarPaisPorId(Long id);
		void actualizar(Pais pais);
		void eliminarFase(Long id);
		void eliminarPais(Long id);
		List<Pais> obtenerActivosPais();
		Fase obtenerFaseActiva();
		public List<Estadios> obtenerEstadiosActivos();
		List<Fase> obtenerFasesActivas();
}
