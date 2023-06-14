package com.games.minesweeper.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CellResponse {
    private boolean isMine;
    private boolean revealed;
    private boolean flagged;
    private String content;
    private boolean highLighted;
}
