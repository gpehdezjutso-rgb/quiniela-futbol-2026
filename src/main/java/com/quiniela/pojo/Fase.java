package com.quiniela.pojo;

import javax.persistence.*;

@Entity
@Table(name = "fase")
public class Fase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(name = "orden_fase")
    private Integer ordenFase;

    private String estado;

    public Fase() {		
	}

	public Fase(Long id, String nombre, Integer ordenFase, String estado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.ordenFase = ordenFase;
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getOrdenFase() {
		return ordenFase;
	}

	public void setOrdenFase(Integer ordenFase) {
		this.ordenFase = ordenFase;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
   
    
}