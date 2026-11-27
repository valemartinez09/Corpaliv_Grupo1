package com.corpaliv.funcionarios.exception;

import java.util.Map;

/** Formato único de error que recibe la app. */
public record ApiError(int estado, String mensaje, Map<String, String> detalles) {
}
