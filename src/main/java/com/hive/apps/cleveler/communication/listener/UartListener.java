package com.hive.apps.cleveler.communication.listener;

import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import static com.fazecast.jSerialComm.SerialPort.LISTENING_EVENT_DATA_RECEIVED;
import static com.fazecast.jSerialComm.SerialPort.LISTENING_EVENT_DATA_WRITTEN;

public class UartListener implements SerialPortDataListener {


    private UartReader reader;

    public void setReader(UartReader reader) {
        this.reader = reader;
    }

    @Override
    public int getListeningEvents() {
        return LISTENING_EVENT_DATA_RECEIVED | LISTENING_EVENT_DATA_WRITTEN;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == LISTENING_EVENT_DATA_RECEIVED) {
            byte[] newData = event.getReceivedData();
            String line = "";
            for (int i = 0; i < newData.length; ++i)
                line += (char)newData[i];
            final var data = line;
            if (reader != null)
                reader.recvData(data);
        } else if (event.getEventType() == LISTENING_EVENT_DATA_WRITTEN) {
        }
    }
}
