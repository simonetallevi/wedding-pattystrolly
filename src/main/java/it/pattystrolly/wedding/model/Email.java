package it.pattystrolly.wedding.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Email {

    @Id
    private String id;

    private String salutation;

    private List<String> emails = new ArrayList<>();

    @Index
    private Boolean sent = false;

    @Index
    private Boolean open = false;

    @Index
    private Boolean clicked = false;

    @Index
    private Answer answer = Answer.NONE;

    public enum Answer {
        NONE,
        OK,
        NO
    }

    public static Email getEmail(String[] tokens) {
        Email email = new Email();
        email.setId(UUID.randomUUID().toString());
        String[] emails = tokens[1].split("\\|");
        for (String e : emails) {
            email.getEmails().add(e);
        }
        email.setSalutation(tokens[0]);
        return email;
    }
}
