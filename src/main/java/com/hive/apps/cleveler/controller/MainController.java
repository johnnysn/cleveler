package com.hive.apps.cleveler.controller;

import com.hive.apps.cleveler.cnc.transform.CompensationFuncion;
import com.hive.apps.cleveler.service.GrblService;
import com.hive.apps.cleveler.view.MainSceneManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainController extends AbstractController {

    private GrblService service;
    private MainSceneManager sceneManager;

    private TimerTask checkerTask;
    private Timer timer;

    public MainController(MainSceneManager sceneManager) {
        service = new GrblService(this);
        this.sceneManager = sceneManager;
    }

    public List<String> getPorts() {
        return service.listAvailablePorts();
    }

    public List<Integer> getBaudRates() {
        return List.of(9600, 57600, 115200);
    }

    public boolean connect(String port, int baudRate) {
        var connected = service.connect(port, baudRate, this);
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
        service.disconnect();
    }

    public void sendCustomCmd(String cmd) {
        service.sendCmd(cmd);
    }

    public void sendLevelCmd(double width, double height, double offset) {
        service.startLeveling(width, height, offset);
    }

    public double[] getMachineCoords() {
        return service.getMachineCoords();
    }

    // TODO Design status checking policy
    private void startStatusChecker() {
        checkerTask = new TimerTask() { public void run() {
            service.sendCmd("?\r");
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
}
