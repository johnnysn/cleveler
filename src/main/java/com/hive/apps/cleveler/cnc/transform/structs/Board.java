package com.hive.apps.cleveler.cnc.transform.structs;


import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  ----------------------
 *  |         |          |
 *  |   q4    |    q3    |
 *  |---------------------
 *  |         |          |
 *  |   q1    |    q2    |
 *  ----------------------
 */


public class Board {

    public Quadrant q1;
    public Quadrant q2;
    public Quadrant q3;
    public Quadrant q4;

    public Map<Point2D, Double> Z;

    public Board(List<Point2D> coords, List<Double> z) {
        q1 = new Quadrant(coords.get(0), coords.get(1), coords.get(4), coords.get(5));
        q2 = new Quadrant(coords.get(1), coords.get(2), coords.get(3), coords.get(4));
        q3 = new Quadrant(coords.get(4), coords.get(3), coords.get(8), coords.get(7));
        q4 = new Quadrant(coords.get(5), coords.get(4), coords.get(7), coords.get(6));
        Z = new HashMap<>();
        for (int i=0; i<coords.size(); i++) {
            Z.put(coords.get(i), z.get(i));
        }
    }
}
