package com.quiniela.controllers;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.quiniela.pojo.EstadisticaEquipo;
import com.quiniela.pojo.Fase;
import com.quiniela.pojo.Pais;
import com.quiniela.pojo.Partido;
import com.quiniela.pojo.PartidoDTO;
import com.quiniela.pojo.Prediccion;
import com.quiniela.pojo.Usuario;
import com.quiniela.service.CatalogosService;
import com.quiniela.service.PartidoService;
import com.quiniela.service.PrediccionService;
import com.quiniela.service.UsuarioService;

@Controller
public class HomeController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PartidoService partidoService;
    
    @Autowired
    private CatalogosService catalogosService;
    
    @Autowired
    private PrediccionService prediccionService;
    
    @Autowired
    private com.quiniela.dao.PrediccionDao prediccionDao;
    
    @Autowired
    private com.quiniela.dao.PosicionesMundialDao posicionesMundialDao;
    
    @Value("#{'${mundial.fases.estatus}'.split(',')}")
    private List<String> listaFasesEstatus;
        
    @Value("#{'${mundial.grupos}'.split(',')}")
    private List<String> listaGrupos;
    
    @GetMapping("/")
    public String mostrarLogin() {
        return "login"; 
    }

    // 2. Procesar las credenciales del usuario   
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("correo") String correo, 
                                @RequestParam("password") String password, 
                                HttpSession session, // Inyectamos la sesión
                                Model model) {
        
        Usuario usuario = usuarioService.autenticar(correo, password);

        if (usuario != null) {
            // Guardamos el objeto usuario completo en la sesión del navegador
            session.setAttribute("usuarioLogueado", usuario);

            if ("ADMIN".equals(usuario.getRol())) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/dashboard";
            }
        }

        model.addAttribute("error", "Correo electrónico o contraseña incorrectos.");
        return "login";
    }


    // NUEVA RUTA PARA EL ADMINISTRADOR
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioLogueado");
        
        // Validamos que haya iniciado sesión Y que sea estrictamente un ADMINISTRADOR
        if (usuarioActual == null || !"ADMIN".equals(usuarioActual.getRol())) {
            return "redirect:/?errorPermiso=true";
        }
        
        model.addAttribute("nombreUsuario", usuarioActual.getNombre());

        // Métricas para el panel de administración
        java.util.List<com.quiniela.pojo.Partido> todosPartidos = partidoService.listarPartidos();
        if (todosPartidos == null) todosPartidos = new java.util.ArrayList<>();

        java.util.List<com.quiniela.pojo.Usuario> todosUsuarios = usuarioService.listarTodosLosUsuarios();
        if (todosUsuarios == null) todosUsuarios = new java.util.ArrayList<>();

        long partidosConResultado = todosPartidos.stream()
                .filter(p -> p.getGolesLocal() != null).count();
        long partidosPendientes = todosPartidos.size() - partidosConResultado;

        java.util.List<com.quiniela.pojo.Prediccion> todasPredicciones = prediccionDao.obtenerTodas();
        if (todasPredicciones == null) todasPredicciones = new java.util.ArrayList<>();

        model.addAttribute("totalUsuarios", todosUsuarios.size());
        model.addAttribute("totalPartidos", todosPartidos.size());
        model.addAttribute("partidosConResultado", partidosConResultado);
        model.addAttribute("partidosPendientes", partidosPendientes);
        model.addAttribute("totalPredicciones", todasPredicciones.size());

        return "admin-dashboard"; 
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioActual == null) {
            return "redirect:/?errorSesion=true";
        }
        
        // Recuperamos partidos y predicciones del usuario logueado de MySQL
        List<Partido> partidos = partidoService.listarPartidos();
        if (partidos == null) partidos = new java.util.ArrayList<>(); // ✅ FIX: proteger contra null antes del for-loop

        List<Prediccion> apuestas = prediccionService.listarApuestasUsuario(usuarioActual.getId());
        if (apuestas == null) apuestas = new java.util.ArrayList<>(); // ✅ FIX: proteger contra null antes de .stream()

        // Unificamos listados en el objeto PartidoDTO
        List<PartidoDTO> partidosConApuesta = new java.util.ArrayList<>();
        for (Partido p : partidos) {
            Prediccion apuestaEncontrada = apuestas.stream()
                .filter(a -> a.getPartido().getId().equals(p.getId()))
                .findFirst()
                .orElse(null);
                
            partidosConApuesta.add(new PartidoDTO(p, apuestaEncontrada));
        }
        
        List<EstadisticaEquipo> listaEstadisticas = partidoService.obtenerTablaGeneralMundial();
        if (listaEstadisticas == null) listaEstadisticas = new java.util.ArrayList<>();
        
        List<Usuario> listaJugadores = usuarioService.obtenerTablaPosiciones();
        if (listaJugadores == null) listaJugadores = new java.util.ArrayList<>();
        
        // Inyectamos las variables específicas del usuario
        model.addAttribute("partidosDTO", partidosConApuesta);
        model.addAttribute("usuarioActual", usuarioActual);
        model.addAttribute("apuestas", apuestas);

        // Calculamos las estadísticas personales
        int[] estadisticas = prediccionService.obtenerEstadisticasUsuario(usuarioActual.getId());
        model.addAttribute("aciertosExactos", estadisticas[0]);
        model.addAttribute("aciertosParciales", estadisticas[1]);
        model.addAttribute("titulo", "Quiniela Mundial 2026");        
        model.addAttribute("resultadosJugadores", listaJugadores);
        model.addAttribute("tablaEquipos", listaEstadisticas);        
        
        return "dashboard"; 
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute("usuario") Usuario usuario, Model model) {
    	usuario.setRol("USER");
    	
		 if (usuario.getPassword() == null || usuario.getPassword().trim().length() < 6) {
		        model.addAttribute("error", "La contraseña debe tener una longitud mínima de 6 caracteres.");
		    return "registro";
		 }
    	 
        boolean registroExitoso = usuarioService.registrarUsuario(usuario);

        if (!registroExitoso) {
            model.addAttribute("error", "El correo electrónico ya se encuentra registrado.");
            return "registro";
        }
        // Al registrarse con éxito, lo mandamos al login para que ingrese formalmente
        return "redirect:/?registroExitoso=true";
    }
    
    @GetMapping("/admin/partidos")
    public String gestionarPartidos(
    		@RequestParam(value = "editarId", required = false) Long editarId, 
    		 @RequestParam(required = false) Long faseId,
    		Model model
    		) {
        Partido partidoForm = new Partido();
              
        if (faseId == null) {

            Fase faseActiva = catalogosService.obtenerFaseActiva();

            if (faseActiva != null) {
                faseId = faseActiva.getId();
            }
        }
        
        // Si viene un ID, buscamos el partido para cargar sus datos en el formulario
        if (editarId != null) {
            Partido partidoExistente = partidoService.obtenerPartidoPorId(editarId);
            if (partidoExistente != null) {
                partidoForm = partidoExistente;
                // Pasamos la fecha formateada para que el input HTML datetime-local la entienda
                if (partidoExistente.getFechaPartido() != null) {
                    model.addAttribute("fechaEditar", partidoExistente.getFechaPartido().toString());
                }
            }
        }
        
        model.addAttribute("partido", partidoForm);
        model.addAttribute("partidos", partidoService.obtenerPorFase(faseId));
        model.addAttribute("estadios", catalogosService.obtenerEstadiosActivos());
        model.addAttribute("paises", catalogosService.obtenerActivosPais());       
        model.addAttribute("fases", catalogosService.listarFases());
        model.addAttribute("fasesActivas", catalogosService.obtenerFasesActivas()); 
        model.addAttribute("faseSeleccionada", faseId);
        model.addAttribute("grupos", listaGrupos);
        
        return "admin-partidos";
    }

    @PostMapping("/admin/partidos")
    public String guardarOActualizarPartido(@ModelAttribute("partido") Partido partido, 
                                            @RequestParam("fechaStr") String fechaStr) {
    	
    	System.out.println("guardarOActualizarPartido -> ");
    	System.out.println("partido -> " + partido.getEquipoLocal() + " - " + partido.getEquipoVisitante());
    	    	
        if (fechaStr != null && !fechaStr.isEmpty()) {
            partido.setFechaPartido(java.time.LocalDateTime.parse(fechaStr));
        }
        
        if (partido.getId() != null) {
            // Si el objeto ya tiene ID, es una edición en memoria
            partidoService.actualizarResultado(partido.getId(), partido.getGolesLocal(), partido.getGolesVisitante());
            // Actualizamos también campos de edición
            Partido p = partidoService.obtenerPartidoPorId(partido.getId());
            if (p != null) {
                p.setEquipoLocal(partido.getEquipoLocal());
                p.setEquipoVisitante(partido.getEquipoVisitante());
                p.setFase(partido.getFase());
                p.setFechaPartido(partido.getFechaPartido());
            }
        } else {
            // Si no tiene ID, es un registro nuevo
            partidoService.registrarPartido(partido);
        }    
        
        return "redirect:/admin/partidos";
    }
    
    @PostMapping("/admin/partidos/resultado")
    public String guardarResultado(@RequestParam("partidoId") Long partidoId,
                                   @RequestParam("golesLocal") Integer golesLocal,
                                   @RequestParam("golesVisitante") Integer golesVisitante) {
        
        // Invocamos al servicio para guardar los goles oficiales
        partidoService.actualizarResultado(partidoId, golesLocal, golesVisitante);
        
        return "redirect:/admin/partidos";
    }
    
    @PostMapping("/admin/partidos/resultado/eliminar")
    public String eliminarResultado(@RequestParam("partidoId") Long partidoId
                                   ) {        
        // Invocamos al servicio para guardar los goles oficiales
        partidoService.eliminarResultado(partidoId );
        
        return "redirect:/admin/partidos";
    }
    
    // POST: Eliminar partido — protegido contra acceso por URL directa (CSRF/GET)
    @PostMapping("/admin/partidos/eliminar")
    public String eliminarPartido(@RequestParam("id") Long id) {
        partidoService.eliminarPartido(id);
        return "redirect:/admin/partidos";
    }

    @PostMapping("/dashboard/apuesta")
    public String guardarApuesta(@RequestParam("partidoId") Long partidoId,
                                 @RequestParam("golesLocalPrediccion") Integer golesLocalPrediccion,
                                 @RequestParam("golesVisitantePrediccion") Integer golesVisitantePrediccion,
                                 HttpSession session) {
        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioActual == null) {
            return "redirect:/?errorSesion=true";
        }
        com.quiniela.pojo.ResultadoApuesta resultado = prediccionService.guardarApuesta(
                usuarioActual.getId(), partidoId, golesLocalPrediccion, golesVisitantePrediccion);
        switch (resultado) {
            case EXITO:
                return "redirect:/dashboard?apuestaOk=true";
            case PARTIDO_CON_RESULTADO:
                return "redirect:/dashboard?errorApuesta=resultado";
            case PARTIDO_EXPIRADO:
                return "redirect:/dashboard?errorApuesta=expirado";
            case DATOS_INVALIDOS:
            default:
                return "redirect:/dashboard?errorApuesta=invalido";
        }
    }

    
    @PostMapping("/admin/puntos/calcular")
    public String calcularPuntosGlobales() {
        // Obtenemos todas las apuestas hechas por los usuarios
    	posicionesMundialDao.vaciarTabla();
    	
    	java.util.List<com.quiniela.pojo.Prediccion> todas = prediccionDao.obtenerTodas();    
        
        // Ejecutamos el motor de puntos
        usuarioService.procesarPuntosGlobales(todas);
        
        // Redirigimos de vuelta al panel de administración principal con un mensaje de éxito
        return "redirect:/admin/dashboard?calculoExitoso=true";
    }
    
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        // Destruye por completo la sesión del navegador
        session.invalidate(); 
        return "redirect:/?logoutExitoso=true";
    }
    
 // 1. GET: Pantalla de gestión de usuarios con soporte para edición opcional
    @GetMapping("/admin/usuarios")
    public String gestionarUsuarios(@RequestParam(value = "editarId", required = false) Long editarId, 
                                   HttpSession session, Model model) {
        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioActual == null || !"ADMIN".equals(usuarioActual.getRol())) {
            return "redirect:/?errorPermiso=true";
        }

        Usuario usuarioForm = new Usuario();
        if (editarId != null) {
            Usuario usuarioExistente = usuarioService.obtenerUsuarioPorId(editarId);
            if (usuarioExistente != null) {
                usuarioForm = usuarioExistente;
            }
        }

        model.addAttribute("usuario", usuarioForm);
        // Enviamos la lista completa sin ordenar por puntos para administración general
        model.addAttribute("usuarios", usuarioService.listarTodosLosUsuarios());
        return "admin-usuarios";
    }

    // 2. POST: Procesa la edición del usuario desde el panel administrativo
    @PostMapping("/admin/usuarios")
    public String guardarOActualizarUsuarioAdmin(@ModelAttribute("usuario") Usuario usuario, HttpSession session) {
        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioActual == null || !"ADMIN".equals(usuarioActual.getRol())) {
            return "redirect:/?errorPermiso=true";
        }

        if (usuario.getId() != null) {
            // Obtenemos el registro real de la BD para no perder datos que no están en el formulario
            Usuario uBD = usuarioService.obtenerUsuarioPorId(usuario.getId());
            if (uBD != null) {
                uBD.setNombre(usuario.getNombre());
                uBD.setCorreoElectronico(usuario.getCorreoElectronico());
                uBD.setRol(usuario.getRol());
                if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                    uBD.setPassword(usuario.getPassword());
                }
                usuarioService.actualizarPerfil(uBD);
            }
        }
        return "redirect:/admin/usuarios";
    }

    // POST: Eliminar usuario — rol ya validado por AdminInterceptor
    @PostMapping("/admin/usuarios/eliminar")
    public String eliminarUsuarioAdmin(@RequestParam("id") Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/admin/usuarios";
    }
    
    @GetMapping("/admin/catalogos")
    public String catalogos(
            @RequestParam(defaultValue = "paises") String tab,
            Model model) {

        model.addAttribute("tab", tab);        
        model.addAttribute("fase", new Fase());
        model.addAttribute("paisAlta", new Pais());        
        model.addAttribute("paises", catalogosService.listarPaises());
        model.addAttribute("fases", catalogosService.listarFases());        
        model.addAttribute("faseEstatus", listaFasesEstatus);        

        return "admin-catalogos";
    }
    
    @PostMapping("/admin/catalogos/fases")
    public String guardarFase(@ModelAttribute Fase fase) {

    	catalogosService.registrarFase(fase);

        return "redirect:/admin/catalogos?tab=fases";
    }
	
    @GetMapping("/admin/catalogos/fase/eliminar")
    public String eliminarFase(
            @RequestParam Long id) {

    	catalogosService.eliminarFase(id);

        return "redirect:/admin/catalogos?tab=fases";
    }
    
    @PostMapping("/admin/catalogos/fases/estado")
    public String actualizarEstadoFase(
            @RequestParam Long id,
            @RequestParam String estado) {

        Fase fase = catalogosService.obtenerFasePorId(id);
        fase.setEstado(estado);
        catalogosService.registrarFase(fase);

        return "redirect:/admin/catalogos?tab=fases";
    }
    
    @PostMapping("/admin/catalogos/paises")
    public String guardarPais(@ModelAttribute Pais pais) {

    	catalogosService.registrarPais(pais);

        return "redirect:/admin/catalogos?tab=paises";
    }


    @PostMapping("/admin/catalogos/paises/estado")
    public String actualizarEstadoPais(
            @RequestParam Long id,
            @RequestParam String estado) {

        Pais pais = catalogosService.obtenerPaisPorId(id);
        pais.setEstado(estado);
        catalogosService.registrarPais(pais);

        return "redirect:/admin/catalogos?tab=paises";
    }
	


}
