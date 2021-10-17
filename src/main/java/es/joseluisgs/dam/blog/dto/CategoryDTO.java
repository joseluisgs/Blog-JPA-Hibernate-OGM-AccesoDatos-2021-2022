package es.joseluisgs.dam.blog.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CategoryDTO {
    private Long id;
    private String texto;

    // TODO Bidireccionalidad
    // Lista de post que tiene asociado. Por ahora suprimo la bidireccionalidad
    // private final Set<Post> posts = new HashSet<>();

//    public String toString() {
//        return toJSON();
//    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                '}';
    }
}
