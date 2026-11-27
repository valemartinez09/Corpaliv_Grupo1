package com.corpaliv.funcionarios.exception;

public class CuentaBloqueadaException extends RuntimeException {

    public CuentaBloqueadaException(long minutosRestantes) {
        super("Demasiados intentos fallidos. Intenta de nuevo en " + minutosRestantes
                + (minutosRestantes == 1 ? " minuto" : " minutos"));
    }
}
