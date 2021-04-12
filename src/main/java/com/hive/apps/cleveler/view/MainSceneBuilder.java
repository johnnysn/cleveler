package com.hive.apps.cleveler.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

class MainSceneBuilder {

    void build(MainSceneBag bag) {
        var vBoxLeft = new VBox(10, buildPortsCombo(bag),
                buildBoardSizeFields(bag),
                buildLevelButton(bag),
                buildFileSetupFields(bag),
                buildTransformFields(bag),
                buildCustomCmdField(bag));
        vBoxLeft.setPadding(new Insets(10));
        bag.txtConsole = new TextArea();
        bag.txtConsole.setMaxWidth(500);
        bag.txtConsole.setEditable(false);
        bag.txtConsole.setMinHeight(350);
        var vBoxRight = new VBox(10, bag.txtConsole, buildCoordBox(bag));

        final var label = new Label();
        label.setText("CNC Leveler version 0.0.1");

        var pane = new BorderPane();
        pane.setPadding(new Insets(10));
        pane.setLeft(vBoxLeft);
        pane.setRight(vBoxRight);
        pane.setTop(label);

        bag.scene = new Scene(pane, 800, 480);
    }

    private Node buildTransformFields(MainSceneBag bag) {
        bag.btnTransform = new Button("Processar GCode");
        return new HBox(5,
                new VBox(bag.btnTransform));
    }

    private Node buildFileSetupFields(MainSceneBag bag) {
        bag.txtFile = new TextField();
        bag.txtFile.setMaxWidth(200);
        var vBoxTxtFile = new VBox(new Label("Arquivo GCODE"), bag.txtFile);

        bag.btnFileChooser = new Button("Arquivo");
        return new HBox(5, vBoxTxtFile,
                new VBox(new Label(""), bag.btnFileChooser));
    }

    private Node buildLevelButton(MainSceneBag bag) {
        bag.btnLevel = new Button("Nivelar CNC");
        return new HBox(5, bag.btnLevel);
    }

    private Node buildCustomCmdField(MainSceneBag bag) {
        bag.txtCmd = new TextField();
        bag.txtCmd.setMaxWidth(250);
        bag.txtCmd.setEditable(Boolean.FALSE);
        var vBoxField = new VBox(5, new Label("Comando manual"), bag.txtCmd);

        bag.btnSendCustom = new Button("Enviar");
        var btnBox = new VBox(5, new Label(""), bag.btnSendCustom);

        return new HBox(10, vBoxField, btnBox);
    }

    private HBox buildPortsCombo(MainSceneBag bag) {
        var lblPorts = new Label("Porta");
        bag.cmbPorts = new ComboBox<>();
        var cmdPortsField = new VBox(5, lblPorts, bag.cmbPorts);

        var lblBaudRate = new Label("Taxa");
        bag.cmdBaud = new ComboBox<>();
        var cmdBaudField = new VBox(5, lblBaudRate, bag.cmdBaud);

        bag.btnConnect = new Button("Conectar");
        var btnBox = new VBox(5, new Label(""), bag.btnConnect);

        return new HBox(10, cmdPortsField, cmdBaudField, btnBox);
    }

    private HBox buildBoardSizeFields(MainSceneBag bag) {
        var lblWidth = new Label("Largura");
        bag.txtWidth = new TextField();
        bag.txtWidth.setMaxWidth(70.0);

        var lblHeight = new Label("Altura");
        bag.txtHeight = new TextField();
        bag.txtHeight.setMaxWidth(70.0);

        var lblOffset = new Label("Offset");
        bag.txtOffset = new TextField();
        bag.txtOffset.setMaxWidth(70.0);

        return new HBox(10,
                new VBox(5, lblWidth, bag.txtWidth),
                new VBox(5, lblHeight, bag.txtHeight),
                new VBox(5, lblOffset, bag.txtOffset));
    }

    private VBox buildCoordBox(MainSceneBag bag) {
        bag.lblX = new Label("0"); bag.lblY = new Label("0"); bag.lblZ = new Label("0");
        var boxX = new HBox(10, new Label("X: "), bag.lblX);
        var boxY= new HBox(10, new Label("Y: "), bag.lblY);
        var boxZ = new HBox(10, new Label("Z: "), bag.lblZ);

        return new VBox(5, boxX, boxY, boxZ);
    }
}
