package es.joseluisgs.dam.blog.dto;

import es.joseluisgs.dam.blog.dao.Comment;
import es.joseluisgs.dam.blog.dao.Post;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Builder
@Getter
@Setter
// Exposé expone solo los campso que queramos en el JSON
public class UserDTO {
    // @Expose
    private Long id;
    // @Expose
    private String nombre;
    // @Expose
    private String email;
    // @Expose
    private Date fechaRegistro;

    // TODO Bidireccionalidad
    // Lista de Comentarios
    private Set<Comment> comentarios = new HashSet<>();
    // Lista de Posts
    private Set<Post> posts = new HashSet<>();
    // Su login activo si lo tiene
    //private Login login;

    // Eliminar campos de las serialización
    // https://www.baeldung.com/gson-exclude-fields-serialization
    private String password;

    
//    @Override
//    public String toString() {
//        return this.toJSON();
//    }


    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                // ", posts=" + posts + // No voy a imprimir los posts poprque son muy largos
                //", password='" + password + '\'' + // Así no sale el password
                '}';
    }
}