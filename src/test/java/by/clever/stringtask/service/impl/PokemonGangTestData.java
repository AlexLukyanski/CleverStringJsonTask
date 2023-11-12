package by.clever.stringtask.service.impl;

import by.clever.stringtask.entity.Pokemon;
import by.clever.stringtask.entity.PokemonGang;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public class PokemonGangTestData {

    @Builder.Default
    UUID id = UUID.fromString("cd0f1d08-b411-495d-81a0-fdbaf403e8ae");

    @Builder.Default
    List<Pokemon> members = List.of(PokemonTestData.builder().build().getPokemon());

    @Builder.Default
    OffsetDateTime creationDate = OffsetDateTime.MAX;

    @Builder.Default
    int rating = 1;

    public PokemonGang getPokemonGang() {
        PokemonGang pokemonGang = new PokemonGang(id, members, creationDate, rating);
        return pokemonGang;
    }
}

