package com.quiniela.pojo;

import javax.persistence.*;

@Entity
@Table(name = "estadios")
public class Estadios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    private String estado;

	public Estadios(String nombre, String estado) {		
		this.nombre = nombre;
		this.estado = estado;
	}

	public Estadios() {		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	   

}