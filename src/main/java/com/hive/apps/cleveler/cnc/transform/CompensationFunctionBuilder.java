package com.hive.apps.cleveler.cnc.transform;

import com.hive.apps.cleveler.cnc.transform.structs.Board;
import com.hive.apps.cleveler.cnc.transform.structs.Quadrant;
import com.hive.apps.cleveler.cnc.transform.structs.Triangle;
import javafx.geometry.Point2D;

import java.util.List;
import java.util.Map;

public class CompensationFunctionBuilder {

    private CompensationFunctionBuilder() {

    }

    public static CompensationFuncion build(List<Point2D> coords, List<Double> Z) {
        var board = new Board(coords, Z);

        adjustPlanes(board.Z, board.q1);
        adjustPlanes(board.Z, board.q2);
        adjustPlanes(board.Z, board.q3);
        adjustPlanes(board.Z, board.q4);

        return new CompensationFuncion(board);
    }

    private static void adjustPlanes(Map<Point2D, Double> Z, Quadrant q) {
        adjustTriangle(Z, q.getLowerTriangle());
        adjustTriangle(Z, q.getUpperTriangle());
    }

    private static void adjustTriangle(Map<Point2D, Double> Z, Triangle t) {
        double[][] D = {
                {t.x1.getX(), t.x1.getY(), 1d},
                {t.x2.getX(), t.x2.getY(), 1d},
                {t.x3.getX(), t.x3.getY(), 1d}
        };

        var z = new double[]{Z.get(t.x1), Z.get(t.x2), Z.get(t.x3)};
        t.a = CoefficientCalculator.calc(D, z, 0);
        t.b = CoefficientCalculator.calc(D, z, 1);
        t.c = CoefficientCalculator.calc(D, z, 2);
    }

}
