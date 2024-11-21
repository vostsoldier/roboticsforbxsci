public class RightTriangle implements Shape {
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

    public static void main(String[] args) {
        Point corner1 = new Point(0, 0);
        RightTriangle triangle1 = new RightTriangle(corner1, 3, 4);

        Point corner2 = new Point(1, 1);
        RightTriangle triangle2 = new RightTriangle(corner2, 6, 8);

        System.out.println("Area of triangle1: " + triangle1.area());
        System.out.println("Perimeter of triangle1: " + triangle1.perimeter());

        Point insidePoint = new Point(1, 1);
        Point onPoint = new Point(3, 0);
        Point outsidePoint = new Point(5, 5);

        System.out.println("Is inside: " + triangle1.isInside(insidePoint));
        System.out.println("Is on: " + triangle1.isOn(onPoint));
        System.out.println("Is inside (outside point): " + triangle1.isInside(outsidePoint));

        RightTriangle translatedTriangle = (RightTriangle) triangle1.translate(2, 3);
        System.out.println("Translated Triangle Corner: (" + translatedTriangle.corner.getX() + ", " + translatedTriangle.corner.getY() + ")");

        RightTriangle scaledTriangle = (RightTriangle) triangle1.scale(2);
        System.out.println("Scaled Triangle SideA: " + scaledTriangle.sideA + ", SideB: " + scaledTriangle.sideB);

        System.out.println("Are triangle1 and triangle2 similar? " + RightTriangle.similar(triangle1, triangle2));
    }
}