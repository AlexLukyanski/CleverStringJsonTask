package by.clever.stringtask.service.impl;

import by.clever.stringtask.entity.PokemonGang;
import by.clever.stringtask.entity.PokemonRegister;
import by.clever.stringtask.entity.constant.RegisterStatus;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public class PokemonRegisterTestData {

    @Builder.Default
    UUID id = UUID.fromString("8b2b030a-a897-4fcf-b864-5f71128bee99");

    @Builder.Default
    Set<PokemonGang> gangsSet = Set.of(PokemonGangTestData.builder().build().getPokemonGang());

    @Builder.Default
    RegisterStatus status = RegisterStatus.BADASS;

    public PokemonRegister getPokemonRegister() {
        PokemonRegister pokemonRegister = new PokemonRegister(id, gangsSet, status);
        return pokemonRegister;
    }
}
