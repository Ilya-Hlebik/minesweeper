package com.games.minesweeper.dto.request;

import lombok.Data;

@Data
public class MinesCountRequest {
    private Integer rows;
    private Integer columns;
}
