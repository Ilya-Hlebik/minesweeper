package com.games.minesweeper.dbo;

import com.games.minesweeper.dto.CellType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CELL")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Cell extends AbstractEntity {
    private int row;
    private int column;
    private CellType cellType;
    private boolean hidden = true;
    private int number;
    private boolean flagged = false;
    private boolean highlighted = false;

    public Cell(int row, int column, CellType cellType) {
        this.row = row;
        this.column = column;
        this.cellType = cellType;
    }

    public boolean isBomb() {
        return cellType == CellType.BOMB;
    }

    public boolean isBlank() {
        return cellType.equals(CellType.BLANK);
    }
}
