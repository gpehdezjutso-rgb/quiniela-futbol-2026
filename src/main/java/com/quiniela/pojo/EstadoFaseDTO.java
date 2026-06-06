package com.quiniela.pojo;

public class EstadoFaseDTO {

    private String fase;
    private String estado;

    public EstadoFaseDTO(String fase, String estado) {
        this.fase = fase;
        this.estado = estado;
    }

    public String getFase() {
        return fase;
    }

    public String getEstado() {
        return estado;
    }
}
