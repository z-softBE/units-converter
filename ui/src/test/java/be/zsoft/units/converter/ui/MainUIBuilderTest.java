package be.zsoft.units.converter.ui;

import fr.brouillard.oss.cssfx.CSSFX;
import io.github.palexdev.materialfx.theming.CSSFragment;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainUIBuilderTest extends ApplicationTest {

    @Mock
    private FXMLLoader fxmlLoader;

    @Mock
    private Stage stage;

    @Mock
    private UserAgentBuilder userAgentBuilder;

    @Mock
    private CSSFragment fragment;

    @InjectMocks
    private MainUIBuilder mainUIBuilder;

    @Captor
    private ArgumentCaptor<Scene> sceneArgumentCaptor;

    @Captor
    private ArgumentCaptor<Image> iconCaptor;

    @Test
    void buildMainStage() throws Exception {
        try (
                MockedStatic<CSSFX> cssfxMockedStatic = mockStatic(CSSFX.class);
                MockedStatic<UserAgentBuilder> userAgentBuilderMockedStatic = mockStatic(UserAgentBuilder.class);
        ) {
            ObservableList<Image> icons = mock(ObservableList.class);
            AnchorPane root = new AnchorPane();

            userAgentBuilderMockedStatic.when(UserAgentBuilder::builder).thenReturn(userAgentBuilder);

            when(userAgentBuilder.themes(any(Collection.class))).thenReturn(userAgentBuilder);
            when(userAgentBuilder.themes(JavaFXThemes.MODENA)).thenReturn(userAgentBuilder);
            when(userAgentBuilder.setDeploy(true)).thenReturn(userAgentBuilder);
            when(userAgentBuilder.setResolveAssets(true)).thenReturn(userAgentBuilder);
            when(userAgentBuilder.build()).thenReturn(fragment);
            when(fxmlLoader.load()).thenReturn(root);
            when(stage.getIcons()).thenReturn(icons);

            mainUIBuilder.buildMainStage(stage);

            cssfxMockedStatic.verify(CSSFX::start);
            verify(userAgentBuilder).themes(JavaFXThemes.MODENA);
            verify(userAgentBuilder).themes(MaterialFXStylesheets.forAssemble(true));
            verify(userAgentBuilder).setDeploy(true);
            verify(userAgentBuilder).setResolveAssets(true);
            verify(userAgentBuilder).build();
            verify(fragment).setGlobal();

            verify(fxmlLoader).setLocation(ResourceLoader.loadURL("fxml/main.fxml"));

            verify(stage).initStyle(StageStyle.DECORATED);
            verify(stage).setMinWidth(1200d);
            verify(stage).setMinHeight(800d);
            verify(stage).getIcons();
            verify(icons).add(iconCaptor.capture());
            verify(stage).setScene(sceneArgumentCaptor.capture());
            verify(stage).show();

            assertThat(iconCaptor.getValue().getUrl()).isEqualTo(ResourceLoader.load("logo.png"));

            assertThat(sceneArgumentCaptor.getValue().getRoot()).isEqualTo(root);
            assertThat(sceneArgumentCaptor.getValue().getWidth()).isEqualTo(1200);
            assertThat(sceneArgumentCaptor.getValue().getHeight()).isEqualTo(800);
            assertThat(sceneArgumentCaptor.getValue().getFill()).isEqualTo(Color.TRANSPARENT);
        }
    }
}