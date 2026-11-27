package corpaliv_api.registros.model.service;

import com.corpaliv.registros.dto.RegistroRequest;
import com.corpaliv.registros.dto.RegistroResponse;
import com.corpaliv.registros.exception.RecursoNoEncontradoException;
import com.corpaliv.registros.exception.ReglaNegocioException;
import com.corpaliv.registros.model.EstadoRegistro;
import com.corpaliv.registros.model.ModalidadColaboracion;
import com.corpaliv.registros.model.Registro;
import com.corpaliv.registros.model.TipoColaborador;
import com.corpaliv.registros.repository.RegistroRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistroService {

    private final RegistroRepository repositorio;

    public RegistroService(RegistroRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<RegistroResponse> listar(EstadoRegistro estado) {
        List<Registro> lista = (estado == null)
                ? repositorio.findAllByOrderByFechaDesc()
                : repositorio.findByEstadoOrderByFechaDesc(estado);
        return lista.stream().map(RegistroResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public RegistroResponse obtener(Long id) {
        return RegistroResponse.desde(buscar(id));
    }

    @Transactional
    public RegistroResponse crear(RegistroRequest datos) {
        validarReglas(datos);
        Registro registro = new Registro();
        copiarDatos(datos, registro);
        registro.setEstado(EstadoRegistro.PENDIENTE);
        registro.setFecha(System.currentTimeMillis());
        return RegistroResponse.desde(repositorio.save(registro));
    }

    @Transactional
    public RegistroResponse actualizar(Long id, RegistroRequest datos) {
        validarReglas(datos);
        Registro registro = buscar(id);
        copiarDatos(datos, registro);
        return RegistroResponse.desde(repositorio.save(registro));
    }

    @Transactional
    public RegistroResponse cambiarEstado(Long id, EstadoRegistro estado) {
        Registro registro = buscar(id);
        registro.setEstado(estado);
        return RegistroResponse.desde(repositorio.save(registro));
    }

    @Transactional
    public void eliminar(Long id) {
        repositorio.delete(buscar(id));
    }

    private Registro buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un registro con id " + id));
    }

    /** Mismas reglas que valida la app en el formulario. */
    private void validarReglas(RegistroRequest d) {
        if (d.tipo() == TipoColaborador.EMPRESA
                && (d.contacto() == null || d.contacto().isBlank())) {
            throw new ReglaNegocioException("La persona de contacto es obligatoria para empresas");
        }
        if (d.tipo() == TipoColaborador.PERSONA && d.modalidad() == ModalidadColaboracion.ALIANZA) {
            throw new ReglaNegocioException("Una persona no puede elegir la modalidad ALIANZA");
        }
        if (d.tipo() == TipoColaborador.EMPRESA && d.modalidad() == ModalidadColaboracion.VOLUNTARIADO) {
            throw new ReglaNegocioException("Una empresa no puede elegir la modalidad VOLUNTARIADO");
        }
        if (d.frecuencia() != null && d.modalidad() != ModalidadColaboracion.DONACION) {
            throw new ReglaNegocioException("La frecuencia solo aplica a las donaciones");
        }
    }

    private void copiarDatos(RegistroRequest d, Registro r) {
        r.setTipo(d.tipo());
        r.setNombre(d.nombre().trim());
        r.setContacto(d.contacto() == null ? null : d.contacto().trim());
        r.setCorreo(d.correo().trim());
        r.setModalidad(d.modalidad());
        r.setFrecuencia(d.frecuencia());
        r.setMensaje(d.mensaje() == null ? null : d.mensaje().trim());
        r.setCartaNombre(d.cartaNombre());
    }
}