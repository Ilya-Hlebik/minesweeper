package com.games.minesweeper.mapper;

import com.games.minesweeper.dto.Cell;
import com.games.minesweeper.dto.response.CellResponse;
import org.springframework.stereotype.Component;

@Component
public class CellMapper {
    public CellResponse[][] cellToCellResponse(Cell[][] cells) {
        CellResponse[][] cellResponse = new CellResponse[cells.length][cells[0].length];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                boolean revealed = !cells[i][j].isHidden();
                boolean flagged = cells[i][j].isFlagged();
                if (revealed) {
                    boolean bomb = cells[i][j].isBomb();
                    int value = cells[i][j].getNumber();
                    String content = value == 0 ? "" : String.valueOf(value);
                    cellResponse[i][j] = new CellResponse(bomb, true, flagged, content);
                } else {

                    cellResponse[i][j] = new CellResponse(false, false, flagged, "");
                }
            }
        }
        return cellResponse;
    }
}
