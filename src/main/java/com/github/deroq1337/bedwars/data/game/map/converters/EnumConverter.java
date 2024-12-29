package com.github.deroq1337.bedwars.data.game.map.converters;

import lombok.RequiredArgsConstructor;
import net.cubespace.Yamler.Config.Converter.Converter;
import net.cubespace.Yamler.Config.InternalConverter;

import java.lang.reflect.ParameterizedType;

@RequiredArgsConstructor
public class EnumConverter implements Converter {

    private final InternalConverter internalConverter;

    @Override
    public Object toConfig(Class<?> aClass, Object o, ParameterizedType parameterizedType) {
        return ((Enum<?>) o).name();
    }

    @Override
    public Object fromConfig(Class<?> aClass, Object o, ParameterizedType parameterizedType) {
        if (o == null) {
            return null;
        }

        return Enum.valueOf((Class<Enum>) aClass, (String) o);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isEnum();
    }
}
