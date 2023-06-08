package com.games.minesweeper.service;

import com.games.minesweeper.dto.Board;
import com.games.minesweeper.dto.Cell;
import com.games.minesweeper.dto.CellType;
import com.games.minesweeper.dto.GameStatus;
import com.games.minesweeper.dto.request.BoardInitiateRequest;
import com.games.minesweeper.dto.request.RevealCellRequest;
import com.games.minesweeper.dto.request.SetFlaggedRequest;
import com.games.minesweeper.dto.response.CellResponse;
import com.games.minesweeper.dto.response.GameResponse;
import com.games.minesweeper.mapper.CellMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class GameService {
    private final BoardService boardService;
    private final CellMapper cellMapper;

    public static Board board;
    private GameStatus gameStatus;

    public CellResponse[][] initiateBoard(BoardInitiateRequest minesCountRequest) {
        board = new Board(minesCountRequest.getRows(), minesCountRequest.getColumns(), minesCountRequest.getMines());
        gameStatus = GameStatus.IN_PLAY;
        board.setCells(new Cell[minesCountRequest.getRows()][minesCountRequest.getColumns()]);
        CellResponse[][] cellResponse = new CellResponse[minesCountRequest.getRows()][minesCountRequest.getColumns()];
        for (int i = 0; i < minesCountRequest.getRows(); i++) {
            for (int j = 0; j < minesCountRequest.getColumns(); j++) {
                cellResponse[i][j] = new CellResponse();
            }
        }
        return cellResponse;
    }

    public GameResponse revealCell(RevealCellRequest revealCellRequest) {
        openCell(revealCellRequest.getRow(), revealCellRequest.getColumn());
        return GameResponse
                .builder()
                .gameStatus(gameStatus)
                .cellResponse(cellMapper.cellToCellResponse(board.getCells()))
                .build();
    }

    public void setFlagged(SetFlaggedRequest setFlaggedRequest) {
        board.getCells()[setFlaggedRequest.getRow()][setFlaggedRequest.getColumn()].setFlagged(setFlaggedRequest.isFlagged());
    }

    public GameResponse showAll() {
        for (int i = 0; i < board.getNRows(); i++) {
            for (int j = 0; j < board.getNColumns(); j++) {
                board.getCells()[i][j].setHidden(false);
            }
        }
        return GameResponse
                .builder()
                .gameStatus(gameStatus)
                .cellResponse(cellMapper.cellToCellResponse(board.getCells()))
                .build();
    }

    private void openCell(int i, int j) {
        if (i < 0 || i >= board.getNRows() || j < 0 || j >= board.getNColumns() || (board.isInitialized() && (!board.getCells()[i][j].isHidden() || board.getCells()[i][j].isFlagged()))) {
            System.out.println("Out of range or already open");
            return;
        }
        if (!board.isInitialized()) {
            boardService.init(i, j, board);
        }
        Cell cell = board.getCells()[i][j];
        if (cell.isBomb()) {
            cell.setHidden(false);
            System.out.println("You hit the bomb!");
            gameStatus = GameStatus.LOSE;
        } else if (cell.getCellType() == CellType.NUMBER) {
            cell.setHidden(false);
            System.out.println("Open " + i + " " + j);
            board.decreaseNumUnexposedRemaining();
        } else {
            openAllBlankCells(board, cell);
        }
        if (board.getNumUnexposedRemaining() == 0) {
            gameStatus = GameStatus.WIN;
        }
    }

    private void openAllBlankCells(Board board, Cell cell) {
        Queue<Cell> cells = new LinkedList<>();
        List<Cell> openedCells = new ArrayList<>();
        cells.add(cell);
        openedCells.add(cell);
        while (!cells.isEmpty()) {
            Cell poll = cells.poll();
            int i = poll.getColumn();
            int j = poll.getRow();
            poll.setHidden(false);
            if (poll.isBlank()) {
                checkOtherSpots(board, i, j, cells, openedCells);
            }
        }
        openedCells.stream().distinct().filter(cell1 -> !cell1.isHidden()).forEach(cell1 -> board.decreaseNumUnexposedRemaining());
    }

    private void checkOtherSpots(Board board, int i, int j, Queue<Cell> cells, List<Cell> openedCells) {
        if (i - 1 >= 0) {
            //checkTop
            checkCellOnBlank(board, i - 1, j, cells, openedCells);
        }
        if (j + 1 < board.getNColumns()) {
            //checkRight
            checkCellOnBlank(board, i, j + 1, cells, openedCells);
        }
        if (i + 1 < board.getNRows()) {
            //checkBottom
            checkCellOnBlank(board, i + 1, j, cells, openedCells);
        }
        if (j - 1 >= 0) {
            //checkLeft
            checkCellOnBlank(board, i, j - 1, cells, openedCells);
        }
    }

    private void checkCellOnBlank(Board board, int i, int j, Queue<Cell> cells, List<Cell> openedCells) {
        Cell cell = board.getCells()[i][j];
        if (!openedCells.contains(cell) && (cell.isBlank() || !cell.isBomb())) {
            cells.add(cell);
            openedCells.add(cell);
        }
    }
}
