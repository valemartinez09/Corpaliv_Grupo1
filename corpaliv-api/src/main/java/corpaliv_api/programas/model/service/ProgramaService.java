package corpaliv_api.programas.model.service;


import com.corpaliv.programas.dto.ProgramaRequest;
import com.corpaliv.programas.dto.ProgramaResponse;
import com.corpaliv.programas.exception.ConflictoException;
import com.corpaliv.programas.exception.RecursoNoEncontradoException;
import com.corpaliv.programas.model.Programa;
import com.corpaliv.programas.repository.ProgramaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgramaService {

    private final ProgramaRepository repositorio;

    public ProgramaService(ProgramaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<ProgramaResponse> listar(boolean soloActivos) {
        List<Programa> lista = soloActivos
                ? repositorio.findByActivoTrueOrderByOrdenAscIdAsc()
                : repositorio.findAllByOrderByOrdenAscIdAsc();
        return lista.stream().map(ProgramaResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public ProgramaResponse obtener(Long id) {
        return ProgramaResponse.desde(buscar(id));
    }

    @Transactional
    public ProgramaResponse crear(ProgramaRequest datos) {
        String titulo = datos.titulo().trim();
        if (repositorio.existsByTituloIgnoreCase(titulo)) {
            throw new ConflictoException("Ya existe un programa con el título \"" + titulo + "\"");
        }
        Programa programa = new Programa();
        copiarDatos(datos, programa);
        return ProgramaResponse.desde(repositorio.save(programa));
    }

    @Transactional
    public ProgramaResponse actualizar(Long id, ProgramaRequest datos) {
        Programa programa = buscar(id);
        String titulo = datos.titulo().trim();
        if (repositorio.existsByTituloIgnoreCaseAndIdNot(titulo, id)) {
            throw new ConflictoException("Ya existe otro programa con el título \"" + titulo + "\"");
        }
        copiarDatos(datos, programa);
        return ProgramaResponse.desde(repositorio.save(programa));
    }

    @Transactional
    public void eliminar(Long id) {
        repositorio.delete(buscar(id));
    }

    private Programa buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un programa con id " + id));
    }

    private void copiarDatos(ProgramaRequest d, Programa p) {
        p.setTitulo(d.titulo().trim());
        p.setDescripcion(d.descripcion().trim());
        p.setCategoria(d.categoria());
        p.setOrden(d.orden() == null ? 0 : d.orden());
        p.setActivo(d.activo() == null || d.activo());
    }
}