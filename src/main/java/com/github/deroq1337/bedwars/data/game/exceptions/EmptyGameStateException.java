package com.github.deroq1337.bedwars.data.game.exceptions;

import org.jetbrains.annotations.NotNull;

public class EmptyGameStateException extends NullPointerException {

    public EmptyGameStateException(@NotNull String message) {
        super(message);
    }
}