package by.clever.stringtask;

import by.clever.stringtask.entity.Pokemon;
import by.clever.stringtask.entity.PokemonGang;
import by.clever.stringtask.entity.PokemonRegister;
import by.clever.stringtask.entity.constant.RegisterStatus;
import by.clever.stringtask.service.JsonOperator;
import by.clever.stringtask.service.impl.JsonOperatorImpl;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        Pokemon pokemon1 = new Pokemon();
        pokemon1.setId(UUID.randomUUID());
        pokemon1.setName("Pika");
        pokemon1.setValue(Double.MAX_VALUE);
        pokemon1.setBirthDate(LocalDate.now());

        Pokemon pokemon2 = new Pokemon();
        pokemon2.setId(UUID.randomUUID());
        pokemon2.setName("Chu");
        pokemon2.setValue(Double.MIN_VALUE);
        pokemon2.setBirthDate(LocalDate.now());

        Pokemon pokemon3 = new Pokemon();
        pokemon3.setId(UUID.randomUUID());
        pokemon3.setName("Meow");
        pokemon3.setValue(Double.MAX_VALUE);
        pokemon3.setBirthDate(LocalDate.now());

        Pokemon pokemon4 = new Pokemon();
        pokemon4.setId(UUID.randomUUID());
        pokemon4.setName("U2");
        pokemon4.setValue(Double.MAX_VALUE);
        pokemon4.setBirthDate(LocalDate.now());


        PokemonGang gang1 = new PokemonGang();
        gang1.setId(UUID.randomUUID());
        gang1.setRating(1);
        gang1.setMembers(List.of(pokemon1, pokemon2));
        gang1.setCreationDate(OffsetDateTime.now());

        PokemonGang gang2 = new PokemonGang();
        gang2.setId(UUID.randomUUID());
        gang2.setRating(3);
        gang2.setMembers(List.of(pokemon3, pokemon4));
        gang2.setCreationDate(OffsetDateTime.now());

        PokemonRegister register = new PokemonRegister();
        register.setId(UUID.randomUUID());
        register.setGangsSet(Set.of(gang1, gang2));
        register.setStatus(RegisterStatus.BADASS);


        JsonOperator operator = new JsonOperatorImpl();
//        String json = operator.toJson(pokemon1);
//        System.out.println(json);

//        String json2 = operator.toJson(gang1);
//        System.out.println(json2);

        String json3 = operator.toJson(register);
        System.out.println(json3);


    }
}