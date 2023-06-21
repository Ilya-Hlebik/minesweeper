package com.games.minesweeper.dbo;

import com.games.minesweeper.dto.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "BOARD")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Board extends AbstractEntity {
    private int nRows;
    private int nColumns;
    private int nBombs = 0;
    @DBRef
    private CellGrid cellGrid;
    @Transient
    private final List<Cell> shuffledCells = new ArrayList<>();
    private int numUnexposedRemaining;
    private boolean initialized;
    private GameStatus gameStatus = GameStatus.IN_PLAY;

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

