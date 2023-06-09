package com.games.minesweeper.dto.response;

import com.games.minesweeper.dto.GameStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameResponse {
    private CellResponse[][] cellResponse;
    private GameStatus gameStatus;
    private int totalCountOfMines;
    private int currentCountOfFlags;
    private int maximumCountOfFlags;
    private int unrevealedAmount;
}
