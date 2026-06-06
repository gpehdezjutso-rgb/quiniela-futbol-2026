package com.quiniela.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "posiciones_mundial")
public class EstadisticaEquipo {

    @Id 
    private String equipo;
    
    private int partidosJugados;
    private int triunfos;
    private int empates;
    private int derrotas;
    private int golesFavor;
    private int golesContra;
    private int diferenciaGoles;
    private int puntos;

    // Constructor obligatorio para Hibernate
    public EstadisticaEquipo() {}

    public EstadisticaEquipo(String equipo) {
        this.equipo = equipo;
    }
    
    public void acumularPartido(int golesFavor, int golesContra) {
        this.partidosJugados++;
        this.golesFavor += golesFavor;
        this.golesContra += golesContra;
        this.diferenciaGoles = this.golesFavor - this.golesContra;

        if (golesFavor > golesContra) {
            this.triunfos++;
            this.puntos += 3;
        } else if (golesFavor == golesContra) {
            this.empates++;
            this.puntos += 1;
        } else {
            this.derrotas++;
        }
    }
    
    public String getEquipo() { return equipo; }
    public void setEquipo(String equipo) { this.equipo = equipo; }
    
    public int getPartidosJugados() { return partidosJugados; }
    public void setPartidosJugados(int partidosJugados) { this.partidosJugados = partidosJugados; }

    public int getTriunfos() { return triunfos; }
    public void setTriunfos(int triunfos) { this.triunfos = triunfos; }

    public int getEmpates() { return empates; }
    public void setEmpates(int empates) { this.empates = empates; }

    public int getDerrotas() { return derrotas; }
    public void setDerrotas(int derrotas) { this.derrotas = derrotas; }

    public int getGolesFavor() { return golesFavor; }
    public void setGolesFavor(int golesFavor) { this.golesFavor = golesFavor; }

    public int getGolesContra() { return golesContra; }
    public void setGolesContra(int golesContra) { this.golesContra = golesContra; }

    public int getDiferenciaGoles() { return diferenciaGoles; }
    public void setDiferenciaGoles(int diferenciaGoles) { this.diferenciaGoles = diferenciaGoles; }

    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }
    
    
}
