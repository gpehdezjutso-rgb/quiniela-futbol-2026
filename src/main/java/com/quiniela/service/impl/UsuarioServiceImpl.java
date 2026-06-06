package com.quiniela.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiniela.dao.UsuarioDao;
import com.quiniela.pojo.Partido;
import com.quiniela.pojo.Prediccion;
import com.quiniela.pojo.Usuario;
import com.quiniela.service.UsuarioService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public boolean registrarUsuario(Usuario usuario) {
        if (usuario.getPassword() == null || usuario.getPassword().trim().length() < 6) {
            return false;
        }
        Usuario usuarioExistente = usuarioDao.buscarPorCorreo(usuario.getCorreoElectronico());
        if (usuarioExistente != null) {
            return false;
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setPuntosTotales(0);
        usuarioDao.guardar(usuario);
        return true;
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioDao.buscarPorId(id);
    }

    @Override
    public List<Usuario> obtenerTablaPosiciones() {
        List<Usuario> todos = usuarioDao.obtenerTodos();
        if (todos == null) return new ArrayList<>();
        return todos.stream()
                .sorted((u1, u2) -> u2.getPuntosTotales().compareTo(u1.getPuntosTotales()))
                .collect(Collectors.toList());
    }

    @Override
    public void actualizarPerfil(Usuario usuario) {
        // Si llega contraseña nueva (no vacía y sin prefijo BCrypt), la hasheamos
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            if (!usuario.getPassword().startsWith("$2a$")) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        } else {
            // Campo vacío: recuperar el hash actual de BD para no perderlo
            Usuario uActual = usuarioDao.buscarPorId(usuario.getId());
            if (uActual != null) {
                usuario.setPassword(uActual.getPassword());
            }
        }
        usuarioDao.actualizar(usuario);
    }

    @Override
    public Usuario autenticar(String correo, String password) {
        if (correo == null || password == null) {
            return null;
        }
        Usuario usuario = usuarioDao.buscarPorCorreo(correo.trim());
        if (usuario != null && passwordEncoder.matches(password.trim(), usuario.getPassword())) {
            return usuario;
        }
        return null;
    }

    @Override
    public void procesarPuntosGlobales(List<Prediccion> todasLasPredicciones) {
        List<Usuario> todosLosUsuarios = usuarioDao.obtenerTodos();
        for (Usuario u : todosLosUsuarios) {
            u.setPuntosTotales(0);
        }
        for (Prediccion p : todasLasPredicciones) {
            Partido partido = p.getPartido();
            if (partido.getGolesLocal() != null && partido.getGolesVisitante() != null) {
                int realL = partido.getGolesLocal();
                int realV = partido.getGolesVisitante();
                int predL = p.getGolesLocalPrediccion();
                int predV = p.getGolesVisitantePrediccion();

                int puntosObtenidos = 0;
                if (realL == predL && realV == predV) {
                    puntosObtenidos = 3;
                } else if ((realL > realV && predL > predV) ||
                           (realL < realV && predL < predV) ||
                           (realL == realV && predL == predV)) {
                    puntosObtenidos = 1;
                }
                p.setPuntosGanados(puntosObtenidos);

                Usuario jugador = p.getUsuario();
                if (jugador != null) {
                    jugador.setPuntosTotales(jugador.getPuntosTotales() + puntosObtenidos);
                    usuarioDao.actualizar(jugador);
                }
            }
        }
    }

    @Override
    public List<Usuario> listarTodosLosUsuarios() {
        return usuarioDao.obtenerTodos();
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioDao.eliminar(id);
    }
}
