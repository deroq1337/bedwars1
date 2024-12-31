package com.github.deroq1337.bedwars.data.game.scoreboard.models;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BedWarsGameScoreboardScore {

    private final Optional<String> name;
    private final @NotNull String teamName;
    private final @NotNull String value;
    private final boolean freeSpace;
    private Optional<String> entry;

    public BedWarsGameScoreboardScore(@NotNull String teamName, @NotNull String value, boolean freeSpace) {
        this(Optional.empty(), teamName, value, freeSpace);
    }
}
