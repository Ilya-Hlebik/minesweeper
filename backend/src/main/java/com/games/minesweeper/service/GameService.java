package com.games.minesweeper.service;

import com.games.minesweeper.dto.Board;
import com.games.minesweeper.dto.request.BoardInitiateRequest;
import com.games.minesweeper.dto.response.CellResponse;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    public static Board board;

    public CellResponse[][] initiateBoard(BoardInitiateRequest minesCountRequest) {
        board = new Board(minesCountRequest.getRows(), minesCountRequest.getColumns(), minesCountRequest.getMines());
        CellResponse cellResponse[][] = new CellResponse[minesCountRequest.getRows()][minesCountRequest.getColumns()];
        for (int i = 0; i < minesCountRequest.getRows(); i++) {
            for (int j = 0; j < minesCountRequest.getColumns(); j++) {
                cellResponse[i][j] = new CellResponse();
            }
        }
        return cellResponse;
    }
}
