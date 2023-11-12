package by.clever.stringtask.entity;

import by.clever.stringtask.entity.constant.RegisterStatus;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PokemonRegister {

    UUID id;
    Set<PokemonGang> gangsSet;
    RegisterStatus status;
}
