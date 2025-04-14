package com.example.reactiveSpring.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
@EnableR2dbcAuditing  //Needed to enable auto-update of Audit fields in a R2DBC repository.
public class DatabaseConfig {

    @Value("classpath:/schema.sql")
    Resource resource;

    // In Reactive applications, JPA is not supported, and JDBC-based DataSource is blocking.
    // We use ConnectionFactoryInitializer to initialize the relational database schema using R2DBC.
    // It binds to the ConnectionFactory and executes the SQL script from schema.sql via ResourceDatabasePopulator.
    // This ensures a non-blocking way to set up the database before the application starts.
    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory){
        ConnectionFactoryInitializer factoryInitializer = new ConnectionFactoryInitializer();
        factoryInitializer.setConnectionFactory(connectionFactory);
        factoryInitializer.setDatabasePopulator(new ResourceDatabasePopulator(resource));
        return factoryInitializer;
    }
}
