package com.games.minesweeper.controller;

import com.games.minesweeper.dto.request.*;
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
    public GameResponse initiateBoard(@RequestBody BoardInitiateRequest minesCountRequest) {
        return gameService.initiateBoard(minesCountRequest);
    }

    @GetMapping(value = "/{gameId}")
    public GameResponse getGameById(@PathVariable String gameId) {
        return gameService.getGameById(gameId);
    }

    @PostMapping(value = "/revealCell")
    public GameResponse revealCell(@RequestBody RevealCellRequest revealCellRequest) {
        return gameService.revealCell(revealCellRequest);
    }

    @PatchMapping(value = "/setFlagged")
    public int setFlagged(@RequestBody SetFlaggedRequest setFlaggedRequest) {
        return gameService.setFlagged(setFlaggedRequest);
    }

    @PostMapping(value = "/showAll")
    public GameResponse showAll(@RequestBody GameIdRequest gameIdRequest) {
        return gameService.showAll(gameIdRequest);
    }

    @PostMapping(value = "/showCellsOptions")
    public GameResponse showCellsOptions(@RequestBody RevealCellRequest revealCellRequest) {
        return gameService.showCellsOptions(revealCellRequest);
    }
}
