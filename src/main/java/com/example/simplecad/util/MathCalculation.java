package com.example.simplecad.util;

import com.example.simplecad.figures.Point;

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

//    public static boolean pointInsideTriangle(Point point, Point v1, Point v2, Point v3) {
//
//    }

    private static double max(double... nums) {
        double max = nums[0];
        for (double n : nums) {
            if (n > max)
                max = n;
        }
        return max;
    }

    private static double min(double... nums) {
        double min = nums[0];
        for (double n : nums) {
            if (n < min)
                min = n;
        }
        return min;
    }
}
