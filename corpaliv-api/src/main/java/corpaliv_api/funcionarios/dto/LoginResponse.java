package corpaliv_api.funcionarios.dto;

import com.corpaliv.funcionarios.model.Funcionario;

public record LoginResponse(Long id, String usuario, String nombre) {

    public static LoginResponse desde(Funcionario f) {
        return new LoginResponse(f.getId(), f.getUsuario(), f.getNombre());
    }
}