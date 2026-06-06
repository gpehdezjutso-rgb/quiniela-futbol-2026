package com.quiniela.dao.impl;


import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.quiniela.dao.CatalogosDao;
import com.quiniela.pojo.Estadios;
import com.quiniela.pojo.Fase;
import com.quiniela.pojo.Pais;

@Repository
@Transactional
public class CatalogosDaoImpl implements CatalogosDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void guardar(Fase fase) {
        sessionFactory.getCurrentSession().saveOrUpdate(fase);
    }
    
    @Override
    public void guardar(Pais pais) {
        sessionFactory.getCurrentSession().saveOrUpdate(pais);
    }

    @Override
    public List<Fase> obtenerTodosFase() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Fase", Fase.class)
                .getResultList();
    }
    
    @Override
    public List<Pais> obtenerTodosPais() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Pais", Pais.class)
                .getResultList();
    }
    
    @Override
    public List<Pais> obtenerActivosPais() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Pais where estado='Activo'", Pais.class)
                .getResultList();
    }

    @Override
    public Fase buscarFasePorId(Long id) {
        return sessionFactory.getCurrentSession().get(Fase.class, id);
    }
    
    @Override
    public Pais buscarPaisPorId(Long id) {
        return sessionFactory.getCurrentSession().get(Pais.class, id);
    }

    @Override
    public void actualizar(Fase fase) {
        sessionFactory.getCurrentSession().update(fase);
    }
    
    @Override
    public void actualizar(Pais pais) {
        sessionFactory.getCurrentSession().update(pais);
    }

    @Override
    public void eliminarFase(Long id) {
        Fase fase = buscarFasePorId(id);
        if (fase != null) {
            sessionFactory.getCurrentSession().delete(fase);
        }
    }
    
    @Override
    public void eliminarPais(Long id) {
        Pais pais = buscarPaisPorId(id);
        if (pais != null) {
            sessionFactory.getCurrentSession().delete(pais);
        }
    }
    
    @Override
    public Fase obtenerFaseActiva() {
        String hql = "FROM Fase f WHERE f.estado = 'Activa'";

        return sessionFactory.getCurrentSession()
                .createQuery(hql, Fase.class)
                .uniqueResult();
    }
    
    @Override
    public List<Fase> obtenerFasesActivas() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Fase f WHERE f.estado = 'Activa'", Fase.class)
                .getResultList();
    }
    
    @Override
    public List<Estadios> obtenerEstadiosActivos() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Estadios where estado='Activo'", Estadios.class)
                .getResultList();
    }

}
