package com.games.minesweeper.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RevealCellRequest extends GameIdRequest {
    private Integer row;
    private Integer column;
}
