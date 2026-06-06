package com.quiniela.pojo;

/**
 * Enum que representa el resultado de intentar guardar una apuesta.
 * Permite al controlador comunicar al usuario exactamente qué pasó.
 */
public enum ResultadoApuesta {
    EXITO,
    PARTIDO_CON_RESULTADO,
    PARTIDO_EXPIRADO,
    DATOS_INVALIDOS
}
