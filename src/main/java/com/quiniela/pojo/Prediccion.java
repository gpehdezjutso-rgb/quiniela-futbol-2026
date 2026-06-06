package com.quiniela.pojo;

import javax.persistence.*;

@Entity
@Table(name = "predicciones")
public class Prediccion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	 // Relación: Muchas predicciones pertenecen a un solo Usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
 // Relación: Muchas predicciones pertenecen a un solo Partido
    @ManyToOne
    @JoinColumn(name = "partido_id", nullable = false)
    private Partido partido;
    
    private Integer golesLocalPrediccion;
    private Integer golesVisitantePrediccion;
    private Integer puntosGanados;
    
    public Prediccion() {
        this.puntosGanados = 0;
    }

    public Prediccion(Long id, Usuario usuario, Partido partido, Integer golesLocalPrediccion, Integer golesVisitantePrediccion) {
        this.id = id;
        this.usuario = usuario;
        this.partido = partido;
        this.golesLocalPrediccion = golesLocalPrediccion;
        this.golesVisitantePrediccion = golesVisitantePrediccion;
        this.puntosGanados = 0;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Partido getPartido() { return partido; }
    public void setPartido(Partido partido) { this.partido = partido; }

    public Integer getGolesLocalPrediccion() { return golesLocalPrediccion; }
    public void setGolesLocalPrediccion(Integer golesLocalPrediccion) { this.golesLocalPrediccion = golesLocalPrediccion; }

    public Integer getGolesVisitantePrediccion() { return golesVisitantePrediccion; }
    public void setGolesVisitantePrediccion(Integer golesVisitantePrediccion) { this.golesVisitantePrediccion = golesVisitantePrediccion; }

    public Integer getPuntosGanados() { return puntosGanados; }
    public void setPuntosGanados(Integer puntosGanados) { this.puntosGanados = puntosGanados; }
}
