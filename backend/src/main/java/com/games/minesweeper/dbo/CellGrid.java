package com.games.minesweeper.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "CELL_GRID")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CellGrid extends AbstractEntity {
    private List<List<Cell>> cells;
}
