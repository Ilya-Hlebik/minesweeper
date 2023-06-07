package com.games.minesweeper.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Board {
    private int nRows;
    private int nColumns;
    private int nBombs = 0;
    private Cell[][] cells;
    private final List<Cell> shuffledCells = new ArrayList<>();
    private int numUnexposedRemaining;
    private boolean initialized;

    public Board(int nRows, int nColumns, int nBombs) {
        this.nRows = nRows;
        this.nColumns = nColumns;
        this.nBombs = nBombs;
        numUnexposedRemaining = nRows * nColumns - nBombs;
    }

    public void decreaseNumUnexposedRemaining() {
        numUnexposedRemaining--;
    }
}
