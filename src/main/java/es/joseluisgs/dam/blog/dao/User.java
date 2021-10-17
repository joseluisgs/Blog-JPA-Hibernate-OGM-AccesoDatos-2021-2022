package es.joseluisgs.dam.blog.dao;

import es.joseluisgs.dam.blog.utils.Cifrador;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

// @Data Ojo con el data que entra en un bucle infinito por la definición de la relación muchos a uno, debes hacer el string a mano
// y Quitar los posts
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user") // Ojo con la minuscula que en la tabla está así
// Todos los usuarios
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    // Todos los usuarios con emial indicados, ojo, no usar parámetros
    @NamedQuery(name = "User.getByMail", query = "SELECT u FROM User u WHERE u.email = :email"),
    // Todos los post de un usuario
    @NamedQuery(name = "User.getMyPosts", query = "SELECT u.posts FROM User u WHERE u.id = :userId")
})
public class User {
    private long id;
    private String nombre;
    private String email;
    private String password;
    private Date fechaRegistro;
    private Set<Post> posts;
    private Set<Comment> comments;

    public User(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = Cifrador.getInstance().SHA256(password);
        this.fechaRegistro = new Date(System.currentTimeMillis());
        this.posts = new HashSet<Post>();
        this.comments = new HashSet<Comment>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nombre", nullable = false, length = 100)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    // @ColumnTransformer(write = " SHA(?) ")
    // Le decimos que lo transforme con esta función. Nos ahorramos cifrarlo nosotros
    @Column(name = "password", nullable = false, length = 100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "fecha_registro", nullable = false)
    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(nombre, user.nombre) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(fechaRegistro, user.fechaRegistro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, email, password, fechaRegistro);
    }

    // Cuidado que hay que poner los orphan para que no se queden colgados los 1 a 1 al quitar los login
    // https://stackoverflow.com/questions/2302802/how-to-fix-the-hibernate-object-references-an-unsaved-transient-instance-save # 96

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "topic", cascade = CascadeType.ALL)
    // Si lo ponemos a lazy perdemos el contecto de la sesión.. a veces y te puedes saltarte una excepción
    /* En @OneToMany el fetch type default es Lazy, esto hace que el atributo posts no sea instanciado hasta que se haga getPosts().
       El problema es que en ese momento ya no cuentas con la Session de JPA, es decir, que la llamada a getPosts()
       debería haber ocurrido antes cuando estabas buscando los datos en el userRepository.
        cambia el comportamiento default con @OneToMany(fetch=FetchType.EAGER).
        Esto hace que friends se instancie junto con el resto de los atributos.
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE) // Estudiar la cascada
    public Set<Post> getPosts() {
        return posts;
    }

    // No es necesario si no queremos cambiar los post desde usuario
    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    // La Cascada
    // http://openjpa.apache.org/builds/2.4.0/apache-openjpa/docs/jpa_overview_meta_field.html#jpa_overview_meta_cascade
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE) // cascade = CascadeType.ALL
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Override
    // No es obligatorio, pero al hacerlo podemos tener problemas con la recursividad de las llamadas
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                // ", password='" + password + '\'' + Evitamos
                ", fechaRegistro=" + fechaRegistro +
                // Cuidado aqui con las llamadas recursivas No me interesa imprimir los post del usuario, pueden ser muchos
                 // ", posts=" + posts + // Podriamos quitarlos para no verlos
                // Tampoco saco los comentarios
                '}';
    }
}
