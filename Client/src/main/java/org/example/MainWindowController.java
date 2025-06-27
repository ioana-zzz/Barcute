package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.GridPane;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.GridCell;


import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainWindowController implements IObserver{

    private String username;
    private Barca config;

    private IService service;
    private int scor = 0;
    private int tries = 3;
    private int triesMade = 0;

    private List<Integer> pozGhicit = new ArrayList<>();
    private List<Try> incercariList = new ArrayList<>();
    private Meci meci = new Meci();

    private Button[] buttons = new Button[25];

    @FXML
    private GridPane joc;

    @FXML
    private TableView<Meci> clasament;

    @FXML
    private TableColumn<Meci, String> nume;

    @FXML
    private TableColumn<Meci, String> data;

    @FXML
    private TableColumn<Meci, Integer> scorColumn;


    @FXML
    private Label text;


    ObservableList<Meci> clasamentList = FXCollections.observableArrayList();



    public MainWindowController() {

    };

    public void setService(IService service) {
        this.service = service;
    }

    public void setConfig(Barca joc){
        initGameBoard();
        this.config = joc;
        meci.setBarca(joc);
        meci.setTimestamp(LocalDateTime.now());

    }

    public void logout() {
        service.logout(username, this);
    }

    public void setUser(String crtUsername) {
        this.username = crtUsername;
        var j = new Jucator(0L, crtUsername);
        meci.setJucator(j);
    }



    public void loadData() {

        nume.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getJucator().getNume()));


        data.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getTimestamp().toString())

        );

        scorColumn.setCellValueFactory(cellData ->
                (new SimpleIntegerProperty(cellData.getValue().getScor()).asObject()));






        List<Meci> meciuri = service.getClasament();
        if(meciuri == null) {
            showError("clasamentul nu a putut fi incarcat / nu exista clasament!");
        }


        clasamentList.setAll(meciuri);
        clasament.setItems(clasamentList);
        clasament.refresh();
    }


    public void initGameBoard() {
        joc.getChildren().clear();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Button btn = new Button();
                btn.setMinSize(60, 60);
                int pos = i * 5 + j;
                btn.setOnAction(event -> handleGuess(pos));
                buttons[5*i+j] = btn;
                joc.add(btn, j, i);
            }
        }
    }



    private int currentTry = 0;

    private void handleGuess(int pos) {
        var incercare = new Try(0);
        incercariList.add(incercare);
        incercare.setPozitie(pos);
        triesMade++;

        if ((pos == config.getPoz1() || pos == config.getPoz2() || pos == config.getPoz3()))
            { if(!pozGhicit.contains(pos))
                {
                    pozGhicit.add(pos);
                    buttons[pos].setStyle("-fx-background-color: lime;");
                    buttons[pos].setText("B");
                    text.setText("Alegere corecta!");

                    scor += 5;
                }
        } else
            {
                buttons[pos].setStyle("-fx-background-color: red;");
                scor -=3 ;
                text.setText("Esti la o dist. de " + getClosest(pos));
            }

        if (pozGhicit.size() == 3) {
            text.setText("FELICITĂRI! Ai ghicit toate pozițiile!");

            meci.setIncercari(incercariList);
            meci.setScor(scor);


            gameEnd();
            service.saveMeci(meci);
        } else if (triesMade == tries) {
            text.setText("Ai pierdut! Nu ai ghicit toate pozițiile.");

            meci.setIncercari(incercariList);
            meci.setScor(scor);


            gameEnd();
            service.saveMeci(meci);
        }
    }



    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }


    public int coordsToInt(int row, int col) {
        return row * 5 + col;
    }

    public int intToX(int position) {
        return position / 5;

    }

    public int intToY(int position) {
        return position % 5;
    }

    @Override
    public void update(Meci meci) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    clasamentList.add(meci);
                    clasamentList.sort((m1, m2) -> Integer.compare(m2.getScor() - m1.getScor(), 0));
                    clasament.setItems(clasamentList);
                    clasament.refresh();
                });
                return null;
            }
        };

        new Thread(task).start();

    }


    private void gameEnd() {
        buttons[config.getPoz1()].setStyle("-fx-background-image: url(https://static.vecteezy.com/system/resources/previews/034/999/112/non_2x/tick-checkmark-icon-web-choice-ok-symbol-sign-button-approval-vector.jpg); -fx-background-size: cover; -fx-text-fill: white;");
        buttons[config.getPoz2()].setStyle("-fx-background-image: url(https://static.vecteezy.com/system/resources/previews/034/999/112/non_2x/tick-checkmark-icon-web-choice-ok-symbol-sign-button-approval-vector.jpg); -fx-background-size: cover; -fx-text-fill: white;");
        buttons[config.getPoz3()].setStyle("-fx-background-image: url(https://static.vecteezy.com/system/resources/previews/034/999/112/non_2x/tick-checkmark-icon-web-choice-ok-symbol-sign-button-approval-vector.jpg); -fx-background-size: cover; -fx-text-fill: white;");


        for (Button button : buttons) {
            if (button != null) {
                button.setDisable(true);
            }
        }
    }

    private double distance(int pos1, int pos2) {
        int x1 = intToX(pos1);
        int y1 = intToY(pos1);
        int x2 = intToX(pos2);
        int y2 = intToY(pos2);

        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private double getClosest(int pos) {
        double d1 = distance(pos, config.getPoz1());
        double d2 = distance(pos, config.getPoz2());
        double d3 = distance(pos, config.getPoz3());

        return Math.min(d1, Math.min(d2, d3));
    }


}
