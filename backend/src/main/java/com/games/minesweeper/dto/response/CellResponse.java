package com.games.minesweeper.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CellResponse {
    public CellResponse(boolean isMine, boolean revealed, boolean flagged, String content) {
        this.isMine = isMine;
        this.revealed = revealed;
        this.flagged = flagged;
        this.content = content;
    }

    private boolean isMine;
    private boolean revealed;
    private boolean flagged;
    private boolean highLighted = new Random().nextBoolean();
    private String content;
}
