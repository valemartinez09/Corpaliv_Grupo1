package corpaliv_api.funcionarios.config;


import com.corpaliv.funcionarios.model.Funcionario;
import com.corpaliv.funcionarios.repository.FuncionarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final FuncionarioRepository repositorio;
    private final PasswordEncoder encoder;

    public DatosIniciales(FuncionarioRepository repositorio, PasswordEncoder encoder) {
        this.repositorio = repositorio;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (repositorio.count() > 0) {
            return;
        }
        // Credenciales ficticias solo para el MVP académico
        Funcionario f = new Funcionario();
        f.setUsuario("funcionario");
        f.setNombre("Funcionario Demo");
        f.setClaveHash(encoder.encode("alba2026"));
        f.setActivo(true);
        repositorio.save(f);
    }
}