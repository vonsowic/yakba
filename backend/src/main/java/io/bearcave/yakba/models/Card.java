package io.bearcave.yakba.models;

import lombok.Data;

import java.util.Objects;

@Data
public class Card {
    private String id;
    private String title;
    private String content;
    private String createdByUserId;

    public boolean idEquals(String id) {
        return Objects.equals(this.id, id);
    }
}
