package com.hive.apps.cleveler.cnc.transform;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class CompensatorTest {

    private final CompensationFuncion compensationFunction;
    private final Compensator compensator;
    private final List<String> cmds;

    public CompensatorTest() {
        compensationFunction = mock(CompensationFuncion.class);
        cmds = generateCmds();

        compensator = new Compensator(cmds, compensationFunction);
    }

    private List<String> generateCmds() {
        var cmds = new ArrayList<String>();
        cmds.add("G20");
        cmds.add("G90");
        cmds.add("G94");
        cmds.add("F4.00");
        cmds.add("G00 Z0.1000");
        cmds.add("G00 X0.0278Y-0.1116");
        cmds.add("G01 Z-0.0020");
        cmds.add("G01 X0.0205Y-0.1067");
        cmds.add("G01 X0.0127Y-0.1035");
        return cmds;
    }

    @Test
    void deveCompensarCorretamente() {
        // arrange
        when(compensationFunction.compensateZ(any(Point2D.class), anyDouble())).thenAnswer(i -> calculateNewZ(
                i.getArgument(0), i.getArgument(1)
        ));
        // act
        var newCmds = compensator.run();
        // assert
        Assertions.assertEquals(cmds.size(), newCmds.size());
    }

    private double calculateNewZ(Point2D p, double z) {
        return z + p.getX()*0.001;
    }
}
