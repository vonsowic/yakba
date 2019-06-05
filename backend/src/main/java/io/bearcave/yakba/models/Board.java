package io.bearcave.yakba.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mongodb.lang.NonNull;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
public class Board {

    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private List<UserBoardAccess> accesses = Collections.emptyList();

    @NonNull
    @JsonInclude(Include.NON_EMPTY)
    private List<Column> columns = Collections.emptyList();

    public Board() {
    }

    public Board(String name, String ownerId) {
        this.name = name;

        var access = new UserBoardAccess(ownerId);
        access.setAccessLevel(BoardAccessLevel.ADMIN);
        this.accesses = Collections.singletonList(access);
    }

    @JsonIgnore
    public Optional<Column> getLastColumn() {
        if (columns.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(getColumns().get(getColumns().size() - 1));
    }

    public void appendColumn(Column column) {
        this.columns.add(column);
    }
}
