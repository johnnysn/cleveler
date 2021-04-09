package com.hive.apps.cleveler.service;

import com.hive.apps.cleveler.cnc.CNCProbeHandler;
import javafx.geometry.Point2D;

import java.util.ArrayList;

class LevelingService {

    private final CNCProbeHandler probeHandler;

    LevelingService(CNCProbeHandler probeHandler) {
        this.probeHandler = probeHandler;
    }

    void execute(double width, double height, double offset) {
        var size_x = width - 2*offset;
        var size_y = height - 2*offset;
        var coords = new ArrayList<Point2D>();
        coords.add(new Point2D(offset, offset)); // 0
        coords.add(new Point2D(offset + size_x/2, offset)); // 1
        coords.add(new Point2D(offset + size_x, offset)); // 2
        coords.add(new Point2D(offset + size_x, offset + size_y/2)); // 3
        coords.add(new Point2D(offset + size_x/2, offset + size_y/2)); // 4
        coords.add(new Point2D(offset, offset + size_y/2)); // 5
        coords.add(new Point2D(offset, offset + size_y)); // 6
        coords.add(new Point2D(offset + size_x/2, offset + size_y)); // 7
        coords.add(new Point2D(offset + size_x, offset + size_y)); // 8
        probeHandler.startProbing(coords);
    }

}
