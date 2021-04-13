package com.hive.apps.cleveler.cnc;

import com.hive.apps.cleveler.communication.UartHandler;

import java.nio.charset.StandardCharsets;

public class CNCCommander {

    private final UartHandler uartHandler;

    public CNCCommander(UartHandler uartHandler) {
        this.uartHandler = uartHandler;
    }

    void sendSetZeroCommand() {
        var cmd = "G10 P0 L20 X0 Y0 Z0\r";
        uartHandler.write(cmd.getBytes(StandardCharsets.UTF_8));
    }

    void sendXYMotionCommand(double x, double y) {
        var cmd = "G1 G90 X"+ x +" Y"+ y +" F500\r";
        uartHandler.write(cmd.getBytes(StandardCharsets.UTF_8));
    }

    // G21 G91 Z5 F500
    // $J=G21G91Z2F500
    void sendZMotionCommand(double z) {
        var cmd = "$J=G21G90Z"+ z +"F500\r";
        uartHandler.write(cmd.getBytes(StandardCharsets.UTF_8));
    }

    // G38.2 Z-40 F40
    void sendProbingCommand(int speed) {
        var cmd = "G38.2 Z-40 F" + speed + "\r";
        uartHandler.write(cmd.getBytes(StandardCharsets.UTF_8));
    }
}
