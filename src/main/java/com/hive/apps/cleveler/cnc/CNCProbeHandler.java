package com.hive.apps.cleveler.cnc;

import com.hive.apps.cleveler.cnc.transform.CompensationFunctionBuilder;
import com.hive.apps.cleveler.controller.AbstractController;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class CNCProbeHandler {

    private static final double TRAVEL_Z = 2d;

    private static final int PROBING_INITIAL_POINT = 1;
    private static final int PROBING_FOUND_ZERO = 2;
    private static final int PROBING_FINISHED = 3;

    private final CNCCommander cncCommander;
    private final AbstractController controller;
    private List<Point2D> coords;
    private int phase;
    private int coordIndex;
    private int coordPhase;
    private List<Double> Z;
    private double z_probe_correction = 0;

    public CNCProbeHandler(CNCCommander cncCommander, AbstractController controller) {
        this.cncCommander = cncCommander;
        this.controller = controller;
        this.phase = 0;
        phase = coordIndex = coordPhase = 0;
        coords = null;
    }

    public void startProbing(List<Point2D> coords) {
        cncCommander.sendProbingCommand(20);
        this.coords = coords;
        this.Z = new ArrayList<>();
        phase = PROBING_INITIAL_POINT;
    }

    public void registerProbing(double z) {
        if (phase > PROBING_INITIAL_POINT)
            Z.add(z_probe_correction + z);
        else
            z_probe_correction = -z;
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
                cncCommander.sendZMotionCommand(TRAVEL_Z);
                phase = PROBING_FINISHED;
            }
        } else if (phase == PROBING_FINISHED) {
            cncCommander.sendXYZMotionCommand(0, 0, 1);
            phase = coordIndex = coordPhase = 0;
            finalizar();
        }
    }

    private void finalizar() {
        controller.showMessage("Z = "+ Z);
        controller.reportTask(
                    "Processo de probing terminado.",
                    CompensationFunctionBuilder.build(coords, Z)
                );
    }

    private void handleCoord(Point2D point2D) {
        if (coordPhase == 0) {
            cncCommander.sendZMotionCommand(TRAVEL_Z);
            coordPhase = 1;
        } else if (coordPhase == 1) {
            cncCommander.sendXYMotionCommand(point2D.getX(), point2D.getY());
            coordPhase = 2;
        } else if (coordPhase == 2) {
            cncCommander.sendProbingCommand(20);
            coordPhase = 3;
        } else if (coordPhase == 3) {
            // collect z
            coordPhase = 0;
            coordIndex++;
            handleProbing();
        }
    }

}
