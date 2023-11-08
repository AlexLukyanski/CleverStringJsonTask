package by.clever.stringtask.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PokemonGang {

    UUID id;
    List<Pokemon> members;
    OffsetDateTime creationDate;
    int rating;
}
