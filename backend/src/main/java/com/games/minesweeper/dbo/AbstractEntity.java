package com.games.minesweeper.dbo;


import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class AbstractEntity {
    @Id
    private String id;
}
