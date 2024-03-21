package com.agency.appointment.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zaxxer.hikari.HikariDataSource;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.primary")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.primary.hikari")
    public HikariDataSource primaryDataSource(DataSourceProperties primaryDataSourceProperties) {
        HikariDataSource dataSource = primaryDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        dataSource.setAutoCommit(false);
        return dataSource;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave")
    public DataSourceProperties replicaDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave.hikari")
    public HikariDataSource replicaDataSource(DataSourceProperties replicaDataSourceProperties) {
        HikariDataSource dataSource = replicaDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        dataSource.setAutoCommit(false);
        return dataSource;
    }

    @Bean
    @Primary
    public TransactionRoutingDataSource routingDataSource(DataSource primaryDataSource, DataSource replicaDataSource) {
        TransactionRoutingDataSource routingDataSource = new TransactionRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceType.READ_WRITE, primaryDataSource);
        dataSourceMap.put(DataSourceType.READ_ONLY, replicaDataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(primaryDataSource);

        return routingDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("routingDataSource") DataSource routingDataSource) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(routingDataSource);
        bean.setPackagesToScan("com.agency.appointment");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        HibernateJpaDialect jpaDialect = vendorAdapter.getJpaDialect();

        assert jpaDialect != null;
        jpaDialect.setPrepareConnection(false);
        bean.setJpaVendorAdapter(vendorAdapter);
        bean.setJpaProperties(additionalProperties());
        return bean;
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JPAQueryFactory(Objects.requireNonNull(entityManagerFactory.getObject()).createEntityManager());
    }

    @Bean
    public JpaTransactionManager transactionManager(@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()));
    }

    private Properties additionalProperties() {
        return new Properties() {{
            put("hibernate.implicit_naming_strategy","org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl");
            put("hibernate.physical_naming_strategy","org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
            put("hibernate.hbm2ddl.auto", "update");
            put("hibernate.ddl-auto", "update");
            put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            put("hibernate.connection.provider_disables_autocommit", Boolean.TRUE.toString());
            put("show-sql", Boolean.TRUE.toString());
        }};
    }

    @Bean
    public ModelMapper customMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        return modelMapper;
    }
}
