package io.bearcave.yakba.dto;

import lombok.Data;

@Data
public class CardOrderUpdateDTO {
    private CardPositionDTO prevPos;
    private CardPositionDTO nextPos;
}
