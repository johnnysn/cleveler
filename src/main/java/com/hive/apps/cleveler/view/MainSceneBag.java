package com.hive.apps.cleveler.view;

import javafx.scene.Scene;
import javafx.scene.control.*;

class MainSceneBag {

    Button btnLevel;
    Scene scene;

    ComboBox<String> cmbPorts;
    ComboBox<Integer> cmdBaud;
    TextArea txtConsole;

    Button btnConnect;

    TextField txtWidth;
    TextField txtHeight;
    TextField txtOffset;

    TextField txtCmd;
    Button btnSendCustom;

    Label lblX;
    Label lblY;
    Label lblZ;

    TextField txtFile;
    Button btnFileChooser;

    TextField txtFileOutput;
    Button btnFileSave;
    Button btnTransform;

}
