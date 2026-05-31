package cl.aduana.ms_login.model;

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

@Entity @Table(name="rol", schema="bd_login")
@Data @NoArgsConstructor @AllArgsConstructor
public class Rol {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String codigo;
    private String nombre;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(schema = "bd_login", name="rol_permiso",
        joinColumns=@JoinColumn(name="id_rol"),
        inverseJoinColumns=@JoinColumn(name="id_permiso"))
    private Set<Permiso> permisos = new HashSet<>();
}