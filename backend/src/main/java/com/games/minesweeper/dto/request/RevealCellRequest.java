package com.games.minesweeper.dto.request;

import lombok.Data;

@Data
public class RevealCellRequest {
    private Integer row;
    private Integer column;
    private boolean revealed;
}
