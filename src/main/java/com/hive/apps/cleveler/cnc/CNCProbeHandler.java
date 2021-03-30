package com.hive.apps.cleveler.cnc;

import javafx.geometry.Point2D;
import java.util.List;

public class CNCProbeHandler {

    private static final int PROBING_INITIAL_POINT = 1;
    private static final int PROBING_FOUND_ZERO = 2;
    private static final int PROBING_FINISHED = 3;

    private final CNCCommander cncCommander;
    private List<Point2D> coords;
    private int phase;
    private int coordIndex;
    private int coordPhase;

    private double z;

    public CNCProbeHandler(CNCCommander cncCommander) {
        this.cncCommander = cncCommander;
        this.phase = 0;
        phase = coordIndex = coordPhase = 0;
        coords = null;
    }

    public void startProbing(List<Point2D> coords) {
        cncCommander.sendProbingCommand(40);
        this.coords = coords;
        phase = PROBING_INITIAL_POINT;
    }

    public void registerProbing(double z) {
        this.z = z;
    }

    public void handleProbing() {
        if (phase == PROBING_INITIAL_POINT) {
            cncCommander.sendSetZeroCommand();
            phase = PROBING_FOUND_ZERO;
            coordIndex = coordPhase = 0;
        } else if (phase == PROBING_FOUND_ZERO) {
            if (coordIndex < coords.size())
                handleCoord(coords.get(coordIndex));
            else {
                cncCommander.sendZMotionCommand(5);
                phase = PROBING_FINISHED;
            }
        } else if (phase == PROBING_FINISHED) {
            cncCommander.sendXYMotionCommand(0, 0);
            phase = coordIndex = coordPhase = 0;
        }
    }

    private void handleCoord(Point2D point2D) {
        if (coordPhase == 0) {
            cncCommander.sendZMotionCommand(5);
            coordPhase = 1;
        } else if (coordPhase == 1) {
            cncCommander.sendXYMotionCommand(point2D.getX(), point2D.getY());
            coordPhase = 2;
        } else if (coordPhase == 2) {
            cncCommander.sendProbingCommand(40);
            coordPhase = 3;
        } else if (coordPhase == 3) {
            // collect z
            coordPhase = 0;
            coordIndex++;
            handleProbing();
        }
    }

}
