package com.github.deroq1337.bedwars.data.game.utils;

import java.util.HashMap;
import java.util.Map;

public class EnumMapFix {

    public static <K extends Enum<K>, V> Map<K, V> fixMapKeys(Map<?, V> map, Class<K> enumClass) {
        if (map == null) {
            return null;
        }

        Map<K, V> fixedMap = new HashMap<>();
        for (Map.Entry<?, V> entry : map.entrySet()) {
            if (!(entry.getKey() instanceof String)) {
                System.out.println("no string");
                continue;
            }
            try {
                K enumKey = Enum.valueOf(enumClass, (String) entry.getKey());
                fixedMap.put(enumKey, entry.getValue());
            } catch (IllegalArgumentException e) {
                System.err.println("Error fixing map keys: " + e.getMessage());
            }
        }

        return fixedMap;
    }
}
