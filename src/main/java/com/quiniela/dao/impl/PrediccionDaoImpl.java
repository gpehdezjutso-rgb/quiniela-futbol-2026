package com.quiniela.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.quiniela.dao.PrediccionDao;
import com.quiniela.pojo.Prediccion;

import java.util.List;

@Repository
@Transactional
public class PrediccionDaoImpl implements PrediccionDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void guardar(Prediccion prediccion) {        
        sessionFactory.getCurrentSession().saveOrUpdate(prediccion);
        sessionFactory.getCurrentSession().flush(); 
    }

    @Override
    public List<Prediccion> buscarPorUsuario(Long usuarioId) {
        String hql = "FROM Prediccion WHERE usuario.id = :usuarioId";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Prediccion.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();
    }

    @Override
    public Prediccion buscarPorUsuarioYPartido(Long usuarioId, Long partidoId) {
        String hql = "FROM Prediccion WHERE usuario.id = :usuarioId AND partido.id = :partidoId";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Prediccion.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("partidoId", partidoId)
                .uniqueResult();
    }

    @Override
    public List<Prediccion> obtenerTodas() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Prediccion", Prediccion.class)
                .getResultList();
    }
}
