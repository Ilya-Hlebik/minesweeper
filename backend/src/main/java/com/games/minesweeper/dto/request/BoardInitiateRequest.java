package com.games.minesweeper.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BoardInitiateRequest extends MinesCountRequest {
    private Integer mines;
}
