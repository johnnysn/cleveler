package com.hive.apps.cleveler.cnc.transform.structs;

import javafx.geometry.Point2D;

public class Quadrant {

    private Triangle s1;
    private Triangle s2;

    private Point2D x1, x2, x3, x4;

    public Quadrant(Point2D x1, Point2D x2, Point2D x3, Point2D x4) {
        this.x1 = x1; this.x2 = x2; this.x3 = x3; this.x4 = x4;
        s1 = new Triangle();
        s1.x1 = x1; s1.x2 = x2; s1.x3 = x4;
        s2 = new Triangle();
        s2.x1 = x2; s2.x2 = x3; s2.x3 = x4;
        m = (x4.getY() - x2.getY()) / (x4.getX() - x2.getX());
    }

    public boolean in(Point2D p) {
        return
                (x1.getX() <= p.getX() && p.getX() <= x2.getX()) &&
                (x1.getY() <= p.getY() && p.getY() <= x4.getY());
    }

    public Triangle getLowerTriangle() {
        return s1;
    }

    public Triangle getUpperTriangle() {
        return s2;
    }

    /**
     * m é o coeficiente angular da reta formada pelos pontos x2 e x4
     */
    private double m;

    public Triangle getTriangle(Point2D p) {
        var pot = m * (p.getX() - x2.getX()) + x2.getY();
        if (p.getY() <= pot)
            return s1;
        return s2;
    }
}
