package com.games.minesweeper.service;

import com.games.minesweeper.dbo.Board;
import com.games.minesweeper.dbo.Cell;
import com.games.minesweeper.dto.CellType;
import com.games.minesweeper.dto.GameStatus;
import com.games.minesweeper.dto.request.BoardInitiateRequest;
import com.games.minesweeper.dto.request.GameIdRequest;
import com.games.minesweeper.dto.request.RevealCellRequest;
import com.games.minesweeper.dto.request.SetFlaggedRequest;
import com.games.minesweeper.dto.response.CellResponse;
import com.games.minesweeper.dto.response.GameResponse;
import com.games.minesweeper.mapper.CellMapper;
import com.games.minesweeper.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class GameService {
    private final BoardService boardService;
    private final CellMapper cellMapper;
    private final BoardRepository boardRepository;

    @Transactional
    public GameResponse initiateBoard(BoardInitiateRequest minesCountRequest) {
        Board boardToSave = new Board(minesCountRequest.getRows(), minesCountRequest.getColumns(), minesCountRequest.getMines());
        Board saveBoard = boardRepository.save(boardToSave);
        return getEmptyGameResponse(saveBoard);
    }

    @Transactional
    public GameResponse revealCell(RevealCellRequest revealCellRequest) {
        Board board = boardRepository.findById(revealCellRequest.getGameId()).orElseThrow();
        openCell(revealCellRequest.getRow(), revealCellRequest.getColumn(), board);
        int currentCountOfFlags = boardService.getCurrentCountOfFlags(board);
        int unrevealedAmount = boardService.updateUnrevealedAmount(board);
        if (board.getNumUnexposedRemaining() == 0) {
            board.setGameStatus(GameStatus.WIN);
        }
        boardService.saveBoardAndCells(board);
        return getGameResponse(board, currentCountOfFlags, unrevealedAmount);
    }

    @Transactional
    public int setFlagged(SetFlaggedRequest setFlaggedRequest) {
        Board board = boardRepository.findById(setFlaggedRequest.getGameId()).orElseThrow();
        board.getCellGrid().getCells().get(setFlaggedRequest.getRow()).get(setFlaggedRequest.getColumn()).setFlagged(setFlaggedRequest.isFlagged());
        boardService.saveBoardAndCells(board);
        return boardService.getCurrentCountOfFlags(board);
    }

    @Transactional
    public GameResponse showAll(GameIdRequest gameIdRequest) {
        Board board = boardRepository.findById(gameIdRequest.getGameId()).orElseThrow();
        for (int i = 0; i < board.getNRows(); i++) {
            for (int j = 0; j < board.getNColumns(); j++) {
                board.getCellGrid().getCells().get(i).get(j).setHidden(false);
            }
        }
        GameResponse defaultGameResponse = getDefaultGameResponse(board);
        boardService.saveBoardAndCells(board);
        return defaultGameResponse;
    }

    @Transactional
    public GameResponse showCellsOptions(RevealCellRequest revealCellRequest) {
        Board board = boardRepository.findById(revealCellRequest.getGameId()).orElseThrow();
        int i = revealCellRequest.getRow();
        int j = revealCellRequest.getColumn();
        if (i < 0 || i >= board.getNRows() || j < 0 || j >= board.getNColumns()
                || (board.isInitialized() && (board.getCellGrid().getCells().get(i).get(j).isHidden()
                || board.getCellGrid().getCells().get(i).get(j).isFlagged()
                || board.getCellGrid().getCells().get(i).get(j).getCellType() != CellType.NUMBER))) {
            System.out.println("Out of range, not revealed, flagged, or not a number");
            return getDefaultGameResponse(board);
        }
        Cell cell = board.getCellGrid().getCells().get(i).get(j);
        int number = cell.getNumber();
        int flagsCount = boardService.getFlagsCountAround(board, i, j);
        if (number == flagsCount) {
            for (int di = -1; di <= 1; di++) {
                for (int dj = -1; dj <= 1; dj++) {
                    if (di == 0 && dj == 0) {
                        continue; // Skip the current cell
                    }
                    openCell(i + di, j + dj, board);
                }
            }
        } else {
            checkOtherSpots(board, i, j, (argI, argJ) -> updateHighlightForUnrevealedCell(board.getCellGrid().getCells().get(argI).get(argJ)));
        }
        if (board.getNumUnexposedRemaining() == 0 && board.getGameStatus() != GameStatus.LOSE) {
            board.setGameStatus(GameStatus.WIN);
        }
        GameResponse defaultGameResponse = getDefaultGameResponse(board);
        checkOtherSpots(board, i, j, (argI, argJ) -> updateHighlightForUnrevealedCell(board.getCellGrid().getCells().get(argI).get(argJ)));
        boardService.saveBoardAndCells(board);
        return defaultGameResponse;
    }

    public void updateHighlightForUnrevealedCell(Cell cell) {
        if (cell.isHidden() && !cell.isFlagged()) {
            cell.setHighlighted(!cell.isHighlighted());
        }
    }

    public GameResponse getGameById(String gameId) {
        Board board = boardRepository.findById(gameId).orElseThrow();
        if (board.getCellGrid() == null) {
            return getEmptyGameResponse(board);
        }
        return getDefaultGameResponse(board);
    }

    private GameResponse getEmptyGameResponse(Board board) {
        CellResponse[][] cellResponse = new CellResponse[board.getNRows()][board.getNColumns()];
        for (int i = 0; i < board.getNRows(); i++) {
            for (int j = 0; j < board.getNColumns(); j++) {
                cellResponse[i][j] = new CellResponse();
            }
        }
        return GameResponse
                .builder()
                .cellResponse(cellResponse)
                .gameId(board.getId())
                .build();
    }

    private void openCell(int i, int j, Board board) {
        if (i < 0 || i >= board.getNRows() || j < 0 || j >= board.getNColumns()
                || (board.isInitialized() && (!board.getCellGrid().getCells().get(i).get(j).isHidden()
                || board.getCellGrid().getCells().get(i).get(j).isFlagged()))) {
            System.out.println("Out of range or already open");
            return;
        }
        if (!board.isInitialized()) {
            boardService.init(i, j, board);
        }
        Cell cell = board.getCellGrid().getCells().get(i).get(j);
        if (cell.isBomb()) {
            cell.setHidden(false);
            System.out.println("You hit the bomb!");
            board.setGameStatus(GameStatus.LOSE);
        } else if (cell.getCellType() == CellType.NUMBER) {
            cell.setHidden(false);
            System.out.println("Open " + i + " " + j);
            board.decreaseNumUnexposedRemaining();
        } else {
            openAllBlankCells(board, cell);
        }
    }

    private void openAllBlankCells(Board board, Cell cell) {
        List<Cell> openedCells = new ArrayList<>();
        openAllBlankCells(board, openedCells, cell.getColumn(), cell.getRow());
        openedCells.stream().distinct().filter(cell1 -> !cell1.isHidden()).forEach(cell1 -> board.decreaseNumUnexposedRemaining());
    }

    private void openAllBlankCells(Board board, List<Cell> openedCells, int i, int j) {
        if (i < 0 || j < 0 || j > board.getNColumns() - 1 || i > board.getNRows() - 1) {
            return;
        }
        Cell cell = board.getCellGrid().getCells().get(i).get(j);
        if (!(!openedCells.contains(cell) && (cell.isBlank() || !cell.isBomb()))) {
            return;
        }
        cell.setHidden(false);
        openedCells.add(cell);
        if (cell.isBlank()) {
            System.out.println("Open " + i + " " + j);
            openAllBlankCells(board, openedCells, i - 1, j - 1);
            openAllBlankCells(board, openedCells, i - 1, j);
            openAllBlankCells(board, openedCells, i - 1, j + 1);
            openAllBlankCells(board, openedCells, i, j + 1);
            openAllBlankCells(board, openedCells, i + 1, j + 1);
            openAllBlankCells(board, openedCells, i + 1, j);
            openAllBlankCells(board, openedCells, i + 1, j - 1);
            openAllBlankCells(board, openedCells, i, j - 1);
        }
    }

    private void checkOtherSpots(Board board, int i, int j, BiConsumer<Integer, Integer> biConsumer) {
        if (i - 1 >= 0 && j - 1 >= 0) {
            //checkTopLeftDiagonal
            biConsumer.accept(i - 1, j - 1);
        }
        if (i - 1 >= 0) {
            //checkTop
            biConsumer.accept(i - 1, j);
        }
        if (i - 1 >= 0 && j + 1 < board.getNColumns()) {
            //checkTopRightDiagonal
            biConsumer.accept(i - 1, j + 1);
        }
        if (j + 1 < board.getNColumns()) {
            //checkRight
            biConsumer.accept(i, j + 1);
        }
        if (i + 1 < board.getNRows() && j + 1 < board.getNColumns()) {
            //checkBottomRightDiagonal
            biConsumer.accept(i + 1, j + 1);
        }
        if (i + 1 < board.getNRows()) {
            //checkBottom
            biConsumer.accept(i + 1, j);
        }
        if (i + 1 < board.getNRows() && j - 1 >= 0) {
            //checkLeftBottomDiagonal
            biConsumer.accept(i + 1, j - 1);
        }
        if (j - 1 >= 0) {
            //checkLeft
            biConsumer.accept(i, j - 1);
        }
    }

    private GameResponse getDefaultGameResponse(Board board) {
        int currentCountOfFlags = boardService.getCurrentCountOfFlags(board);
        int unrevealedAmount = boardService.updateUnrevealedAmount(board);
        return getGameResponse(board, currentCountOfFlags, unrevealedAmount);
    }

    private GameResponse getGameResponse(Board board, int currentCountOfFlags, int unrevealedAmount) {
        return GameResponse
                .builder()
                .gameStatus(board.getGameStatus())
                .totalCountOfMines(board.getNBombs())
                .currentCountOfFlags(currentCountOfFlags)
                .maximumCountOfFlags(board.getNBombs())
                .unrevealedAmount(unrevealedAmount)
                .gameId(board.getId())
                .cellResponse(cellMapper.cellToCellResponse(board.getCellGrid().getCells()))
                .build();
    }
}
