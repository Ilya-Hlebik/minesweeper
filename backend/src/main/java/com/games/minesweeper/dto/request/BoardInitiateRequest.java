package com.games.minesweeper.dto.request;

import lombok.Data;

@Data
public class BoardInitiateRequest {
    private Integer rows;
    private Integer columns;
    private Integer mines;
}
