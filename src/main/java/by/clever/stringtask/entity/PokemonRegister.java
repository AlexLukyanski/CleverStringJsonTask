package by.clever.stringtask.entity;

import by.clever.stringtask.entity.constant.RegisterStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PokemonRegister {

    UUID id;
    Set<PokemonGang> gangsSet;
    RegisterStatus status;
}
