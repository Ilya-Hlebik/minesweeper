package com.games.minesweeper.controller;

import com.games.minesweeper.dto.request.BoardInitiateRequest;
import com.games.minesweeper.dto.request.MinesCountRequest;
import com.games.minesweeper.dto.request.RevealCellRequest;
import com.games.minesweeper.dto.request.SetFlaggedRequest;
import com.games.minesweeper.dto.response.CellResponse;
import com.games.minesweeper.dto.response.GameResponse;
import com.games.minesweeper.dto.response.MinesCountResponse;
import com.games.minesweeper.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping(value = "/minesCount")
    public MinesCountResponse getMinesCount(@RequestBody MinesCountRequest minesCountRequest) {
        return new MinesCountResponse(1, minesCountRequest.getRows() * minesCountRequest.getColumns() / 6);
    }

    @PostMapping(value = "/initiate")
    public CellResponse[][] initiateBoard(@RequestBody BoardInitiateRequest minesCountRequest) {
        return gameService.initiateBoard(minesCountRequest);
    }

    @PostMapping(value = "/revealCell")
    public GameResponse revealCell(@RequestBody RevealCellRequest revealCellRequest) {
        return gameService.revealCell(revealCellRequest);
    }

    @PatchMapping(value = "/setFlagged")
    public void setFlagged(@RequestBody SetFlaggedRequest setFlaggedRequest) {
          gameService.setFlagged(setFlaggedRequest);
    }

    @PostMapping(value = "/showAll")
    public GameResponse showAll() {
        return gameService.showAll();
    }
}
