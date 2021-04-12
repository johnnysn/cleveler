package com.hive.apps.cleveler;

import com.hive.apps.cleveler.view.MainSceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    MainSceneManager mainSceneManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        //Properties prop = new Properties();
        //prop.load(ClassLoader.getSystemClassLoader().getResourceAsStream("application.properties"));

        //var url = ClassLoader.getSystemClassLoader().getResource("main.fxml");
        //Parent root = FXMLLoader.load(url);

        mainSceneManager = new MainSceneManager(primaryStage);

        primaryStage.setTitle("Cleveler");
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(mainSceneManager.build());
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> onClose());
        primaryStage.show();
    }

    private void onClose() {
        mainSceneManager.exit();
    }
}