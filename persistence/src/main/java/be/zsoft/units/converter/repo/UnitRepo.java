package be.zsoft.units.converter.repo;

import be.zsoft.units.converter.model.Unit;
import be.zsoft.units.converter.model.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepo extends JpaRepository<Unit, String> {

    @Query("SELECT u FROM Unit u WHERE u.type = :unitType order by LOWER(u.name)")
    List<Unit> findAllByType(UnitType unitType);
}
