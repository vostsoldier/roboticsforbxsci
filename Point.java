public class Point {
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

    public static class Circle {
        private Point center;
        private double radius;

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

        public static void main(String[] args) {
            Point center = new Point(0, 0);
            Circle circle = new Circle(center, 5);

            System.out.println("Area: " + circle.area());
            System.out.println("Perimeter: " + circle.perimeter());

            Point insidePoint = new Point(3, 4);
            Point onPoint = new Point(5, 0);
            Point outsidePoint = new Point(6, 0);

            System.out.println("Is inside: " + circle.isInside(insidePoint));
            System.out.println("Is on: " + circle.isOn(onPoint));
            System.out.println("Is inside (outside point): " + circle.isInside(outsidePoint));

            Circle translatedCircle = circle.translate(2, 3);
            System.out.println("Translated Circle Center: (" + translatedCircle.center.getX() + ", " + translatedCircle.center.getY() + ")");

            Circle scaledCircle = circle.scale(2);
            System.out.println("Scaled Circle Radius: " + scaledCircle.radius);
        }
    }
}