package es.joseluisgs.dam.blog.dto;

import es.joseluisgs.dam.blog.dao.Post;
import es.joseluisgs.dam.blog.dao.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Builder
@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String texto;
    private Timestamp fechaPublicacion;
    // Autor que la realiza
    private User user;
    // Post al que pertenece
    private Post post;

    @Override
    // DE comentario nos interesa saber su autor, porque el foro sabemos cual si accedemos por foro
    // Asi que en vez d eimprimirlo todo, solo haremos cosas que nos interese
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", user=" + user +
                ", post= Post{id:" + post.getId() + ", titulo=" + post.getTitulo() + ", " + "url=" + post.getUrl() +
                "}}";
    }
}
