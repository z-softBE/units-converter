package be.zsoft.units.converter.ui.cell;

import be.zsoft.units.converter.model.Unit;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;

public class UnitListCellFactory extends MFXListCell<Unit> {

    public UnitListCellFactory(MFXListView<Unit> listView, Unit data) {
        super(listView, data);
    }

    @Override
    protected void render(Unit data) {
        super.render(data);
    }
}
