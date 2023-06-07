package com.games.minesweeper.dto.response;

import com.games.minesweeper.dto.GameStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameResponse {
    private CellResponse[][] cellResponse;
    private GameStatus gameStatus;
}
