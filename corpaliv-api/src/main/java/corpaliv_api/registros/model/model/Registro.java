package corpaliv_api.registros.model.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "registros")
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoColaborador tipo;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 120)
    private String contacto;

    @Column(nullable = false, length = 160)
    private String correo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModalidadColaboracion modalidad;

    @Enumerated(EnumType.STRING)
    private FrecuenciaDonacion frecuencia;

    @Column(length = 1000)
    private String mensaje;

    @Column(length = 255)
    private String cartaNombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRegistro estado = EstadoRegistro.PENDIENTE;

    // Milisegundos desde 1970, igual que el campo "fecha" de la app
    @Column(nullable = false)
    private long fecha;

    public Long getId() { return id; }

    public TipoColaborador getTipo() { return tipo; }
    public void setTipo(TipoColaborador tipo) { this.tipo = tipo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public ModalidadColaboracion getModalidad() { return modalidad; }
    public void setModalidad(ModalidadColaboracion modalidad) { this.modalidad = modalidad; }

    public FrecuenciaDonacion getFrecuencia() { return frecuencia; }
    public void setFrecuencia(FrecuenciaDonacion frecuencia) { this.frecuencia = frecuencia; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getCartaNombre() { return cartaNombre; }
    public void setCartaNombre(String cartaNombre) { this.cartaNombre = cartaNombre; }

    public EstadoRegistro getEstado() { return estado; }
    public void setEstado(EstadoRegistro estado) { this.estado = estado; }

    public long getFecha() { return fecha; }
    public void setFecha(long fecha) { this.fecha = fecha; }
}