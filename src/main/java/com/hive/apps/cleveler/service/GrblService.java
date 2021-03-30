package com.hive.apps.cleveler.service;

import com.hive.apps.cleveler.cnc.CNCCommander;
import com.hive.apps.cleveler.cnc.CNCInterpreter;
import com.hive.apps.cleveler.cnc.CNCProbeHandler;
import com.hive.apps.cleveler.cnc.CNCStatus;
import com.hive.apps.cleveler.communication.UartHandler;
import com.hive.apps.cleveler.controller.AbstractController;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class GrblService {

    private UartHandler uartHandler;
    private CNCStatus cncStatus;
    private CNCInterpreter cncInterpreter;
    private LevelingService levelingService;

    public GrblService() {
        cncStatus = new CNCStatus();
        uartHandler = new UartHandler();
        var cncCommander = new CNCCommander(uartHandler);
        var probeHandler = new CNCProbeHandler(cncCommander);
        cncInterpreter = new CNCInterpreter(cncStatus, probeHandler);
        levelingService = new LevelingService(probeHandler);
    }

    public List<String> listAvailablePorts() {
        return uartHandler.listAvailablePorts();
    }

    public boolean connect(String port, int baudRate, AbstractController controller) {
        cncInterpreter.setController(controller);
        return uartHandler.connectToPort(port, baudRate, cncInterpreter);
    }

    public double[] getMachineCoords() {
        return new double[]{cncStatus.x, cncStatus.y, cncStatus.z};
    }

    public void sendCmd(String cmd) {
        uartHandler.write(cmd.getBytes(StandardCharsets.UTF_8));
    }

    public void disconnect() {
        uartHandler.disconnect();
    }

    public void startLeveling(double width, double height, double offset) {
        levelingService.execute(width, height, offset);
    }
}
