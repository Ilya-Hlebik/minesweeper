package com.games.minesweeper.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SetFlaggedRequest extends GameIdRequest {
    private boolean flagged;
    private Integer row;
    private Integer column;
}
