package com.games.minesweeper.controller;

import com.games.minesweeper.dto.request.MinesCountRequest;
import com.games.minesweeper.dto.response.MinesCountResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {
    @GetMapping(value = "/get")
    public void get() {
        System.out.println("HERE!!!!");
    }
    @PostMapping(value = "/minesCount")
    public MinesCountResponse getMinesCount(@RequestBody MinesCountRequest minesCountRequest) {
        return new MinesCountResponse(1,4);
    }
}
