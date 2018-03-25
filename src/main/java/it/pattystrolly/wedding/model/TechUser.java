package it.pattystrolly.wedding.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class TechUser {

    @Id
    private String email;

    private String nome;

    private String accessToken;

    private String refreshToken;

    private Date expirationTime;
}
