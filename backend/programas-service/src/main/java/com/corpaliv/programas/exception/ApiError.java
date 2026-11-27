package com.corpaliv.programas.exception;

import java.util.Map;

/** Formato único de error que recibe la app. */
public record ApiError(int estado, String mensaje, Map<String, String> detalles) {
}