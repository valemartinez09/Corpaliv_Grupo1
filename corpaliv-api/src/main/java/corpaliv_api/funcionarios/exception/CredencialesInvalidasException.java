package corpaliv_api.funcionarios.exception;

/** Mismo mensaje para "usuario no existe" y "contraseña incorrecta". */
public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException() {
        super("Usuario o contraseña incorrectos");
    }
}