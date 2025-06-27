package  org.example;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;
import org.example.Barca;
import org.example.Jucator;
import org.example.MainWindowController;
import org.example.IService;

import java.io.IOException;

public class LoginController  {

    private IService service;
    private Stage stage;

    @FXML
    private TextField username;


    @FXML
    private Button login;

    @FXML
    private Parent mainWindowParent;
    private MainWindowController mainCtrl;
    private String crtUsername;
    private Logger logger  = LogManager.getLogger(LoginController.class);

    @FXML
    private void initialize() {
        login.setOnAction(event -> login(event));

        Button logbtn = new Button("Login");
        logbtn.setOnAction(event -> login(event));

    }

    private void login(ActionEvent event) {
        System.out.println("Login button pressed");
        String user = username.getText();

        try {
            Barca joc = service.login(user, mainCtrl);
            mainCtrl.setConfig(joc);
            crtUsername = user;
            openMainWindow(event);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void openMainWindow(ActionEvent actionEvent) {
        try {
            Stage crtStage = (Stage)((Node)(actionEvent.getSource())).getScene().getWindow();
            Stage stage=new Stage();
            stage.setTitle("Game Window for " +crtUsername);
            stage.setScene(new Scene(mainWindowParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    mainCtrl.logout();
                    logger.debug("Back to login window");
                    System.exit(0);
                }

            });



            stage.show();
            mainCtrl.setUser(crtUsername);
            mainCtrl.loadData();

            crtStage.hide();

        } catch (Exception e) {
            logger.info("Error opening main window: " + e.getMessage());
        }
    }

    public void setService(IService service) {
        this.service = service;
    }


    public void setParent(Parent root) {
        this.mainWindowParent = root;
    }

    public void setMainController(MainWindowController mainCtrl) {
        this.mainCtrl = mainCtrl;
    }




}
