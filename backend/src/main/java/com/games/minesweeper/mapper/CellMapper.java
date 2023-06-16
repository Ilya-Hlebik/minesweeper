package com.games.minesweeper.mapper;

import com.games.minesweeper.dbo.Cell;
import com.games.minesweeper.dto.response.CellResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CellMapper {
    public CellResponse[][] cellToCellResponse(List<List<Cell>> cells) {
        CellResponse[][] cellResponse = new CellResponse[cells.size()][cells.get(0).size()];
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < cells.get(i).size(); j++) {
                boolean revealed = !cells.get(i).get(j).isHidden();
                boolean flagged = cells.get(i).get(j).isFlagged();
                boolean highlighted = cells.get(i).get(j).isHighlighted();
                if (revealed) {
                    boolean bomb = cells.get(i).get(j).isBomb();
                    int value = cells.get(i).get(j).getNumber();
                    String content = value == 0 ? "" : String.valueOf(value);
                    cellResponse[i][j] = new CellResponse(bomb, true, flagged, content, highlighted);
                } else {
                    cellResponse[i][j] = new CellResponse(false, false, flagged, "", highlighted);
                }
            }
        }
        return cellResponse;
    }
}
