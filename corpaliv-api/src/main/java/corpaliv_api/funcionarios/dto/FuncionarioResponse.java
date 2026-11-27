package corpaliv_api.funcionarios.dto;

import com.corpaliv.funcionarios.model.Funcionario;

/** Nunca incluye la contraseña ni su hash. */
public record FuncionarioResponse(Long id, String usuario, String nombre, boolean activo) {

    public static FuncionarioResponse desde(Funcionario f) {
        return new FuncionarioResponse(f.getId(), f.getUsuario(), f.getNombre(), f.isActivo());
    }
}
