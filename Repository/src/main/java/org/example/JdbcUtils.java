package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Component
@PropertySource("classpath:jdbc.properties")
public class JdbcUtils {

    private Properties jdbcProps = new Properties();

    private static final Logger logger = LogManager.getLogger();


    public JdbcUtils(){
       try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jdbc.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("Unable to find jdbc.properties");
            }
            jdbcProps.load(inputStream);
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error loading properties file: " + e);
        }

        getNewConnection();
    }

    private  Connection instance=null;

    private Connection getNewConnection(){
       logger.traceEntry();
        String url=jdbcProps.getProperty("jdbc.url");
        String user=jdbcProps.getProperty("jdbc.user");
        String pass=jdbcProps.getProperty("jdbc.pass");
        logger.info("trying to connect to database ... {}",url);
        logger.info("user: {}",user);
        logger.info("pass: {}", pass);
        Connection con=null;
        try {

            if (user!=null && pass!=null)
                con= DriverManager.getConnection(url,user,pass);
            else
                con=DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting connection "+e);
        }
        logger.info("Connected to database");
        return con;
    }

    @Bean
    public Connection getConnection(){
        logger.traceEntry();
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
           logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(instance);
        logger.info("Returning connection");
        return instance;
    }
}

