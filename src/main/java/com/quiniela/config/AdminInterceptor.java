package com.quiniela.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import com.quiniela.pojo.Usuario;

/**
 * Interceptor que protege TODAS las rutas /admin/** de forma centralizada.
 * Reemplaza los if-guards manuales que había dispersos en cada método del controller.
 */
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null)
                ? (Usuario) session.getAttribute("usuarioLogueado")
                : null;

        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            response.sendRedirect(request.getContextPath() + "/?errorPermiso=true");
            return false;
        }
        return true;
    }
}
