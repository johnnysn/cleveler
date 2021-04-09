package com.hive.apps.cleveler.cnc.transform;

import java.util.ArrayList;
import java.util.List;

public class Compensator {

    public static List<String> compensateGCode(List<String> lines, CompensationFuncion function) {
        var gcode = new ArrayList<String>();

        var z = 0d;
        for (var cmd : lines) {

        }

        return gcode;
    }

}
