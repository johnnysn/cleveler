package com.hive.apps.cleveler.cnc.transform.interpretation;

import com.hive.apps.cleveler.cnc.GCodeCommands;
import com.hive.apps.cleveler.cnc.exception.InvalidGCODEException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandInterpreter {

    public static boolean isGCode(String cmd) {
        return cmd.startsWith(GCodeCommands.GCODE_COMMAND_PREFIX);
    }

    public static boolean isImperialUnitSystemSetup(String cmd) {
        return GCodeCommands.GCODE_UNIT_SYSTEM_SETUP_IMPERIAL.equals(cmd);
    }

    public static boolean isMotionCommand(String cmd) {
        return cmd.startsWith(GCodeCommands.GCODE_MOTION_SEEK)
                || cmd.startsWith(GCodeCommands.GCODE_MOTION_FEED);
    }

    public static Double[] extractCoordinates(String cmd) {
        var coords = new Double[]{null, null, null};
        Pattern pattern = Pattern.compile("[0-9.,-]+");
        var indexOfCoord = cmd.indexOf("X");
        if (indexOfCoord > 0) {
            coords[0] = extractValue(pattern, cmd.substring(indexOfCoord));
        }
        indexOfCoord = cmd.indexOf("Y");
        if (indexOfCoord > 0) {
            coords[1] = extractValue(pattern, cmd.substring(indexOfCoord));
        }
        indexOfCoord = cmd.indexOf("Z");
        if (indexOfCoord > 0) {
            coords[2] = extractValue(pattern, cmd.substring(indexOfCoord));
        }
        return coords;
    }

    private static Double extractValue(Pattern pattern, String shard) {
        Matcher matcher = pattern.matcher(shard);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(0));
        }
        return null;
    }

    public static String swapMotionCmd(String original, double x, double y, double z) {
        String cmd;
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        formatter.applyPattern("#0.0000");
        if (original.startsWith(GCodeCommands.GCODE_MOTION_FEED))
            cmd = GCodeCommands.GCODE_MOTION_FEED + " ";
        else if (original.startsWith(GCodeCommands.GCODE_MOTION_SEEK))
            cmd = GCodeCommands.GCODE_MOTION_SEEK + " ";
        else
            throw new InvalidGCODEException("Linha de comando de movimento inválida.");
        if (original.indexOf('X') >= 0)
            cmd += "X" + formatter.format(x);
        if (original.indexOf('Y') >= 0)
            cmd += "Y" + formatter.format(y);
        cmd += "Z" + formatter.format(z);
        return cmd;
    }
}
