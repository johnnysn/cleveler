package com.hive.apps.cleveler.cnc.transform.structs;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Quadrant {

    public Triangle s1;
    public Triangle s2;

    public List<Point2D> points;

    public Quadrant(Point2D x1, Point2D x2, Point2D x3, Point2D x4) {
        points = new ArrayList<>();
        points.add(x1); points.add(x2); points.add(x3); points.add(x4);
        s1 = new Triangle();
        s1.x1 = x1; s1.x2 = x2; s1.x3 = x4;
        s2 = new Triangle();
        s2.x1 = x2; s2.x2 = x3; s2.x3 = x4;
    }
}
