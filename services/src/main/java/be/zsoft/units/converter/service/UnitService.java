package be.zsoft.units.converter.service;

import be.zsoft.units.converter.model.Unit;
import be.zsoft.units.converter.model.UnitType;
import be.zsoft.units.converter.repo.UnitRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor

@Service
public class UnitService {

    private final UnitRepo unitRepo;

    public List<Unit> getAllUnitsByType(UnitType unitType) {
        return unitRepo.findAllByType(unitType);
    }
}
