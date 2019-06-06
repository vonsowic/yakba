package io.bearcave.yakba.dto;

import lombok.Data;

@Data
public class ColumnReorderRequestDTO {
    private String boardId;
    private String columnId;
    private Integer index;
}
