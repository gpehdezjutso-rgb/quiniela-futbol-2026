package com.quiniela.dao.impl;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.quiniela.dao.PartidoDao;
import com.quiniela.pojo.Partido;

import java.util.List;

@Repository
@Transactional
public class PartidoDaoImpl implements PartidoDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void guardar(Partido partido) {
        //sessionFactory.getCurrentSession().save(partido);
        
        System.out.println("ANTES: " + partido.getEquipoVisitante());
        System.out.println("ANTES: " + partido.getEquipoLocal());
        System.out.println("ANTES: " + partido.getId());
        System.out.println("ANTES: Fecha recibida: " + partido.getFechaPartido());

        sessionFactory.getCurrentSession().save(partido);

        System.out.println("DESPUES: " + partido.getEquipoVisitante());
        System.out.println("DESPUES: " + partido.getEquipoLocal());
        System.out.println("DESPUES: Fecha recibida: " + partido.getFechaPartido());
    }

    @Override
    public List<Partido> obtenerTodos() {     
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT DISTINCT p FROM Partido p LEFT JOIN FETCH p.fase ORDER BY p.fechaPartido ASC", Partido.class)
                .getResultList();
    }

    @Override
    public Partido buscarPorId(Long id) {
        return sessionFactory.getCurrentSession().get(Partido.class, id);
    }

    @Override
    public void actualizar(Partido partido) {
        sessionFactory.getCurrentSession().update(partido);
    }

    @Override
    public void eliminar(Long id) {
        Partido partido = buscarPorId(id);
        if (partido != null) {
            sessionFactory.getCurrentSession().delete(partido);
        }
    }
    
    @Override
    public List<Partido> obtenerPorFase(Long faseId) {

        String hql = "FROM Partido p " +
                     "LEFT JOIN FETCH p.fase " +
                     "WHERE p.fase.id = :faseId " +
                     "ORDER BY p.fechaPartido";

        return sessionFactory.getCurrentSession()
                .createQuery(hql, Partido.class)
                .setParameter("faseId", faseId)
                .list();
    }
    
    
}
