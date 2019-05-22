package io.bearcave.yakba.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class Board {
    private String id;
    private String name;
}
