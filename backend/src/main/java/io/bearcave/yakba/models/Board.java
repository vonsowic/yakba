package io.bearcave.yakba.models;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Board {
    private String id;
    private String name;
    private List<UserBoardAccess> accesses = Collections.emptyList();
    private List<Column> columns = Collections.emptyList();

    public Board() {
    }

    public Board(String name, String ownerId) {
        this.name = name;

        var access = new UserBoardAccess(ownerId);
        access.setAccessLevel(UserBoardAccess.BoardAccessLevel.ADMIN);
        this.accesses = Collections.singletonList(access);
    }
}
