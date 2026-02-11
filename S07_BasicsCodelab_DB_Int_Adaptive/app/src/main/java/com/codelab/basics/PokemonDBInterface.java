package com.codelab.basics;

import java.util.List;

public interface PokemonDBInterface {

    public int count();

    public int save(Pokemon pokemon);

    public int update(Pokemon pokemon);

    public int deleteById(Long id);

    public List<Pokemon> findAll();

    public String getNameById(Long id);

    public Pokemon getMaxPower();

    public void incAccessCount(long id);

    public long getMostAccessedId();
}