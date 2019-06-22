package io.bearcave.yakba.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Column {
    private String id;
    private String name;

    @JsonInclude(Include.NON_EMPTY)
    private List<Card> cards = Collections.emptyList();
}
