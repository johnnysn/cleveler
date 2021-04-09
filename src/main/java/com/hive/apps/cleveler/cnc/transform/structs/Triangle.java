package com.hive.apps.cleveler.cnc.transform.structs;

import javafx.geometry.Point2D;

public class Triangle {

    public Point2D x1;
    public Point2D x2;
    public Point2D x3;

    public double a = 0;
    public double b = 0;
    public double c = 0;

    public double z(Point2D x) {
        return a*x.getX() + b*x.getY() + c;
    }


}
