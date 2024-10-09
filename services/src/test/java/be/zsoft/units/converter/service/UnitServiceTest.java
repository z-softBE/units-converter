package be.zsoft.units.converter.service;

import be.zsoft.units.converter.model.Unit;
import be.zsoft.units.converter.model.UnitType;
import be.zsoft.units.converter.repo.UnitRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnitServiceTest {

    @Mock
    private UnitRepo unitRepo;

    @InjectMocks
    private UnitService unitService;

    @Test
    void getAllUnitsByType() {
        List<Unit> expected = List.of(Unit.builder().id(UUID.randomUUID().toString()).build(), Unit.builder().id(UUID.randomUUID().toString()).build());

        when(unitRepo.findAllByType(UnitType.AREA)).thenReturn(expected);

        List<Unit> result = unitService.getAllUnitsByType(UnitType.AREA);

        verify(unitRepo).findAllByType(UnitType.AREA);

        assertThat(result).containsExactlyElementsOf(expected);
    }

}