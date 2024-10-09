package be.zsoft.units.converter.config;

import be.zsoft.units.converter.utils.Constants;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.sqlite.JDBC;

import javax.sql.DataSource;
import java.nio.file.Paths;

@Configuration
@EnableJpaRepositories(basePackages = {"be.zsoft.units.converter.repo"})
@EntityScan(basePackages = {"be.zsoft.units.converter.model"})
public class DatasourceConfig {

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(JDBC.class.getName());
        dataSource.setUrl("jdbc:sqlite:" + getDBPath());
        return dataSource;
    }

    private String getDBPath() {
        return Paths.get(Constants.APPLICATION_DIRECTORY.toString(), "db.db").toString();
    }
}
