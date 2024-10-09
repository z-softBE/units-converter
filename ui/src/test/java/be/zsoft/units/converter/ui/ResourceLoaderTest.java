package be.zsoft.units.converter.ui;

import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceLoaderTest {

    @Test
    void loadURL() {
        URL expected = ResourceLoaderTest.class.getResource("fxml/main.fxml");
        URL result = ResourceLoader.loadURL("fxml/main.fxml");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void load() {
        String expected = ResourceLoaderTest.class.getResource("fxml/main.fxml").toString();
        String result = ResourceLoader.load("fxml/main.fxml");

        assertThat(result).isEqualTo(expected);
    }
}