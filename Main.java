import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Vector {
    private List<Point> points = new ArrayList<>();
    private List<Point> obstacles = new ArrayList<>();
    private List<Point> items = new ArrayList<>();
    private Point currentPoint = new Point(0, 0);
    private Point targetPoint = new Point(24, 24); // Default target point
    private Point dropOffPoint = new Point(12, 12); // Drop-off location
    private Random random = new Random();
    private boolean carryingItem = false;

    public Vector() {
        generateObstacles();
        generateItems();
        generateRandomTargetPoint();
    }

    private void generateObstacles() {
        for (int i = 0; i < 10; i++) {
            Point obstacle = new Point(random.nextInt(25), random.nextInt(25));
            obstacles.add(obstacle);
        }
    }

    private void generateItems() {
        for (int i = 0; i < 5; i++) {
            Point item = new Point(random.nextInt(25), random.nextInt(25));
            items.add(item);
        }
    }

    public void setTargetPoint(Point targetPoint) {
        this.targetPoint = targetPoint;
    }

    public void generateRandomTargetPoint() {
        this.targetPoint = new Point(random.nextInt(25), random.nextInt(25));
    }

    public void startGeneratingCoordinates() {
        Thread generateThread = new Thread(() -> {
            try {
                while (true) {
                    Point nextPoint = getNextPoint();
                    synchronized (points) {
                        points.add(nextPoint);
                    }
                    System.out.println("Generated Coordinates: (" + nextPoint.getX() + ", " + nextPoint.getY() + ")");
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                System.out.println("Exception is caught");
            }
        });
        generateThread.start();
    }

    private Point getNextPoint() {
        if (!carryingItem && !items.isEmpty()) {
            targetPoint = findNearestItem();
        } else if (carryingItem) {
            targetPoint = dropOffPoint;
        }

        int dx = targetPoint.x - currentPoint.x;
        int dy = targetPoint.y - currentPoint.y;
        int stepX = Integer.signum(dx);
        int stepY = Integer.signum(dy);

        Point nextPoint = new Point(currentPoint.x + stepX, currentPoint.y + stepY);

        if (isObstacle(nextPoint)) {
            nextPoint = avoidObstacle(nextPoint);
        }

        if (nextPoint.equals(targetPoint)) {
            if (!carryingItem && items.contains(nextPoint)) {
                items.remove(nextPoint);
                carryingItem = true;
            } else if (carryingItem && nextPoint.equals(dropOffPoint)) {
                carryingItem = false;
            }
        }

        currentPoint = nextPoint;
        return nextPoint;
    }

    private Point findNearestItem() {
        Point nearestItem = null;
        double minDistance = Double.MAX_VALUE;
        for (Point item : items) {
            double distance = currentPoint.distance(item);
            if (distance < minDistance) {
                minDistance = distance;
                nearestItem = item;
            }
        }
        return nearestItem;
    }

    private boolean isObstacle(Point point) {
        return obstacles.contains(point);
    }

    private Point avoidObstacle(Point point) {
        Point[] alternatives = {
            new Point(point.x + 1, point.y),
            new Point(point.x - 1, point.y),
            new Point(point.x, point.y + 1),
            new Point(point.x, point.y - 1)
        };

        for (Point alternative : alternatives) {
            if (!isObstacle(alternative) && isValidPoint(alternative)) {
                return alternative;
            }
        }
        return point;
    }

    private boolean isValidPoint(Point point) {
        return point.x >= 0 && point.x < 25 && point.y >= 0 && point.y < 25;
    }

    public List<Point> getPoints() {
        synchronized (points) {
            return new ArrayList<>(points);
        }
    }

    public List<Point> getObstacles() {
        return obstacles;
    }

    public List<Point> getItems() {
        return items;
    }

    public Point getDropOffPoint() {
        return dropOffPoint;
    }

    public List<Point> getSensorPoints(int radius) {
        List<Point> sensorPoints = new ArrayList<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (dx == 0 && dy == 0) continue; // Skip the current point
                Point sensorPoint = new Point(currentPoint.x + dx, currentPoint.y + dy);
                if (isValidPoint(sensorPoint)) {
                    sensorPoints.add(sensorPoint);
                }
            }
        }
        return sensorPoints;
    }
}
public class Main {
    public static void main(String[] args) {
        Vector vectorInstance = new Vector();
        vectorInstance.generateRandomTargetPoint(); // Generate a random target point
        vectorInstance.startGeneratingCoordinates(); 
        Thread printThread = new Thread(() -> {
            while (true) {
                List<Point> points = vectorInstance.getPoints();
                List<Point> obstacles = vectorInstance.getObstacles();
                List<Point> items = vectorInstance.getItems();
                Point dropOffPoint = vectorInstance.getDropOffPoint();
                List<Point> sensorPoints = vectorInstance.getSensorPoints(2); // Sensor radius of 2
                printCoordinatePlane(points, obstacles, items, dropOffPoint, sensorPoints);
                try {
                    Thread.sleep(5000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        printThread.start();
    }

    private static void printCoordinatePlane(List<Point> points, List<Point> obstacles, List<Point> items, Point dropOffPoint, List<Point> sensorPoints) {
        char[][] plane = new char[25][25];
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                plane[i][j] = '.';
            }
        }

        for (Point obstacle : obstacles) {
            int x = (int) obstacle.getX();
            int y = (int) obstacle.getY();
            if (x < 25 && y < 25) {
                plane[y][x] = 'O';
            }
        }

        for (Point item : items) {
            int x = (int) item.getX();
            int y = (int) item.getY();
            if (x < 25 && y < 25) {
                plane[y][x] = 'I';
            }
        }

        int dropOffX = (int) dropOffPoint.getX();
        int dropOffY = (int) dropOffPoint.getY();
        if (dropOffX < 25 && dropOffY < 25) {
            plane[dropOffY][dropOffX] = 'D';
        }

        for (Point point : points) {
            int x = (int) point.getX();
            int y = (int) point.getY();
            if (x < 25 && y < 25) {
                plane[y][x] = 'X';
            }
        }

        for (Point sensorPoint : sensorPoints) {
            int x = (int) sensorPoint.getX();
            int y = (int) sensorPoint.getY();
            if (x < 25 && y < 25) {
                if (isObstacle(sensorPoint, obstacles)) {
                    plane[y][x] = '!';
                } else {
                    plane[y][x] = 'S';
                }
            }
        }

        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                System.out.print(plane[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static boolean isObstacle(Point point, List<Point> obstacles) {
        return obstacles.contains(point);
    }
}