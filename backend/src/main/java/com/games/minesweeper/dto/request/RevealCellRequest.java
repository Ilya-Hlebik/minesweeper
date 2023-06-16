package com.games.minesweeper.dto.request;

import lombok.Data;

@Data
public class RevealCellRequest {
    private String gameId;
    private Integer row;
    private Integer column;
}
