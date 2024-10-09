package be.zsoft.units.converter.ui.background;

import be.zsoft.units.converter.model.Unit;
import be.zsoft.units.converter.model.UnitType;
import be.zsoft.units.converter.service.UnitService;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FetchUnitDataService extends javafx.concurrent.Service<List<Unit>> {

    private final UnitService unitService;

    // State
    @Setter
    private UnitType unitType;

    @Override
    protected Task<List<Unit>> createTask() {
        return new Task<>() {
            @Override
            protected List<Unit> call() throws Exception {
                return unitService.getAllUnitsByType(unitType);
            }
        };
    }
}
