package by.clever.stringtask.service.impl;

import by.clever.stringtask.entity.Pokemon;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public class PokemonTestData {

    @Builder.Default
    UUID id = UUID.fromString("e2599420-6aa9-4731-be40-783ed9051457");

    @Builder.Default
    String name = "Pika";

    @Builder.Default
    LocalDate birthDate = LocalDate.parse("2023-10-29");

    @Builder.Default
    Double value = 22.0;

    public Pokemon getPokemon() {
        Pokemon pokemon = new Pokemon(id, name, birthDate, value);
        return pokemon;
    }
}
