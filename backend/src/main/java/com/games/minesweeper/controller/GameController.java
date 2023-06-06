package com.games.minesweeper.controller;

import com.games.minesweeper.dto.request.BoardInitiateRequest;
import com.games.minesweeper.dto.request.MinesCountRequest;
import com.games.minesweeper.dto.response.CellResponse;
import com.games.minesweeper.dto.response.MinesCountResponse;
import com.games.minesweeper.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
