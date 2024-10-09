package be.zsoft.units.converter.repo;

import be.zsoft.units.converter.model.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyExchangeRepo extends JpaRepository<CurrencyExchangeRate, String> {

    @Query("SELECT cer FROM CurrencyExchangeRate cer WHERE cer.code = :code")
    Optional<CurrencyExchangeRate> findByCode(String code);
}
