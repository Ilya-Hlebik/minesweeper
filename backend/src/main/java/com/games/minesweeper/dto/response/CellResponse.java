package com.games.minesweeper.dto.response;

import lombok.Data;

@Data
public class CellResponse {
    private boolean isMine;
    private boolean revealed;
    private boolean flagged;
    private Integer content;
}
