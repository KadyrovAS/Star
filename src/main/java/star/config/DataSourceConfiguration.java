package star.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Configuration
public class DataSourceConfiguration{

    @Bean(name = "rulesDataSourcePart01")
    public DataSource rulesDataSourcePart01(@Value("${application.rules.part01.url}") String rulesUrl) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(rulesUrl);
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }

    @Bean(name = "rulesDataSourcePart02")
    public DataSource rulesDataSourcePart02(
            @Value("${application.rules.part02.url}") String url,
            @Value("${application.rules.part02.username}") String username,
            @Value("${application.rules.part02.password}") String password
    ) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }

    @Bean
    public DataSourceInitializer rulesDataSourceInitializerPart01(
            @Qualifier("rulesDataSourcePart01") DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema-init_part01.sql"));

        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    @Bean
    public DataSourceInitializer rulesDataSourceInitializerPart02(
            @Qualifier("rulesDataSourcePart02") DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("liquibase/scripts/schema-init_part02.sql"));

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

    @Bean(name = "rulesJdbcTemplatePart01")
    public JdbcTemplate rulesJdbcTemplatePart01(
            @Qualifier("rulesDataSourcePart01") DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "rulesJdbcTemplatePart02")
    public JdbcTemplate rulesJdbcTemplatePart02(
            @Qualifier("rulesDataSourcePart02") DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats());
        return cacheManager;
    }

    @Value("${telegram.bot.token}")
    private String token;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }

}