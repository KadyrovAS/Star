package star.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean(name = "rulesDataSource")
    public DataSource rulesDataSource(@Value("${application.rules.url}") String rulesUrl){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(rulesUrl);
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }

    @Bean
    public DataSourceInitializer rulesDataSourceInitializer(
            @Qualifier("rulesDataSource") DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema-init.sql"));

        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    @Bean(name = "transactionsDataSource")
    public DataSource transactionsDataSource(@Value("${application.transactions-db.url}") String transactionsUrl) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(transactionsUrl);
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setReadOnly(true);
        return dataSource;
    }

    @Bean(name = "transactionsJdbcTemplate")
    public JdbcTemplate transactionsJdbcTemplate(
            @Qualifier("transactionsDataSource") DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "rulesJdbcTemplate")
    public JdbcTemplate rulesJdbcTemplate(
            @Qualifier("rulesDataSource") DataSource dataSource
    ){
        return new JdbcTemplate(dataSource);
    }
}