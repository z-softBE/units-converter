package be.zsoft.units.converter.ui.controller;

import be.zsoft.units.converter.ui.ResourceLoader;
import be.zsoft.units.converter.ui.utils.FxmlLoaderUtils;
import be.zsoft.units.converter.ui.utils.NavbarItem;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.utils.ScrollUtils;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Component
public class MainController {

    private static final String GITHUB_URL = "https://github.com/z-softBE/units-converter";
    private static final String ZSOFT_URL = "https://z-soft.be";
    private static final String DONATE_URL = "https://buymeacoffee.com/zsoft";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private HostServices hostServices;

    @FXML
    private BorderPane contentPane;

    @FXML
    private MFXScrollPane navbarScrollPane;

    @FXML
    private VBox navbarVBox;

    @FXML
    private StackPane logoContainer;

    @FXML
    private ImageView zsoftLogo;

    private final ToggleGroup toggleGroup = new ToggleGroup();

    private DetailsViewController detailsViewController;

    @FXML
    public void initialize() {
        try {
            buildNavbar();
            ScrollUtils.addSmoothScrolling(navbarScrollPane);
            initializeLogo();
            initializeZSoft();
            loadController();

            Toggle toggleToSelect = toggleGroup.getToggles().getFirst();
            toggleGroup.selectToggle(toggleToSelect);
            loadPage((NavbarItem) toggleToSelect.getUserData());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    void onDonate(ActionEvent event) {
        hostServices.showDocument(DONATE_URL);
    }

    @FXML
    void onGithub(ActionEvent event) {
        hostServices.showDocument(GITHUB_URL);
    }

    private void buildNavbar() {
        List<ToggleButton> navbarToggles = Stream.of(NavbarItem.values()).map(this::createToggle).toList();
        toggleGroup.selectToggle(navbarToggles.getFirst());
        navbarVBox.getChildren().setAll(navbarToggles);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;

            loadPage((NavbarItem) newValue.getUserData());
        });
    }

    private void initializeLogo() {
        Image logoImage = new Image(ResourceLoader.load("logo.png"), 32, 32, true, true);
        ImageView logoView = new ImageView(logoImage);
        Circle clip = new Circle(30);
        clip.centerXProperty().bind(logoView.layoutBoundsProperty().map(Bounds::getCenterX));
        clip.centerYProperty().bind(logoView.layoutBoundsProperty().map(Bounds::getCenterY));
        logoView.setClip(clip);
        logoContainer.getChildren().add(logoView);
    }

    private void initializeZSoft() {
        zsoftLogo.addEventHandler(MouseEvent.MOUSE_ENTERED, _ -> zsoftLogo.getScene().setCursor(Cursor.HAND));
        zsoftLogo.addEventHandler(MouseEvent.MOUSE_EXITED, _ -> zsoftLogo.getScene().setCursor(Cursor.DEFAULT));
        zsoftLogo.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() != MouseButton.PRIMARY) return;
            hostServices.showDocument(ZSOFT_URL);
        });
    }

    private void loadController() throws IOException {
        Pair<Node, Object> loaderData = FxmlLoaderUtils.loadView("fxml/details-view.fxml", applicationContext);
        contentPane.getChildren().clear();
        contentPane.setCenter(loaderData.getKey());

        detailsViewController = (DetailsViewController) loaderData.getValue();
    }

    private void loadPage(NavbarItem navbarItem) {
        detailsViewController.initController(navbarItem.getType());
    }

    private ToggleButton createToggle(NavbarItem item) {
        MFXIconWrapper wrapper = new MFXIconWrapper(item.getIcon(), 24, 32);
        MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(item.getText(), wrapper);
        toggleNode.setAlignment(Pos.CENTER_LEFT);
        toggleNode.setMaxWidth(Double.MAX_VALUE);
        toggleNode.setToggleGroup(toggleGroup);
        toggleNode.setUserData(item);

        return toggleNode;
    }
}
