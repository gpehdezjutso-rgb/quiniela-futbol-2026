package com.quiniela.pojo;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "partidos")
public class Partido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private String equipoLocal;
    private String equipoVisitante;
    private Integer golesLocal;
    private Integer golesVisitante;
    private LocalDateTime fechaPartido;
    private String grupo;    
    private String estadio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fase_id")
    private Fase fase;

    // Constructores
    public Partido() {}

    public Partido(Long id, String equipoLocal, String equipoVisitante, LocalDateTime fechaPartido, Fase fase, String grupo, String estadio) {
        this.id = id;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.fechaPartido = fechaPartido;
        this.fase = fase;
        this.grupo = grupo;
        this.estadio = estadio;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEquipoLocal() { return equipoLocal; }
    public void setEquipoLocal(String equipoLocal) { this.equipoLocal = equipoLocal; }

    public String getEquipoVisitante() { return equipoVisitante; }
    public void setEquipoVisitante(String equipoVisitante) { this.equipoVisitante = equipoVisitante; }

    public Integer getGolesLocal() { return golesLocal; }
    public void setGolesLocal(Integer golesLocal) { this.golesLocal = golesLocal; }

    public Integer getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(Integer golesVisitante) { this.golesVisitante = golesVisitante; }

    public LocalDateTime getFechaPartido() { return fechaPartido; }
    public void setFechaPartido(LocalDateTime fechaPartido) { this.fechaPartido = fechaPartido; }

	public String getGrupo() { return grupo; }

	public void setGrupo(String grupo) { this.grupo = grupo; }

	public Fase getFase() { return fase; }

	public void setFase(Fase fase) { this.fase = fase; }

	public String getEstadio() {
		return estadio;
	}

	public void setEstadio(String estadio) {
		this.estadio = estadio;
	}

	
    

}
