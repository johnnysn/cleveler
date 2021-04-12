package com.hive.apps.cleveler.cnc.transform;

import com.hive.apps.cleveler.cnc.GCodeCommands;
import com.hive.apps.cleveler.cnc.transform.interpretation.CommandInterpreter;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Compensator {

    private List<String> cmds;
    private CompensationFuncion compensationFuncion;

    public Compensator(List<String> cmds, CompensationFuncion compensationFuncion) {
        this.setup(cmds, compensationFuncion);
    }

    public void setup(List<String> cmds, CompensationFuncion compensationFuncion) {
        this.cmds = cmds;
        this.compensationFuncion = compensationFuncion;
        x = y = z = 0;
        isImperial = false;
    }

    private double x, y, z;
    private boolean isImperial;

    public List<String> run() {
        var gcode = new ArrayList<String>();

        cmds.forEach(cmd -> processLine(gcode, cmd));

        return gcode;
    }

    private void processLine(List<String> gcode, String cmd) {
        if (CommandInterpreter.isGCode(cmd)) processGCode(gcode, cmd);
        else gcode.add(cmd);
    }

    private void processGCode(List<String> gcode, String cmd) {
        if (CommandInterpreter.isImperialUnitSystemSetup(cmd)) {
            isImperial = true;
            gcode.add(GCodeCommands.GCODE_UNIT_SYSTEM_SETUP_METRIC);
        } else if (CommandInterpreter.isMotionCommand(cmd)) {
            processMotionCmd(gcode, cmd);
        } else {
            gcode.add(cmd);
        }
    }

    private void processMotionCmd(List<String> gcode, String cmd) {
        var coords = CommandInterpreter.extractCoordinates(cmd);
        if (coords[0] != null) x = isImperial ? 25.4 * coords[0] : coords[0];
        if (coords[1] != null) y = isImperial ? 25.4 * coords[1] : coords[1];
        if (coords[2] != null) z = isImperial ? 25.4 * coords[2] : coords[2];
        gcode.add(CommandInterpreter.swapMotionCmd(cmd, x, y,
                compensationFuncion.compensateZ(new Point2D(x, y), z)));
    }

}
