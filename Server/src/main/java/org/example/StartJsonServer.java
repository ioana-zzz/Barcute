package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.Repository.BarcaRepository;
import org.example.Repository.JucatorRepository;
import org.example.Repository.MeciRepository;
import org.example.JsonProtocol.ClientJsonWorker;
import org.example.utils.AbsConcurrentServer;


import java.io.IOException;
import java.net.Socket;
import java.rmi.ServerException;
import java.util.Properties;

public class StartJsonServer {
    private static int defaultPort = 55556;
    private static Logger logger = LogManager.getLogger(StartJsonServer.class);

    public static void main(String[] args) {
        // Load server properties
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartJsonServer.class.getResourceAsStream("/server.properties"));
        } catch (IOException e) {
            logger.error("Could not load server properties", e);
            return;
        }

        // Set port
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port", String.valueOf(defaultPort)));
        } catch (NumberFormatException ex) {
            logger.debug("Wrong port number " + ex.getMessage());
            logger.debug("Using default port: " + defaultPort);
        }


        // Create repositories using SessionFactory
        JucatorRepository jucatorRepository =
                new JucatorRepository(HibernateUtils.getSessionFactory());
        MeciRepository meciRepository =
                new MeciRepository(HibernateUtils.getSessionFactory());


        BarcaRepository jocRepository = new BarcaRepository();

        // Create service
        Service service = new Service(meciRepository, jocRepository, jucatorRepository);

        // Set up server
        AbsConcurrentServer server = new AbsConcurrentServer(serverPort) {
            @Override
            protected Thread createWorker(Socket client) {
                ClientJsonWorker worker = new ClientJsonWorker(service, client);
                return new Thread(worker);
            }
        };

        try {
            server.start();
            System.out.println("Server started...");
        } catch (ServerException e) {
            logger.debug("Server exception " + e.getMessage());
        } finally {
            // Important: Close the SessionFactory when done
            HibernateUtils.closeSessionFactory();
        }
    }
}
