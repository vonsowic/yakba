package io.bearcave.yakba.dto;

import io.bearcave.yakba.models.BoardAccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserBoardAccessDTO {
    private String boardId;
    private String boardName;
    private BoardAccessLevel access;
}
