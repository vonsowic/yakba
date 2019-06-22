package io.bearcave.yakba.dto;

import lombok.Data;

@Data
public class CardOrderUpdateRQ {
    private CardPosition prevPos;
    private CardPosition nextPos;
}
