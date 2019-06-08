package io.bearcave.yakba.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Data
public class Column {
    private String id;
    private String name;

    @JsonInclude(Include.NON_EMPTY)
    private List<Card> cards = Collections.emptyList();

    public boolean idEquals(String id) {
        return Objects.equals(this.id, id);
    }
}
