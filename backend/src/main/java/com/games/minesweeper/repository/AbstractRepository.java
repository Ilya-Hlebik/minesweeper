package com.games.minesweeper.repository;


import com.games.minesweeper.dbo.AbstractEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface AbstractRepository<EntityClass extends AbstractEntity> extends MongoRepository<EntityClass, String>
{
}
