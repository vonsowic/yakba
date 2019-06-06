package io.bearcave.yakba.models;

import lombok.Data;

@Data
public class Card {
    private String id;
    private String title;
    private String content;
    private String createdByUserId;
}
