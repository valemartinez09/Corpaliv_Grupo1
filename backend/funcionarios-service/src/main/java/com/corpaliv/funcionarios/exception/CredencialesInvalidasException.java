package com.corpaliv.funcionarios.exception;

/** Mismo mensaje para "usuario no existe" y "contraseña incorrecta". */
public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException() {
        super("Usuario o contraseña incorrectos");
    }
}