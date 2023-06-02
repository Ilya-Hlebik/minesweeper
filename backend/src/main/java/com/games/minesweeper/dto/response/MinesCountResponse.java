package com.games.minesweeper.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MinesCountResponse {
    private Integer minesFrom;
    private Integer minesTo;
}
