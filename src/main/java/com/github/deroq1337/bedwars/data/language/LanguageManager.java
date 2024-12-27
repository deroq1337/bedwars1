package com.github.deroq1337.bedwars.data.language;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface LanguageManager {

    @NotNull CompletableFuture<Void> loadMessages();

    void clearMessages();

    @NotNull String getMessage(@NotNull Locale locale, @NotNull String key);

    @NotNull Set<Locale> getLocales();
}
