package junction.finland.nova_spring.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories( basePackages = {"junction.finland.nova_spring.repository" , "junction.finland.nova_spring.security.repo"}
        ,entityManagerFactoryRef = "postgreSqlEntityManagerFactory"
)

@Log4j2
public class DataSourcePostgreSQLConfig {
    private final Environment environment;

    @Value("${app.datasource.postgresql.configuration.maximum-pool-size}")
    private String hibernatePoolSize;

    public DataSourcePostgreSQLConfig(Environment environment) {
        this.environment = environment;
    }

    @Primary
    @Bean("dataSourcePostgreSqlProperties")
    @ConfigurationProperties("app.datasource.postgresql")
    public DataSourceProperties dataSourcePostgreSqlProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean("dataSourcePostgreSql")
    @ConfigurationProperties(prefix = "app.datasource.postgresql.hikari")
    public HikariDataSource dataSourcePostgreSql(@Qualifier("dataSourcePostgreSqlProperties") DataSourceProperties dataSourcePostgreSqlProperties) {
        return dataSourcePostgreSqlProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean("postgreSqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean postgreSqlEntityManagerFactory(@Qualifier("dataSourcePostgreSql") DataSource dataSourcePostgreSql) {
        log.debug("loading config postgreSqlEntityManagerFactory");
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSourcePostgreSql);
        em.setPackagesToScan("junction.finland.nova_spring.entities" , "junction.finland.nova_spring.security.entities");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        final HashMap<String, Object> properties = new HashMap<>();
        String hbm2ddl = environment.getProperty("spring.jpa.hibernate.ddl-auto", String.class, "");
        log.info("hbm2ddl set to : {}",hbm2ddl);
        if (!hbm2ddl.equals("")) {
            properties.put("hibernate.hbm2ddl.auto", hbm2ddl);
        }
        log.info("connection.pool_size set to : {}",hibernatePoolSize);
        properties.put("connection.pool_size",hibernatePoolSize);
        em.setJpaPropertyMap(properties);
        return em;
    }

}
