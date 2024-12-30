package com.github.deroq1337.bedwars.data.language;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultLanguageManager implements LanguageManager {

    private final @NotNull Map<Locale, Translation> localeTranslationMap = new ConcurrentHashMap<>();
    private final @NotNull File messagesFolder;

    public DefaultLanguageManager(@NotNull File messagesFolder) {
        this.messagesFolder = messagesFolder;
        if (!messagesFolder.exists()) {
            messagesFolder.mkdirs();
        }
    }

    @Override
    public @NotNull CompletableFuture<Void> loadMessages() {
        return CompletableFuture.runAsync(() -> {
            Optional.ofNullable(messagesFolder.listFiles()).ifPresent(files -> {
                if (files.length == 0) {
                    return;
                }

                Arrays.stream(files)
                        .filter(file -> file.getName().endsWith(".properties"))
                        .forEach(this::loadMessages);
            });
        }).exceptionally(t -> {
            System.err.println("Could not load locales: " + t.getMessage());
            return null;
        });
    }

    @Override
    public void clearMessages() {
        localeTranslationMap.clear();
    }

    private void loadMessages(@NotNull File file) {
        Locale locale = getLocale(file);
        if (localeTranslationMap.containsKey(locale)) {
            System.out.println("Found duplicate locale: " + locale.toLanguageTag());
            return;
        }

        Translation translation = new Translation(locale, file);
        translation.load();
        localeTranslationMap.put(locale, translation);

        System.out.println("Loaded locale: " + locale.toLanguageTag());
    }

    @Override
    public @NotNull String getMessage(@NotNull Locale locale, @NotNull String key) {
        return getMessageOptional(locale, key).orElse("N/A");
    }

    @Override
    public Optional<String> getMessageOptional(@NotNull Locale locale, @NotNull String key) {
        return Optional.ofNullable(localeTranslationMap.get(locale))
                .flatMap(translation -> translation.getMessage(key));
    }

    @Override
    public @NotNull Set<Locale> getLocales() {
        return localeTranslationMap.keySet();
    }

    private @NotNull Locale getLocale(@NotNull File file) {
        return Locale.forLanguageTag(file.getName().split("\\.")[0]);
    }
}
