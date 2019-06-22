package io.bearcave.yakba.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Document
public class User {

    @Id
    private String username;

    @Indexed(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @NotNull
    private Date createdAt = new Date();

    @JsonIgnore
    private boolean enabled = true;
}
