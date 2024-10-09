package be.zsoft.units.converter.ui.utils;

import be.zsoft.units.converter.model.UnitType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NavbarItem {

    LENGTH("fas-ruler", "Length", UnitType.LENGTH),
    VOLUME("fas-circle", "Volume", UnitType.VOLUME),
    AREA("fas-vector-square", "Area", UnitType.AREA),
    ENERGY("fas-bolt", "Energy", UnitType.ENERGY),
    FORCE("fas-dumbbell", "Force", UnitType.FORCE),
    SPEED("fas-person-running", "Speed", UnitType.SPEED),
    DATA("fas-server", "Data", UnitType.DATA),
    CURRENCY("fas-dollar-sign", "Currency", UnitType.CURRENCY),
    WEIGHT("fas-scale-balanced", "Weight", UnitType.WEIGHT),
    TEMPERATURE("fas-thermometer", "Temperature", UnitType.TEMPERATURE),
    PRESSURE("fas-triangle-exclamation", "Pressure", UnitType.PRESSURE),
    POWER("fas-plug", "Power", UnitType.POWER),
    TIME("fas-clock", "Time", UnitType.TIME),
    ANGLE("fas-slash", "Angle", UnitType.ANGLE),
    DENSITY("fas-water", "Density", UnitType.DENSITY),
    FUEL_CONSUMPTION("fas-gas-pump", "Fuel consumption", UnitType.FUEL_CONSUMPTION),
    ELECTRIC_CURRENT("fas-car-battery", "Electric current", UnitType.ELECTRIC_CURRENT);

    private final String icon;
    private final String text;
    private final UnitType type;

}
