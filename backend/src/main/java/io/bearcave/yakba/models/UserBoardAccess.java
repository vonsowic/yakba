package io.bearcave.yakba.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"accessLevel"})
public class UserBoardAccess {
    private String userId;
    private BoardAccessLevel accessLevel = BoardAccessLevel.USER;

    public UserBoardAccess() {
    }

    public UserBoardAccess(String userId) {
        this.userId = userId;
    }

    public enum BoardAccessLevel {
        USER,
        ADMIN
    }
}
