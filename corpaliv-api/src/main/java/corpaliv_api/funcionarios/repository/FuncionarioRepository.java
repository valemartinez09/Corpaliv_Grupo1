package corpaliv_api.funcionarios.repository;

import com.corpaliv.funcionarios.model.Funcionario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByUsuario(String usuario);

    boolean existsByUsuario(String usuario);

    boolean existsByUsuarioAndIdNot(String usuario, Long id);

    long countByActivoTrue();

    List<Funcionario> findAllByOrderByUsuarioAsc();
}