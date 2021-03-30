package com.hive.apps.cleveler.view;

import com.hive.apps.cleveler.controller.MainController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.util.Duration;

public class MainSceneManager {

    private MainController controller;
    private MainSceneBuilder builder;
    private MainSceneBag bag;

    private Alert alert;

    public MainSceneManager() {
        builder = new MainSceneBuilder();
        bag = new MainSceneBag();
        controller = new MainController(this);
        startMachineCheck();
    }

    public Scene build() {
        builder.build(bag);
        bag.btnConnect.setOnAction(e -> connect());
        bag.btnSendCustom.setOnAction(e -> sendCmd());
        bag.btnLevel.setOnAction(e -> level());
        bag.cmbPorts.getItems().addAll(controller.getPorts());
        bag.cmdBaud.getItems().addAll(controller.getBaudRates());
        switchConnection(false);
        alert = new Alert(Alert.AlertType.ERROR);
        return bag.scene;
    }

    private void level() {
        if (bag.txtWidth.getText()==null || bag.txtHeight.getText()==null || bag.txtOffset.getText()==null) {
            alert.setContentText("Favor, preencher informações da placa.");
            alert.show();
            return;
        }

        try {
            var width = Double.parseDouble(bag.txtWidth.getText());
            var height = Double.parseDouble(bag.txtHeight.getText());
            var offset = Double.parseDouble(bag.txtOffset.getText());
            controller.sendLevelCmd(width, height, offset);
        } catch (NumberFormatException e) {
            alert.setContentText("Formato numérico inv&aacutelido.");
            alert.show();
        }
    }

    private void connect() {
        var baud = bag.cmdBaud.getValue();
        var port = bag.cmbPorts.getValue();
        if (baud == null || port == null || "".equals(port)) {
            alert.setContentText("Você precisa informar a porta e o baud rate, seu baitola!");
            alert.show();
            return;
        }

        if (controller.connect(port, baud)) {
            switchConnection(true);
        }
    }

    private void sendCmd() {
        controller.sendCustomCmd(bag.txtCmd.getText().trim() + "\r");
    }

    private void switchConnection(boolean connected) {
        bag.cmbPorts.setDisable(connected);
        bag.cmdBaud.setDisable(connected);
        bag.btnConnect.setDisable(connected);

        bag.txtHeight.setDisable(!connected);
        bag.txtOffset.setDisable(!connected);
        bag.txtWidth.setDisable(!connected);

        bag.btnLevel.setDisable(!connected);

        bag.txtCmd.setDisable(!connected);
        bag.btnSendCustom.setDisable(!connected);
    }

    public void printData(String data) {
        Platform.runLater(() -> bag.txtConsole.appendText(data));
    }

    private void startMachineCheck() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            var coords = controller.getMachineCoords();
            bag.lblX.setText(""+coords[0]);
            bag.lblY.setText(""+coords[1]);
            bag.lblZ.setText(""+coords[2]);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void exit() {
        controller.disconnect();
    }

}
