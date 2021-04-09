package com.hive.apps.cleveler.cnc.transform;

public class CoefficientCalculator {

    public static double[] calc(double[][] D, double[] z) {
        var coeffs = new double[3];
        var detD = det(D);
        coeffs[0] = calc(D, z, 0, detD);
        coeffs[1] = calc(D, z, 1, detD);
        coeffs[2] = calc(D, z, 2, detD);
        return coeffs;
    }

    public static double calc(double[][] D, double[] z, int coeffIndex) {
        return calc(D, z, coeffIndex, det(D));
    }

    private static double calc(double[][] D, double[] z, int coeffIndex, double detD) {
        double[][] Dc = {
                {D[0][0], D[0][1], D[0][2]},
                {D[1][0], D[1][1], D[1][2]},
                {D[2][0], D[2][1], D[2][2]}
        };

        Dc[0][coeffIndex] = z[0];
        Dc[1][coeffIndex] = z[1];
        Dc[2][coeffIndex] = z[2];

        return det(Dc) / detD;
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
