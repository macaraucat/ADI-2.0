package cl.aduana.ms_login.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity @Table(name="usuario", schema="bd_login")
@Data @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @EqualsAndHashCode.Include
    private Long id;
    private String tipoDocumento;
    @Column(unique = true, nullable = false, length = 10)
    private String nroDocumento;
    private String nombreCompleto;
    @Column(unique = true)
    private String email;
    private String password;
    private String organismo;
    private boolean activo = true;
    private LocalDateTime ultimoAcceso;
    private LocalDateTime fechaCreacion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = "bd_login", name="usuario_rol",
        joinColumns=@JoinColumn(name="id_usuario"),
        inverseJoinColumns=@JoinColumn(name="id_rol"))
    @ToString.Exclude
    private Set<Rol> roles = new HashSet<>();
}