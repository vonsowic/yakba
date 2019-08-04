package io.bearcave.yakba.dto;

import io.bearcave.yakba.models.BoardAccessLevel;
import lombok.Data;

@Data
public class UserRS {
    private String username;
    private BoardAccessLevel accessLevel;
}
