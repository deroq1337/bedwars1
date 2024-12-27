package com.github.deroq1337.bedwars.data.game.exceptions;

import org.jetbrains.annotations.NotNull;

public class GameVotingWinnerDeterminationException extends RuntimeException {

    public GameVotingWinnerDeterminationException(@NotNull String message) {
        super(message);
    }
}
