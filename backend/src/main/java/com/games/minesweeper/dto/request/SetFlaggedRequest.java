package com.games.minesweeper.dto.request;

import lombok.Data;

@Data
public class SetFlaggedRequest {
    private boolean flagged;
    private Integer row;
    private Integer column;
}
