package com.github.deroq1337.bedwars.data.language;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Getter
public class Translation {

    private final @NotNull Locale locale;
    private final @NotNull File file;
    private final @NotNull Map<String, String> messages = new ConcurrentHashMap<>();

    public void load() {
        try (InputStreamReader inputStream = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            Properties properties = new Properties();
            properties.load(inputStream);

            properties.forEach((key, value) -> {
                String translationKey = (String) key;
                String message = (String) value;

                if (message.contains("%prefix%")) {
                    message = message.replaceAll("%prefix%", properties.getProperty("prefix", "N/A"));
                }

                messages.put(translationKey, message);
            });
        } catch (IOException e) {
            System.err.println("Could not load messages for locale '" + locale.toLanguageTag() + "': " + e.getMessage());
        }
    }

    public @NotNull String getMessage(@NotNull String key) {
        return messages.getOrDefault(key, "N/A");
    }
}
