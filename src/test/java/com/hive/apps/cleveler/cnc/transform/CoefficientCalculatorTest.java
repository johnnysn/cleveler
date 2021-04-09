package com.hive.apps.cleveler.cnc.transform;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CoefficientCalculatorTest {

    @Test
    void deveCalcularCoeficientesCorretamente() {
        // arrange
        double[][] D = {
                {1d, 0, 1d},
                {1d, 1d, 0},
                {0, 1d, 1d}
        };
        double[] z = {9d, 7d, 6d};
        // act
        var a = CoefficientCalculator.calc(D, z, 0);
        var b = CoefficientCalculator.calc(D, z, 1);
        var c = CoefficientCalculator.calc(D, z, 2);
        // assert
        Assertions.assertEquals(5d, a, 1e-6);
        Assertions.assertEquals(2d, b, 1e-6);
        Assertions.assertEquals(4d, c, 1e-6);
    }

    @Test
    void deveCalcularCoeficientesCorretamente2() {
        // arrange
        double[][] D = {
                {1d, 0, -2d},
                {1d, 3d, 0},
                {0, 2d, 1d}
        };
        double[] z = {6d, 7d, 0d};
        // act
        var coeffs = CoefficientCalculator.calc(D, z);
        // assert
        Assertions.assertEquals(10d, coeffs[0], 1e-6);
        Assertions.assertEquals(-1d, coeffs[1], 1e-6);
        Assertions.assertEquals(2d, coeffs[2], 1e-6);
    }
}
