public class shapes {
    public static void main(String[] args) {
        Point center = new Point(0, 0);
        Circle circle = new Circle(center, 5);
        System.out.println("Circle Area: " + circle.area());
        System.out.println("Circle Perimeter: " + circle.perimeter());
        Point insidePoint = new Point(3, 4);
        Point onPoint = new Point(5, 0);
        Point outsidePoint = new Point(6, 0);
        System.out.println("Is inside: " + circle.isInside(insidePoint));
        System.out.println("Is on: " + circle.isOn(onPoint));
        System.out.println("Is inside (outside point): " + circle.isInside(outsidePoint));
        Circle translatedCircle = circle.translate(2, 3);
        System.out.println("Translated Circle: " + translatedCircle);
        Circle scaledCircle = circle.scale(2);
        System.out.println("Scaled Circle: " + scaledCircle);
        Point corner1 = new Point(0, 0);
        RightTriangle triangle1 = new RightTriangle(corner1, 3, 4);
        Point corner2 = new Point(1, 1);
        RightTriangle triangle2 = new RightTriangle(corner2, 6, 8);
        System.out.println("Triangle1 Area: " + triangle1.area());
        System.out.println("Triangle1 Perimeter: " + triangle1.perimeter());
        System.out.println("Are triangle1 and triangle2 similar? " + RightTriangle.similar(triangle1, triangle2));
    }
}

class Point {
    private double x;
    private double y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
    }

    public Point translateX(double dx) {
        return new Point(this.x + dx, this.y);
    }

    public Point translateY(double dy) {
        return new Point(this.x, this.y + dy);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

class Circle {
    public final Point center;
    public final double radius;

    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public double area() {
        return Math.PI * Math.pow(radius, 2);
    }

    public double perimeter() {
        return 2 * Math.PI * radius;
    }

    public boolean isInside(Point p) {
        return Point.distance(center, p) < radius;
    }

    public boolean isOn(Point p) {
        return Point.distance(center, p) == radius;
    }

    public Circle translate(double x, double y) {
        return new Circle(center.translateX(x).translateY(y), radius);
    }

    public Circle scale(double k) {
        return new Circle(center, radius * k);
    }

    @Override
    public String toString() {
        return "(center: " + center + "; radius: " + radius + ")";
    }
}

class RightTriangle implements Shape {
    private Point corner;
    private double sideA;
    private double sideB;

    public RightTriangle(Point corner, double sideA, double sideB) {
        this.corner = corner;
        this.sideA = sideA;
        this.sideB = sideB;
    }

    @Override
    public double area() {
        return 0.5 * sideA * sideB;
    }

    @Override
    public double perimeter() {
        double hypotenuse = Math.sqrt(sideA * sideA + sideB * sideB);
        return sideA + sideB + hypotenuse;
    }

    @Override
    public boolean isInside(Point p) {
        double x = p.getX();
        double y = p.getY();
        double cornerX = corner.getX();
        double cornerY = corner.getY();

        return (x >= cornerX && x <= cornerX + sideA && y >= cornerY && y <= cornerY + sideB) ||
               (x >= cornerX && x <= cornerX + sideB && y >= cornerY && y <= cornerY + sideA);
    }

    @Override
    public boolean isOn(Point p) {
        double x = p.getX();
        double y = p.getY();
        double cornerX = corner.getX();
        double cornerY = corner.getY();

        return (x == cornerX && y >= cornerY && y <= cornerY + sideB) ||
               (x == cornerX + sideA && y >= cornerY && y <= cornerY + sideB) ||
               (y == cornerY && x >= cornerX && x <= cornerX + sideA) ||
               (y == cornerY + sideB && x >= cornerX && x <= cornerX + sideA) ||
               (x == cornerX && y >= cornerY && y <= cornerY + sideA) ||
               (x == cornerX + sideB && y >= cornerY && y <= cornerY + sideA) ||
               (y == cornerY && x >= cornerX && x <= cornerX + sideB) ||
               (y == cornerY + sideA && x >= cornerX && x <= cornerX + sideB);
    }

    @Override
    public Shape translate(double x, double y) {
        return new RightTriangle(corner.translateX(x).translateY(y), sideA, sideB);
    }

    @Override
    public Shape scale(double k) {
        return new RightTriangle(corner, sideA * k, sideB * k);
    }

    public static boolean similar(RightTriangle t1, RightTriangle t2) {
        double ratio1 = t1.sideA / t2.sideA;
        double ratio2 = t1.sideB / t2.sideB;
        return Math.abs(ratio1 - ratio2) < 1e-9;
    }
}

interface Shape {
    double area();
    double perimeter();
    boolean isInside(Point p);
    boolean isOn(Point p);
    Shape translate(double x, double y);
    Shape scale(double k);
}