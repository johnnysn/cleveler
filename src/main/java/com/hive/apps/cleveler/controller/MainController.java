package com.hive.apps.cleveler.controller;

import com.hive.apps.cleveler.cnc.transform.CompensationFuncion;
import com.hive.apps.cleveler.service.GrblService;
import com.hive.apps.cleveler.service.transform.TransformService;
import com.hive.apps.cleveler.view.MainSceneManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainController extends AbstractController {

    private GrblService grblService;
    private TransformService transformService;
    private MainSceneManager sceneManager;

    private TimerTask checkerTask;
    private Timer timer;

    public MainController(MainSceneManager sceneManager) {
        grblService = new GrblService(this);
        transformService = new TransformService();
        this.sceneManager = sceneManager;
    }

    public List<String> getPorts() {
        return grblService.listAvailablePorts();
    }

    public List<Integer> getBaudRates() {
        return List.of(9600, 57600, 115200);
    }

    public boolean connect(String port, int baudRate) {
        var connected = grblService.connect(port, baudRate, this);
        if (connected) {
            //startStatusChecker();
        }
        return connected;
    }

    public void disconnect() {
        if (checkerTask != null) {
            checkerTask.cancel();
            timer.cancel();
            timer.purge();
        }
        grblService.disconnect();
    }

    public void sendCustomCmd(String cmd) {
        grblService.sendCmd(cmd);
    }

    public void sendLevelCmd(double width, double height, double offset) {
        grblService.startLeveling(width, height, offset);
    }

    public double[] getMachineCoords() {
        return grblService.getMachineCoords();
    }

    // TODO Design status checking policy
    private void startStatusChecker() {
        checkerTask = new TimerTask() { public void run() {
            grblService.sendCmd("?\r");
        }};

        timer = new Timer();
        timer.scheduleAtFixedRate(checkerTask, 0, 2500);
    }

    @Override
    public void showMessage(String msg) {
        sceneManager.printMessage(msg);
    }

    public void reportTask(String msg, Object data) {
        if (data instanceof CompensationFuncion) { // Finished probing
            sceneManager.setProbingFunction(msg, (CompensationFuncion) data);
        }
    }

    public void transformGCodeFile(File file, CompensationFuncion function, String outputFileName)
            throws IOException {
        transformService.compensateZ(file, function, outputFileName);
    }
}
