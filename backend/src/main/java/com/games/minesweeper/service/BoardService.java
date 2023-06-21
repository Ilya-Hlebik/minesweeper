package com.games.minesweeper.service;

import com.games.minesweeper.dbo.Board;
import com.games.minesweeper.dbo.Cell;
import com.games.minesweeper.dbo.CellGrid;
import com.games.minesweeper.dto.CellType;
import com.games.minesweeper.repository.BoardRepository;
import com.games.minesweeper.repository.CellGridRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final CellGridRepository cellGridRepository;
    private final BoardRepository boardRepository;

    public void init(int i, int j, Board board) {
        initializeBoard(board);
        shuffleBoard(i, j, board);
        setNumberedCells(board);
        setInitialized(board);
    }

    public int getCurrentCountOfFlags(Board board) {
        return (int) board.getCellGrid()
                .getCells()
                .stream()
                .flatMap(Collection::stream)
                .filter(Cell::isFlagged)
                .count();
    }

    public int updateUnrevealedAmount(Board board) {
        int value = (int) (board.getCellGrid()
                .getCells()
                .stream()
                .flatMap(Collection::stream)
                .filter(Cell::isHidden)
                .count() - board.getNBombs());
        board.setNumUnexposedRemaining(value);
        return Math.max(value, 0);
    }


    public int getFlagsCountAround(Board board, int i, int j) {
        return numberOfCellsByTypeAround(i, j, board, Cell::isFlagged);
    }

    public void saveBoardAndCells(Board board) {
        cellGridRepository.save(board.getCellGrid());
        boardRepository.save(board);
    }

    private void setInitialized(Board board) {
        board.setInitialized(true);
        boardRepository.save(board);
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
        CellGrid cellGrid = new CellGrid();
        List<List<Cell>> cells = new ArrayList<>();
        for (int i = 0; i < board.getNRows(); i++) {
            List<Cell> row = new ArrayList<>();
            for (int j = 0; j < board.getNColumns(); j++) {
                Cell next = iterator.next();
                row.add(next);
                next.setRow(i);
                next.setColumn(j);
            }
            cells.add(row);
        }
        cellGrid.setCells(cells);
        board.setCellGrid(cellGrid);
    }

    private void setNumberedCells(Board board) {
        for (int i = 0; i < board.getNRows(); i++) {
            for (int j = 0; j < board.getNColumns(); j++) {
                Cell cell = board.getCellGrid().getCells().get(i).get(j);
                if (cell.isBomb()) {
                    continue;
                }
                int numberOfBombsAround = numberOfCellsByTypeAround(i, j, board, Cell::isBomb);
                if (numberOfBombsAround > 0) {
                    cell.setCellType(CellType.NUMBER);
                    cell.setNumber(numberOfBombsAround);
                }
            }
        }
        cellGridRepository.save(board.getCellGrid());
    }

    private int numberOfCellsByTypeAround(int i, int j, Board board, Function<Cell, Boolean> cellTypeFunction) {
        int numberOfCellsByType = 0;
        if (i - 1 >= 0 && j - 1 >= 0) {
            //checkTopLeftDiagonal
            numberOfCellsByType = updateNumberOfCellsByType(numberOfCellsByType, i - 1, j - 1, board, cellTypeFunction);
        }
        if (i - 1 >= 0) {
            //checkTop
            numberOfCellsByType = updateNumberOfCellsByType(numberOfCellsByType, i - 1, j, board, cellTypeFunction);
        }
        if (i - 1 >= 0 && j + 1 < board.getNColumns()) {
            //checkTopRightDiagonal
            numberOfCellsByType = updateNumberOfCellsByType(numberOfCellsByType, i - 1, j + 1, board, cellTypeFunction);
        }
        if (j + 1 < board.getNColumns()) {
            //checkRight
            numberOfCellsByType = updateNumberOfCellsByType(numberOfCellsByType, i, j + 1, board, cellTypeFunction);
        }
        if (i + 1 < board.getNRows() && j + 1 < board.getNColumns()) {
            //checkBottomRightDiagonal
            numberOfCellsByType = updateNumberOfCellsByType(numberOfCellsByType, i + 1, j + 1, board, cellTypeFunction);
        }
        if (i + 1 < board.getNRows()) {
            //checkBottom
            numberOfCellsByType = updateNumberOfCellsByType(numberOfCellsByType, i + 1, j, board, cellTypeFunction);
        }
        if (i + 1 < board.getNRows() && j - 1 >= 0) {
            //checkLeftBottomDiagonal
            numberOfCellsByType = updateNumberOfCellsByType(numberOfCellsByType, i + 1, j - 1, board, cellTypeFunction);
        }
        if (j - 1 >= 0) {
            //checkLeft
            numberOfCellsByType = updateNumberOfCellsByType(numberOfCellsByType, i, j - 1, board, cellTypeFunction);
        }
        return numberOfCellsByType;
    }

    private int updateNumberOfCellsByType(int numberOfBombs, int i, int j, Board board, Function<Cell, Boolean> cellTypeFunction) {
        Cell cell = board.getCellGrid().getCells().get(i).get(j);
        if (cellTypeFunction.apply(cell)) {
            numberOfBombs++;
        }
        return numberOfBombs;
    }
}
