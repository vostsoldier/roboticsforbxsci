import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

class Vector {
    private List<Point> points = new ArrayList<>();

    public void startGeneratingCoordinates() {
        Thread generateThread = new Thread(() -> {
            try {
                while (true) {
                    Point point = new Point();
                    point.setLocation(Math.random() * 25, Math.random() * 25); // Limit to 50 by 50 plane
                    synchronized (points) {
                        points.add(point);
                    }
                    System.out.println("Generated Coordinates: (" + point.getX() + ", " + point.getY() + ")");
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                System.out.println("Exception is caught");
            }
        });
        generateThread.start();
    }

    public List<Point> getPoints() {
        synchronized (points) {
            return new ArrayList<>(points);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Vector vectorInstance = new Vector();
        vectorInstance.startGeneratingCoordinates(); 
        Thread printThread = new Thread(() -> {
            while (true) {
                List<Point> points = vectorInstance.getPoints();
                printCoordinatePlane(points);
                try {
                    Thread.sleep(5000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        printThread.start();
    }

    private static void printCoordinatePlane(List<Point> points) {
        char[][] plane = new char[25][25];
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                plane[i][j] = '.';
            }
        }

        for (Point point : points) {
            int x = (int) point.getX();
            int y = (int) point.getY();
            if (x < 25 && y < 25) {
                plane[y][x] = 'X';
            }
        }

        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                System.out.print(plane[i][j] + " ");
            }
            System.out.println();
        }
    }
}