package es.joseluisgs.dam.blog.dto;

import es.joseluisgs.dam.blog.dao.User;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;


@Data
@Builder
public class LoginDTO {
    private long id;
    private Timestamp ultimoAcceso;
    private String token;

    @Override
    public String toString() {
        return "LoginDTO{" +
                "userID=" + id +
                ", ultimoAcceso=" + ultimoAcceso +
                ", token='" + token + '\'' +
                '}';
    }
}
