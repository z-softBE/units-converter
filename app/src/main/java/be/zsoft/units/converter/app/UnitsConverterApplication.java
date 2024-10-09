package be.zsoft.units.converter.app;

import be.zsoft.units.converter.ui.exception.ExceptionHandler;
import be.zsoft.units.converter.utils.Constants;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j

@SpringBootApplication(
        scanBasePackages = {"com.gluonhq.ignite.spring", "be.zsoft.units.converter"},
        exclude = {DataSourceAutoConfiguration.class}
)
@EnableScheduling
public class UnitsConverterApplication {

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        SetupSwingLookAndFeel();
        SetupApplicationDirectory();
        Application.launch(UnitsConverterFXApplication.class, args);
    }

    private static void SetupSwingLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            log.error(ex.getMessage(), ex);
            System.exit(-1);
        }
    }

    private static void SetupApplicationDirectory() {
        log.info("Creating application directory");

        try {
            Files.createDirectories(Constants.APPLICATION_DIRECTORY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
