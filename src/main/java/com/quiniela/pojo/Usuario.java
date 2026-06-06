package com.quiniela.pojo;

import javax.persistence.*;


@Entity
@Table(name = "usuarios")
public class Usuario {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private String nombre;
    
    @Column(unique = true, nullable = false)
    private String correoElectronico;
    
    private String password;
    private Integer puntosTotales;
    private String rol; // "ADMIN" o "USER"

    // Constructores
    public Usuario() {
        this.puntosTotales = 0;
        this.rol = "USER"; // Por defecto, todos se registran como usuarios normales
    }

    public Usuario(Long id, String nombre, String correoElectronico, String password, Integer puntosTotales, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.password = password;
        this.puntosTotales = puntosTotales;
        this.rol = rol;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getPuntosTotales() { return puntosTotales; }
    public void setPuntosTotales(Integer puntosTotales) { this.puntosTotales = puntosTotales; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
