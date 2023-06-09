package com.games.minesweeper.service;

import com.games.minesweeper.dto.Board;
import com.games.minesweeper.dto.Cell;
import com.games.minesweeper.dto.CellType;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Iterator;

@Service
public class BoardService {

    public void init(int i, int j, Board board) {
        initializeBoard(board);
        shuffleBoard(i, j, board);
        setNumberedCells(board);
        board.setInitialized(true);
    }

    private void initializeBoard(Board board) {
        int tempBombs = 0;
        for (int i = 0; i < board.getNRows(); i++) {
            for (int j = 0; j < board.getNColumns(); j++) {
                if (tempBombs != board.getNBombs()) {
                    board.getShuffledCells().add(new Cell(i, j, CellType.BOMB));
                    tempBombs++;
                } else {
                    board.getShuffledCells().add(new Cell(i, j, CellType.BLANK));
                }
            }
        }
    }

    private void shuffleBoard(int rowToEscape, int columnToEscape, Board board) {
        do {
            Collections.shuffle(board.getShuffledCells());
        } while (board.getShuffledCells().get(rowToEscape * board.getNRows() + columnToEscape).isBomb());
        Iterator<Cell> iterator = board.getShuffledCells().iterator();
        for (int i = 0; i < board.getNRows(); i++) {
            for (int j = 0; j < board.getNColumns(); j++) {
                board.getCells()[i][j] = iterator.next(); //?
                board.getCells()[i][j].setColumn(i);
                board.getCells()[i][j].setRow(j);
            }
        }
    }

    private void setNumberedCells(Board board) {
        for (int i = 0; i < board.getNRows(); i++) {
            for (int j = 0; j < board.getNColumns(); j++) {
                Cell cell = board.getCells()[i][j];
                if (cell.isBomb()) {
                    continue;
                }
                int numberOfBombsAround = numberOfBombsAround(i, j, board);
                if (numberOfBombsAround > 0) {
                    cell.setCellType(CellType.NUMBER);
                    cell.setNumber(numberOfBombsAround);
                }
            }
        }
    }

    private int numberOfBombsAround(int i, int j, Board board) {
        int numberOfBombs = 0;
        if (i - 1 >= 0 && j - 1 >= 0) {
            //checkTopLeftDiagonal
            numberOfBombs = updateNumberOfBombs(numberOfBombs, i - 1, j - 1, board);
        }
        if (i - 1 >= 0) {
            //checkTop
            numberOfBombs = updateNumberOfBombs(numberOfBombs, i - 1, j, board);
        }
        if (i - 1 >= 0 && j + 1 < board.getNColumns()) {
            //checkTopRightDiagonal
            numberOfBombs = updateNumberOfBombs(numberOfBombs, i - 1, j + 1, board);
        }
        if (j + 1 < board.getNColumns()) {
            //checkRight
            numberOfBombs = updateNumberOfBombs(numberOfBombs, i, j + 1, board);
        }
        if (i + 1 < board.getNRows() && j + 1 < board.getNColumns()) {
            //checkBottomRightDiagonal
            numberOfBombs = updateNumberOfBombs(numberOfBombs, i + 1, j + 1, board);
        }
        if (i + 1 < board.getNRows()) {
            //checkBottom
            numberOfBombs = updateNumberOfBombs(numberOfBombs, i + 1, j, board);
        }
        if (i + 1 < board.getNRows() && j - 1 >= 0) {
            //checkLeftBottomDiagonal
            numberOfBombs = updateNumberOfBombs(numberOfBombs, i + 1, j - 1, board);
        }
        if (j - 1 >= 0) {
            //checkLeft
            numberOfBombs = updateNumberOfBombs(numberOfBombs, i, j - 1, board);
        }
        return numberOfBombs;
    }

    private int updateNumberOfBombs(int numberOfBombs, int i, int j, Board board) {
        Cell cell = board.getCells()[i][j];
        if (cell.isBomb()) {
            numberOfBombs++;
        }
        return numberOfBombs;
    }

    public int getCurrentCountOfFlags(Board board) {
        int value = 0;
        for (int i = 0; i < board.getNRows(); i++) {
            for (int j = 0; j < board.getNColumns(); j++) {
                if (board.getCells()[i][j].isFlagged()) {
                    value++;
                }
            }
        }
        return value;
    }

    public int updateUnrevealedAmount(Board board) {
        int value = -board.getNBombs();
        for (int i = 0; i < board.getNRows(); i++) {
            for (int j = 0; j < board.getNColumns(); j++) {
                if (board.getCells()[i][j].isHidden()) {
                    value++;
                }
            }
        }
        board.setNumUnexposedRemaining(value);
        return Math.max(value, 0);
    }
}
