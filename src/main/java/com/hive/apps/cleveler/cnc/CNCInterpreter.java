package com.hive.apps.cleveler.cnc;

import com.hive.apps.cleveler.communication.listener.UartReader;
import com.hive.apps.cleveler.controller.AbstractController;

import java.util.Locale;
import java.util.regex.Pattern;

public class CNCInterpreter implements UartReader {

    private CNCStatus cncStatus;
    private CNCProbeHandler probeHandler;
    private AbstractController controller;

    public void setController(AbstractController controller) {
        this.controller = controller;
    }

    private Pattern statusPattern;
    private Pattern probePattern;

    public CNCInterpreter(CNCStatus cncStatus, CNCProbeHandler probeHandler) {
        this.cncStatus = cncStatus;
        this.probeHandler = probeHandler;
        statusPattern = Pattern.compile("\\|MPos:([0-9.,-]+)\\|");
        probePattern = Pattern.compile("\\[PRB:([0-9.,-]+):");
        filterPattern = Pattern.compile("[A-Za-z0-9:.,|-]+>");
    }

    /*
    * Ex of response to '?': <Alarm|MPos:0.000,0.000,-40.000|FS:0,0> plus other lines
    * Ex of response to probe G38.2 Z-40 F40: [PRB:0.000,0.000,-4.707:1]
    */
    @Override
    public void recvData(String data) {
        var myData = data.trim();
        if (!filter(myData)) {
            handleStatus(myData);
        }
    }

    private void handleStatus(String myData) {
        var matcher = statusPattern.matcher(myData);
        if (matcher.find()) {
            var coordStr = matcher.group(1).split(",");
            cncStatus.x = Double.parseDouble(coordStr[0]);
            cncStatus.y = Double.parseDouble(coordStr[1]);
            cncStatus.z = Double.parseDouble(coordStr[2]);
            return;
        }
        matcher = probePattern.matcher(myData);
        if (matcher.find()) {
            var z = Double.parseDouble(matcher.group(1).split(",")[2]);
            probeHandler.registerProbing(z);
            report("Probing finished. Z = " + z);
        }
        if (myData.toUpperCase(Locale.ROOT).contains("OK")) {
            probeHandler.handleProbing();
            report("OK");
        }
    }

    private void report(String msg) {
        if (controller != null)
            controller.showMessage(msg + "\n");
    }

    private Pattern filterPattern;
    private boolean filter(String myData) {
        var matcher = filterPattern.matcher(myData);
        return matcher.matches();
    }
}
