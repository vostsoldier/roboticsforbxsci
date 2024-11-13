public class Book {
    private double x;
    private double y;

    public Book(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static Book centerOfMass(Book[] points) {
        if (points == null || points.length == 0) {
            throw new IllegalArgumentException("Array of points must not be null or empty");
        }

        double sumX = 0;
        double sumY = 0;

        for (Book point : points) {
            sumX += point.getX();
            sumY += point.getY();
        }

        double centerX = sumX / points.length;
        double centerY = sumY / points.length;

        return new Book(centerX, centerY);
    }

    public double angleWithXAxis() {
        double angleInRadians = Math.atan2(y, x);
        return Math.toDegrees(angleInRadians);
    }

    public Book rotate(double theta) {
        double radians = Math.toRadians(theta);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);
        double newX = x * cosTheta - y * sinTheta;
        double newY = x * sinTheta + y * cosTheta;
        return new Book(newX, newY);
    }

    public static void main(String[] args) {
        Book[] points = {
            new Book(1, 1),
            new Book(2, 2),
            new Book(3, 3)
        };

        Book center = Book.centerOfMass(points);
        System.out.println("Center of Mass: (" + center.getX() + ", " + center.getY() + ")");

        Book point = new Book(1, 1);
        System.out.println("Angle with X-Axis: " + point.angleWithXAxis() + " degrees");

        Book rotatedPoint90 = point.rotate(90);
        System.out.println("Rotated Point by 90 degrees: (" + rotatedPoint90.getX() + ", " + rotatedPoint90.getY() + ")");

        Book rotatedPoint45 = point.rotate(45);
        System.out.println("Rotated Point by 45 degrees: (" + rotatedPoint45.getX() + ", " + rotatedPoint45.getY() + ")");
    }
}