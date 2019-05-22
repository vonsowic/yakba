package io.bearcave.yakba.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class UserBoardAccess {
    private String userId;
    private String boardId;
    private BoardAccessLevel accessLevel = BoardAccessLevel.USER;

    public UserBoardAccess() {

    }

    public UserBoardAccess(String userId, String boardId) {
        this.userId = userId;
        this.boardId = boardId;
    }

    public enum BoardAccessLevel {
        USER,
        ADMIN
    }
}
