package com.hive.apps.cleveler.cnc.transform;

import com.hive.apps.cleveler.cnc.transform.structs.Triangle;
import javafx.geometry.Point2D;

import java.util.Map;

public class CoefficientCalculator {

    public static double calc(double[][] D, Map<Point2D, Double> Z, Triangle t, int coeffIndex) {
        double[][] Dc = {
                {D[0][0], D[0][1], D[0][2]},
                {D[1][0], D[1][1], D[1][2]},
                {D[2][0], D[2][1], D[2][2]}
        };

        Dc[0][coeffIndex] = Z.get(t.x1);
        Dc[1][coeffIndex] = Z.get(t.x2);
        Dc[2][coeffIndex] = Z.get(t.x3);

        return det(Dc) / det(D);
    }

    private static double det(double[][] M) {
        return M[0][0] * M[1][1] * M[2][2] +
                M[0][1] * M[1][2] * M[2][0] +
                M[0][2] * M[1][0] * M[2][1] - (
                    M[2][0] * M[1][1] * M[0][2] +
                    M[2][1] * M[1][2] * M[0][0] +
                    M[2][2] * M[1][0] * M[0][1]
                );
    }

}
