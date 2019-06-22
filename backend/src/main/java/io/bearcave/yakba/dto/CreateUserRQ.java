package io.bearcave.yakba.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class CreateUserRQ {
    private String username;
    private String email;
    private String password;

    @JsonIgnore
    public boolean isNotEmpty() {
        return StringUtils.isNotEmpty(username)
                && StringUtils.isNotEmpty(email)
                && StringUtils.isNotEmpty(password);
    }
}
