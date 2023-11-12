package by.clever.stringtask.service.impl;

import by.clever.stringtask.entity.PokemonGang;
import by.clever.stringtask.entity.PokemonRegister;
import by.clever.stringtask.service.DateTimeConverter;
import by.clever.stringtask.service.JsonOperator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class JsonOperatorImplTest {

    @Nested
    class ToJsonMethodTest {

        @Test
        void should_ReturnJsonString_when_MethodInvokes() throws JsonProcessingException {

            //given
            PokemonRegister pokemonRegister = PokemonRegisterTestData.builder().build().getPokemonRegister();
            DateTimeConverter dateTimeConverter = new DateTimeConverterImpl();
            JsonOperator jsonOperator = new JsonOperatorImpl();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String expected = objectMapper.writeValueAsString(pokemonRegister);

            //when
            String temp = jsonOperator.toJson(pokemonRegister).replace("\n", "").replace(" ", "");
            String actual = dateTimeConverter.convertFromJson(temp);

            //then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class FromJsonMethodTest {

        @Test
        void should_ReturnObjectFromJson_when_MethodInvokes() throws JsonProcessingException {

            //given
            PokemonGang pokemonGang = PokemonGangTestData.builder().build().getPokemonGang();
            DateTimeConverter dateTimeConverter = new DateTimeConverterImpl();
            JsonOperator jsonOperator = new JsonOperatorImpl();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String json = jsonOperator.toJson(pokemonGang).replace("\n", "").replace(" ", "");
            String jsonCorrect = dateTimeConverter.convertFromJson(json);
            PokemonGang expected = (PokemonGang) jsonOperator.fromJson(json, PokemonGang.class);

            //when
            PokemonGang actual = objectMapper.readValue(jsonCorrect, PokemonGang.class);

            //then
            assertEquals(expected, actual);
        }
    }
}