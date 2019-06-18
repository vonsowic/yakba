package io.bearcave.yakba.dto;

import lombok.Data;

@Data
public class ColumnReorderRequestRQ {
    private String boardId;
    private String columnId;
    private Integer index;
}
