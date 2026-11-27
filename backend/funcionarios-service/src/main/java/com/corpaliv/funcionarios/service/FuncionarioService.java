package com.corpaliv.funcionarios.service;

import com.corpaliv.funcionarios.dto.FuncionarioRequest;
import com.corpaliv.funcionarios.dto.FuncionarioResponse;
import com.corpaliv.funcionarios.dto.LoginRequest;
import com.corpaliv.funcionarios.dto.LoginResponse;
import com.corpaliv.funcionarios.exception.ConflictoException;
import com.corpaliv.funcionarios.exception.CredencialesInvalidasException;
import com.corpaliv.funcionarios.exception.CuentaBloqueadaException;
import com.corpaliv.funcionarios.exception.RecursoNoEncontradoException;
import com.corpaliv.funcionarios.exception.ReglaNegocioException;
import com.corpaliv.funcionarios.model.Funcionario;
import com.corpaliv.funcionarios.repository.FuncionarioRepository;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FuncionarioService {

    private static final Pattern TIENE_LETRA = Pattern.compile(".*[A-Za-z].*");
    private static final Pattern TIENE_DIGITO = Pattern.compile(".*\\d.*");

    private final FuncionarioRepository repositorio;
    private final PasswordEncoder encoder;
    private final int maxIntentos;
    private final long bloqueoMs;

    // Hash de una clave inventada: se compara cuando el usuario no existe, para que
    // la respuesta tarde lo mismo y no se pueda deducir qué usuarios existen.
    private final String hashFalso;

    public FuncionarioService(
            FuncionarioRepository repositorio,
            PasswordEncoder encoder,
            @Value("${corpaliv.seguridad.max-intentos:5}") int maxIntentos,
            @Value("${corpaliv.seguridad.bloqueo-minutos:5}") long bloqueoMinutos) {
        this.repositorio = repositorio;
        this.encoder = encoder;
        this.maxIntentos = maxIntentos;
        this.bloqueoMs = bloqueoMinutos * 60_000L;
        this.hashFalso = encoder.encode("clave-inventada-para-igualar-tiempos-1");
    }

    // ---------- Login ----------

    @Transactional(noRollbackFor = CredencialesInvalidasException.class)
    public LoginResponse login(LoginRequest datos) {
        Funcionario f = repositorio.findByUsuario(normalizar(datos.usuario())).orElse(null);

        if (f == null) {
            encoder.matches(datos.clave(), hashFalso);
            throw new CredencialesInvalidasException();
        }

        long ahora = System.currentTimeMillis();
        if (f.getBloqueadoHasta() > ahora) {
            long minutos = (f.getBloqueadoHasta() - ahora + 59_999L) / 60_000L;
            throw new CuentaBloqueadaException(minutos);
        }

        boolean correcta = encoder.matches(datos.clave(), f.getClaveHash());
        if (!correcta || !f.isActivo()) {
            registrarFallo(f, ahora);
            throw new CredencialesInvalidasException();
        }

        f.setIntentosFallidos(0);
        f.setBloqueadoHasta(0);
        repositorio.save(f);
        return LoginResponse.desde(f);
    }

    private void registrarFallo(Funcionario f, long ahora) {
        int intentos = f.getIntentosFallidos() + 1;
        if (intentos >= maxIntentos) {
            f.setBloqueadoHasta(ahora + bloqueoMs);
            f.setIntentosFallidos(0);
        } else {
            f.setIntentosFallidos(intentos);
        }
        repositorio.save(f);
    }

    // ---------- CRUD ----------

    @Transactional(readOnly = true)
    public List<FuncionarioResponse> listar() {
        return repositorio.findAllByOrderByUsuarioAsc().stream()
                .map(FuncionarioResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public FuncionarioResponse obtener(Long id) {
        return FuncionarioResponse.desde(buscar(id));
    }

    @Transactional
    public FuncionarioResponse crear(FuncionarioRequest datos) {
        String usuario = normalizar(datos.usuario());
        if (repositorio.existsByUsuario(usuario)) {
            throw new ConflictoException("Ya existe un funcionario con el usuario \"" + usuario + "\"");
        }
        validarClave(datos.clave());

        Funcionario f = new Funcionario();
        f.setUsuario(usuario);
        f.setNombre(datos.nombre().trim());
        f.setClaveHash(encoder.encode(datos.clave()));
        f.setActivo(datos.activo() == null || datos.activo());
        return FuncionarioResponse.desde(repositorio.save(f));
    }

    @Transactional
    public FuncionarioResponse actualizar(Long id, FuncionarioRequest datos) {
        Funcionario f = buscar(id);
        String usuario = normalizar(datos.usuario());
        if (repositorio.existsByUsuarioAndIdNot(usuario, id)) {
            throw new ConflictoException("Ya existe otro funcionario con el usuario \"" + usuario + "\"");
        }

        boolean nuevoActivo = datos.activo() == null || datos.activo();
        if (f.isActivo() && !nuevoActivo && repositorio.countByActivoTrue() <= 1) {
            throw new ConflictoException("No se puede desactivar al último funcionario activo");
        }

        f.setUsuario(usuario);
        f.setNombre(datos.nombre().trim());
        f.setActivo(nuevoActivo);

        // La clave solo cambia si viene en la solicitud
        if (datos.clave() != null && !datos.clave().isBlank()) {
            validarClave(datos.clave());
            f.setClaveHash(encoder.encode(datos.clave()));
            f.setIntentosFallidos(0);
            f.setBloqueadoHasta(0);
        }
        return FuncionarioResponse.desde(repositorio.save(f));
    }

    @Transactional
    public void eliminar(Long id) {
        Funcionario f = buscar(id);
        if (f.isActivo() && repositorio.countByActivoTrue() <= 1) {
            throw new ConflictoException("No se puede eliminar al último funcionario activo");
        }
        repositorio.delete(f);
    }

    // ---------- Apoyo ----------

    private Funcionario buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un funcionario con id " + id));
    }

    private String normalizar(String usuario) {
        return usuario.trim().toLowerCase(Locale.ROOT);
    }

    private void validarClave(String clave) {
        if (clave == null || clave.isBlank()) {
            throw new ReglaNegocioException("La contraseña es obligatoria");
        }
        if (clave.length() < 8 || clave.length() > 72) {
            throw new ReglaNegocioException("La contraseña debe tener entre 8 y 72 caracteres");
        }
        if (!TIENE_LETRA.matcher(clave).matches() || !TIENE_DIGITO.matcher(clave).matches()) {
            throw new ReglaNegocioException("La contraseña debe incluir letras y números");
        }
    }
}