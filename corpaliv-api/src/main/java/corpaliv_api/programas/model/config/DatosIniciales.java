package corpaliv_api.programas.model.config;

import com.corpaliv.programas.model.CategoriaPrograma;
import com.corpaliv.programas.model.Programa;
import com.corpaliv.programas.repository.ProgramaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final ProgramaRepository repositorio;

    public DatosIniciales(ProgramaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void run(String... args) {
        if (repositorio.count() > 0) {
            return;
        }
        repositorio.save(nuevo(
                "Inclusión sociolaboral",
                "A través de la Red Sociolaboral Alba Lab preparamos a personas con discapacidad "
                        + "múltiple para el empleo y trabajamos con empresas para generar "
                        + "oportunidades laborales reales.",
                CategoriaPrograma.INCLUSION, 1));
        repositorio.save(nuevo(
                "Producción y comercialización",
                "Nuestros talleres elaboran productos con impacto social. Cada compra apoya la "
                        + "autonomía de quienes los producen.",
                CategoriaPrograma.PRODUCCION, 2));
        repositorio.save(nuevo(
                "Escuela Jan Van Dijk",
                "La Escuela Especial Jan Van Dijk ofrece formación pensada para desarrollar la "
                        + "autonomía y la participación de cada estudiante.",
                CategoriaPrograma.ESCUELA, 3));
    }

    private Programa nuevo(String titulo, String descripcion,
                           CategoriaPrograma categoria, int orden) {
        Programa p = new Programa();
        p.setTitulo(titulo);
        p.setDescripcion(descripcion);
        p.setCategoria(categoria);
        p.setOrden(orden);
        p.setActivo(true);
        return p;
    }
}