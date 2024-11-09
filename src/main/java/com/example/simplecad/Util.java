package com.example.simplecad;

import com.example.simplecad.figures.Point;

public class Util {
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
}
