package com.github.deroq1337.bedwars.data.game.exceptions;

import org.jetbrains.annotations.NotNull;

public class GameVotingInitializationException extends RuntimeException {

    public GameVotingInitializationException(@NotNull String message) {
        super(message);
    }
}
