package com.github.deroq1337.bedwars.data.game.map.converters;

import com.github.deroq1337.bedwars.data.game.map.serialization.BedWarsMapLocation;
import lombok.RequiredArgsConstructor;
import net.cubespace.Yamler.Config.ConfigSection;
import net.cubespace.Yamler.Config.Converter.Converter;
import net.cubespace.Yamler.Config.InternalConverter;

import java.lang.reflect.ParameterizedType;

@RequiredArgsConstructor
public class BedWarsMapLocationConverter implements Converter {

    private final InternalConverter internalConverter;

    @Override
    public Object toConfig(Class<?> aClass, Object o, ParameterizedType parameterizedType) {
        return ((BedWarsMapLocation) o).toMap();
    }

    @Override
    public Object fromConfig(Class<?> aClass, Object o, ParameterizedType parameterizedType) {
        if (!(o instanceof ConfigSection configSection)) {
            return null;
        }

        if (configSection.getRawMap() == null) {
            return null;
        }

        return new BedWarsMapLocation(configSection.getRawMap());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(BedWarsMapLocation.class);
    }
}
