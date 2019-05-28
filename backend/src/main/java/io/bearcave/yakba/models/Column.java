package io.bearcave.yakba.models;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Column {
    private String id;
    private String name;
    private List<Card> card = Collections.emptyList();
}
