package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.LoginController;
import org.example.MainWindowController;
import org.example.JsonProtocol.ProxyJson;
//import org.example.rpcProtocol.ProxyRpc;
import org.example.IService;

import java.io.IOException;
import java.util.Properties;

public class StartJsonClient extends Application {
    private static int defaultPort = 55556;
    private static String defaultServer = "localhost";
    private Logger logger = LogManager.getLogger(StartJsonClient.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartJsonClient.class.getResourceAsStream("/client.properties"));
        } catch (IOException e) {
            logger.info("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            logger.debug("Wrong port number " + ex.getMessage());
        }

        IService server = new ProxyJson(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        LoginController ctrl = loader.getController();
        ctrl.setService(server);

        FXMLLoader mloader = new FXMLLoader(getClass().getResource("/game.fxml"));
        Parent mroot = mloader.load();
        MainWindowController mainCtrl = mloader.getController();
        mainCtrl.setService(server);

        ctrl.setMainController(mainCtrl);
        ctrl.setParent(mroot);

        primaryStage.setTitle("Joculet");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();





    }

    public static void main(String[] args) {
        launch(args);

    }
}