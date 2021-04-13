package com.hive.apps.cleveler.view;

import com.hive.apps.cleveler.cnc.transform.CompensationFuncion;
import com.hive.apps.cleveler.controller.MainController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class MainSceneManager {

    private Stage stage;
    private MainController controller;
    private MainSceneBuilder builder;
    private MainSceneBag bag;

    private CompensationFuncion probingFunction;
    private File inputFile;
    private File outputFile;

    private Alert alertError;
    private Alert alertInfo;

    public MainSceneManager(Stage stage) {
        this.stage = stage;
        builder = new MainSceneBuilder();
        bag = new MainSceneBag();
        controller = new MainController(this);
        startMachineCheck();
    }

    public Scene build() {
        builder.build(bag);
        // Ações dos botões
        bag.btnConnect.setOnAction(e -> connect());
        bag.btnSendCustom.setOnAction(e -> sendCmd());
        bag.btnLevel.setOnAction(e -> level());
        bag.btnFileChooser.setOnAction(e -> chooseFile());
        bag.btnFileSave.setOnAction(e -> saveFile());
        bag.btnTransform.setOnAction(e -> transform());
        // Preenchimento dos comboboxes
        bag.cmbPorts.getItems().addAll(controller.getPorts());
        bag.cmdBaud.getItems().addAll(controller.getBaudRates());
        switchConnection(false);
        alertError = new Alert(Alert.AlertType.ERROR);
        alertInfo = new Alert(Alert.AlertType.INFORMATION);
        return bag.scene;
    }

    private void transform() {
        if (probingFunction == null || inputFile == null || outputFile == null) {
            alertError.setContentText("É necessário informar os arquivos de origem e destino após realizar o probing da placa.");
            alertError.show();
            return;
        }
        try {
            controller.transformGCodeFile(inputFile, probingFunction, outputFile);
            alertInfo.setContentText("Processamento finalizado.");
            alertInfo.show();
        } catch (IOException e) {
            alertError.setContentText("Erro ao processar o arquivo.");
            alertError.show();
        }
    }

    private void saveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Criar arquivo GCode de destino");
        fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Arquivos CNC", "*.cnc"),
                    new FileChooser.ExtensionFilter("Arquivos de texto", "*.txt")
                );
        outputFile = fileChooser.showSaveDialog(stage);
        if (outputFile != null) {
            bag.txtFileOutput.setText(outputFile.getName());
        }
    }

    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir arquivo GCode de origem");
        fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Arquivos CNC", "*.cnc"),
                    new FileChooser.ExtensionFilter("Arquivos de texto", "*.txt")
                );
        inputFile = fileChooser.showOpenDialog(stage);
        if (inputFile != null) {
            bag.txtFile.setText(inputFile.getName());
        }
    }

    private void level() {
        if (bag.txtWidth.getText()==null || bag.txtHeight.getText()==null || bag.txtOffset.getText()==null) {
            alertError.setContentText("Favor, preencher informações da placa.");
            alertError.show();
            return;
        }

        try {
            var width = Double.parseDouble(bag.txtWidth.getText());
            var height = Double.parseDouble(bag.txtHeight.getText());
            var offset = Double.parseDouble(bag.txtOffset.getText());
            controller.sendLevelCmd(width, height, offset);
        } catch (NumberFormatException e) {
            alertError.setContentText("Formato numérico inv&aacutelido.");
            alertError.show();
        }
    }

    private void connect() {
        var baud = bag.cmdBaud.getValue();
        var port = bag.cmbPorts.getValue();
        if (baud == null || port == null || "".equals(port)) {
            alertError.setContentText("Você precisa informar a porta e o baud rate, seu baitola!");
            alertError.show();
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

        bag.txtFile.setDisable(!connected);
        bag.btnFileChooser.setDisable(!connected);
        bag.txtFileOutput.setDisable(!connected);
        bag.btnFileSave.setDisable(!connected);

        inputFile = null;
        probingFunction = null;
        bag.btnTransform.setDisable(Boolean.TRUE);
    }

    public void printMessage(String data) {
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

    public void setProbingFunction(String msg, CompensationFuncion cf) {
        this.printMessage(msg);
        this.probingFunction = cf;
        bag.btnTransform.setDisable(Boolean.FALSE);
    }

    public void exit() {
        controller.disconnect();
    }

}
