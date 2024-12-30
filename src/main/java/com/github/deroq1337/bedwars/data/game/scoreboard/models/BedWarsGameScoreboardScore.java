package com.github.deroq1337.bedwars.data.game.scoreboard.models;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class BedWarsGameScoreboardScore {

    private final @NotNull String name;
    private final @NotNull String teamName;
    private final @NotNull String value;
}
