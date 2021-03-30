package com.hive.apps.cleveler.communication;

import com.fazecast.jSerialComm.SerialPort;
import com.hive.apps.cleveler.communication.listener.UartListener;
import com.hive.apps.cleveler.communication.listener.UartReader;

import java.util.ArrayList;
import java.util.List;

public class UartHandler {

    private SerialPort connectedPort;
    private UartListener listener;

    public List<String> listAvailablePorts() {
        var list = new ArrayList<String>();
        var ports = SerialPort.getCommPorts();
        for (var port : ports) {
            list.add(port.getSystemPortName());
        }
        return list;
    }

    public boolean connectToPort(String label, int baudRate, UartReader reader) {
        var ports = SerialPort.getCommPorts();
        for (var port : ports) {
            if (port.getSystemPortName().equals(label)) {
                connectedPort = port;
                port.setBaudRate(baudRate);
                if (port.openPort()) {
                    listener = new UartListener();
                    listener.setReader(reader);
                    port.addDataListener(listener);
                    return true;
                }
            }
        }
        connectedPort = null;
        return false;
    }

    public synchronized void write(byte[] buffer) {
        if (connectedPort != null) {
            connectedPort.writeBytes(buffer, buffer.length);
        }
    }

    public void disconnect() {
        if (connectedPort != null) {
            connectedPort.removeDataListener();
            connectedPort.closePort();
        }
    }
}
