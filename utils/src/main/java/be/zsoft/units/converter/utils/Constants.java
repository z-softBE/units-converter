package be.zsoft.units.converter.utils;

import lombok.experimental.UtilityClass;

import java.nio.file.Path;

@UtilityClass
public class Constants {

    public static final Path APPLICATION_DIRECTORY = Path.of(System.getProperty("user.home"), ".units-converter");
}
