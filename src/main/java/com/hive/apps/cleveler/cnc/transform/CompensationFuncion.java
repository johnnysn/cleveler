package com.hive.apps.cleveler.cnc.transform;

import com.hive.apps.cleveler.cnc.transform.structs.Board;
import com.hive.apps.cleveler.cnc.transform.structs.Quadrant;
import javafx.geometry.Point2D;

public class CompensationFuncion {

    public Board board;

    public CompensationFuncion(Board board) {
        this.board = board;
    }

    public double compensateZ(Point2D p, double z) {
        var quadrant = getQuadrant(p);

        return quadrant.getTriangle(p).z(p) + z;
    }

    private Quadrant getQuadrant(Point2D p) {
        if (board.q1.in(p))
            return board.q1;

        if (board.q2.in(p))
            return board.q2;

        if (board.q3.in(p))
            return board.q3;

        return board.q4;
    }
}
