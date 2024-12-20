package com.example.simplecad.util;

import com.example.simplecad.figures.Point;

import java.util.ArrayList;
import java.util.List;

public class MathCalculation {
    public static double getPointsDistance(Point point1, Point point2) {
        double a = Math.abs(point1.getX() - point2.getX());
        double b = Math.abs(point1.getY() - point2.getY());
        return Math.sqrt(a * a + b * b);
    }

    public static double[] getSolution(double[][] a, double[] b) {
        int n = b.length;
        double[] x = new double[n];

        for (int i = 0; i < n; i++) {
            for (int k = i + 1; k < n; k++) {
                double coef = a[k][i] / a[i][i];
                for (int j = i; j < n; j++)
                    a[k][j] = a[k][j] - coef * a[i][j];
                b[k] = b[k] - coef * b[i];
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++)
                sum += a[i][j] * x[j];
            x[i] = (b[i] - sum) / a[i][i];
        }

        return x;
    }

    public static double[] getCenterAndRadius(Point point1, Point point2, Point point3) {
        double[][] a = {
                {2 * point1.getX(), 2 * point1.getY(), 1},
                {2 * point2.getX(), 2 * point2.getY(), 1},
                {2 * point3.getX(), 2 * point3.getY(), 1}
        };
        double[] b = {
                point1.getX() * point1.getX() + point1.getY() * point1.getY(),
                point2.getX() * point2.getX() + point2.getY() * point2.getY(),
                point3.getX() * point3.getX() + point3.getY() * point3.getY()
        };
        double[] x = getSolution(a, b);

        double centerX = x[0];
        double centerY = x[1];
        double radius = Math.sqrt(x[0] * x[0] + x[1] * x[1] + x[2]);

        return new double[]{centerX, centerY, radius};
    }

    public static List<Point> findLineCircleIntersections(Point center, double radius, Point point1, Point point2) {
        double cx = center.getX();
        double cy = center.getY();

        double x1 = point1.getX();
        double y1 = point1.getY();
        double x2 = point2.getX();
        double y2 = point2.getY();

        double k = (y2 - y1) / (x2 - x1);
        double b = y1 - k * x1;

        double A = 1 + k * k;
        double B = 2 * k * (b - cy) - 2 * cx;
        double C = cx * cx + (b - cy) * (b - cy) - radius * radius;

        double D = B * B - 4 * A * C;

        if (D < 0)
            return new ArrayList<>();

        if (D == 0) {
            double xIntersect = B / (2 * A);
            double yIntersect = k * xIntersect + b;
            return List.of(new Point(xIntersect, yIntersect));
        }

        double sqrtD = Math.sqrt(D);
        double xIntersect1 = (-B + sqrtD) / (2 * A);
        double yIntersect1 = k * xIntersect1 + b;
        double xIntersect2 = (-B - sqrtD) / (2 * A);
        double yIntersect2 = k * xIntersect2 + b;
        Point intersection1 = new Point(xIntersect1, yIntersect1);
        Point intersection2 = new Point(xIntersect2, yIntersect2);

        return List.of(intersection1, intersection2);
    }

    public static Point getMiddlePoint(Point p1, Point p2) {
        double x = p1.getX() + 0.5 * (p2.getX() - p1.getX());
        double y = p1.getY() + 0.5 * (p2.getY() - p1.getY());
        return new Point(x, y);
    }
}
