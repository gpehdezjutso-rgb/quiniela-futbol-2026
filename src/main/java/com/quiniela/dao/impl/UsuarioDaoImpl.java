package com.quiniela.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.quiniela.dao.UsuarioDao;
import com.quiniela.pojo.Usuario;

import java.util.List;

@Repository
@Transactional // Hibernate requiere transacciones activas para operar
public class UsuarioDaoImpl implements UsuarioDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void guardar(Usuario usuario) {
        sessionFactory.getCurrentSession().save(usuario);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return sessionFactory.getCurrentSession().get(Usuario.class, id);
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        String hql = "FROM Usuario WHERE correoElectronico = :correo";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Usuario.class)
                .setParameter("correo", correo)
                .uniqueResult();
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Usuario", Usuario.class)
                .getResultList();
    }

    @Override
    public void actualizar(Usuario usuario) {
        sessionFactory.getCurrentSession().update(usuario);
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = buscarPorId(id);
        if (usuario != null) {
            sessionFactory.getCurrentSession().delete(usuario);
        }
    }
}
