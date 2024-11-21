import java.util.ArrayList;

public class java102problems {
    private double x;
    private double y;

    public java102problems(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static java102problems centerOfMass(java102problems[] points) {
        if (points == null || points.length == 0) {
            throw new IllegalArgumentException("Array of points must not be null or empty");
        }

        double sumX = 0;
        double sumY = 0;

        for (java102problems point : points) {
            sumX += point.getX();
            sumY += point.getY();
        }

        double centerX = sumX / points.length;
        double centerY = sumY / points.length;

        return new java102problems(centerX, centerY);
    }

    public double angleWithXAxis() {
        double angleInRadians = Math.atan2(y, x);
        return Math.toDegrees(angleInRadians);
    }

    public java102problems rotate(double theta) {
        double radians = Math.toRadians(theta);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);
        double newX = x * cosTheta - y * sinTheta;
        double newY = x * sinTheta + y * cosTheta;
        return new java102problems(newX, newY);
    }

    public static void main(String[] args) {
        java102problems[] points = {
            new java102problems(1, 1),
            new java102problems(2, 2),
            new java102problems(3, 3)
        };

        java102problems center = java102problems.centerOfMass(points);
        System.out.println("Center of Mass: (" + center.getX() + ", " + center.getY() + ")");

        java102problems point = new java102problems(1, 1);
        System.out.println("Angle with X-Axis: " + point.angleWithXAxis() + " degrees");

        java102problems rotatedPoint90 = point.rotate(90);
        System.out.println("Rotated Point by 90 degrees: (" + rotatedPoint90.getX() + ", " + rotatedPoint90.getY() + ")");

        java102problems rotatedPoint45 = point.rotate(45);
        System.out.println("Rotated Point by 45 degrees: (" + rotatedPoint45.getX() + ", " + rotatedPoint45.getY() + ")");
        double[][] gridArray1 = {
            {1.1, 2.2, 3.3},
            {4.4, 5.5, 6.6},
            {7.7, 8.8, 9.9}
        };

        double[][] gridArray2 = {
            {1.1, 2.2},
            {3.3, 4.4}
        };

        Grid grid1 = new Grid(gridArray1);
        new Grid(gridArray2);

        ArrayList<Double> diagonal = grid1.diagonal();
        System.out.println("Primary Diagonal of grid1: " + diagonal);

        System.out.println("Biggest side length of any Grid: " + Grid.getMaxSideLength());
    }

    public static class Grid {
        private double[][] grid;
        private static int maxSideLength = 0;

        public Grid(double[][] grid) {
            this.grid = grid;
            int sideLength = Math.max(grid.length, grid[0].length);
            if (sideLength > maxSideLength) {
                maxSideLength = sideLength;
            }
        }
        public ArrayList<Double> diagonal() {
            ArrayList<Double> diagonalElements = new ArrayList<>();
            int n = Math.min(grid.length, grid[0].length); 
            for (int i = 0; i < n; i++) {
                diagonalElements.add(grid[i][i]);
            }
            return diagonalElements;
        }
        public static int getMaxSideLength() {
            return maxSideLength;
        }
    }
}