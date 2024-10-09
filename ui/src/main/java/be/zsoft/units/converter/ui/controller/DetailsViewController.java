package be.zsoft.units.converter.ui.controller;

import be.zsoft.units.converter.model.Unit;
import be.zsoft.units.converter.model.UnitType;
import be.zsoft.units.converter.service.CalculationService;
import be.zsoft.units.converter.ui.background.FetchUnitDataService;
import be.zsoft.units.converter.ui.cell.UnitListCellFactory;
import be.zsoft.units.converter.ui.exception.ExceptionDialog;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.StackPane;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class DetailsViewController {

    private static final boolean ENABLE_LOADING = false;

    @Autowired
    private FetchUnitDataService fetchUnitDataService;

    @Autowired
    private CalculationService calculationService;

    @FXML
    private MFXListView<Unit> fromLv;

    @FXML
    private MFXListView<Unit> toLv;

    @FXML
    private Label resultLbl;

    @FXML
    private MFXTextField valueTxt;

    @FXML
    private StackPane loadingPane;

    private UnitType currentUnitType;
    private List<Unit> units = new ArrayList<>();

    @FXML
    void initialize() {
        fromLv.setCellFactory(unit -> new UnitListCellFactory(fromLv, unit));
        toLv.setCellFactory(unit -> new UnitListCellFactory(toLv, unit));

        fromLv.getSelectionModel().setAllowsMultipleSelection(false);
        toLv.getSelectionModel().setAllowsMultipleSelection(false);

        fromLv.getSelectionModel().selectionProperty().addListener((_, _, _) -> calculate());
        toLv.getSelectionModel().selectionProperty().addListener((_, _, _) -> calculate());

        valueTxt.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));
        valueTxt.textProperty().addListener((_, _, _) -> calculate());
    }

    @FXML
    void onSwitch(ActionEvent event) {
        int fromIndex = fromLv.getSelectionModel().getSelection().keySet().stream().findFirst().orElse(-1);
        int toIndex = toLv.getSelectionModel().getSelection().keySet().stream().findFirst().orElse(-1);

        if (fromIndex < 0 || toIndex < 0) return;

        fromLv.getSelectionModel().selectIndex(toIndex);
        toLv.getSelectionModel().selectIndex(fromIndex);

        calculate();
    }

    public void initController(UnitType type) {
        currentUnitType = type;
        fetchUnitDataService.setUnitType(currentUnitType);

        resetControls();
        configureAndStartFetchService();
        loadingPane.setVisible(false);
    }

    private void resetControls() {
        units.clear();

        fromLv.getSelectionModel().clearSelection();
        toLv.getSelectionModel().clearSelection();

        fromLv.getItems().clear();
        toLv.getItems().clear();

        valueTxt.setText("1");
        resultLbl.setText("Result: ");
    }

    private void configureAndStartFetchService() {
        fetchUnitDataService.setOnRunning(event -> {
            if (ENABLE_LOADING) {
                loadingPane.setVisible(true);
            }
        });

        fetchUnitDataService.setOnFailed(event -> {
            loadingPane.setVisible(false);
            ExceptionDialog.show(fetchUnitDataService.getException());
        });

        fetchUnitDataService.setOnSucceeded(event -> {
            units = fetchUnitDataService.getValue();
            initializeControls();
            loadingPane.setVisible(false);
        });

        fetchUnitDataService.restart();
    }

    private void initializeControls() {
        fromLv.setItems(FXCollections.observableList(units));
        toLv.setItems(FXCollections.observableList(units));

        fromLv.getSelectionModel().selectIndex(0);
        toLv.getSelectionModel().selectIndex(0);
    }

    private void calculate() {
        String valueText = valueTxt.getText();
        Unit fromUnit = fromLv.getSelectionModel().getSelectedValue();
        Unit toUnit = toLv.getSelectionModel().getSelectedValue();

        if (!StringUtils.hasText(valueText) || !NumberUtils.isParsable(valueText)) return;
        if (fromUnit == null || toUnit == null) return;

        BigDecimal value = new BigDecimal(valueTxt.getText());
        BigDecimal result = currentUnitType == UnitType.CURRENCY ?
                calculationService.calculateCurrency(fromUnit, toUnit, value) :
                calculationService.calculateUnit(fromUnit, toUnit, value);

        String resultString = "Result: %s".formatted(result.toPlainString());

        if (StringUtils.hasText(toUnit.getShortHand())) {
            resultString += " " + toUnit.getShortHand();
        }

        resultLbl.setText(resultString);
    }

}
