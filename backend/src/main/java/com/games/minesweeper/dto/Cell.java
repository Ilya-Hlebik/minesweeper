package com.games.minesweeper.dto;

import lombok.Data;

@Data
public class Cell {
    private int row;
    private int column;
    private CellType cellType;
    private boolean hidden = true;
    private int number;
    private boolean flagged = false;

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
