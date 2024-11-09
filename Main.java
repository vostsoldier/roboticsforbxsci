import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;

class Vector {
    private List<Point> points = new ArrayList<>();
    private List<Point> obstacles = new ArrayList<>();
    private List<Point> items = new ArrayList<>();
    private Point currentPoint = new Point(0, 0);
    private Point targetPoint = new Point(24, 24); 
    private Point dropOffPoint = new Point(12, 12); 
    private Random random = new Random();
    private boolean carryingItem = false;

    public Vector() {
        generateObstacles();
        generateItems();
        generateRandomTargetPoint();
    }

    private void generateObstacles() {
        for (int i = 0; i < 30; i++) {
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
                    Thread.sleep(500);
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
                pickUpItem(nextPoint);
            } else if (carryingItem && nextPoint.equals(dropOffPoint)) {
                dropOffItem(nextPoint);
            }
        }

        currentPoint = nextPoint;
        return nextPoint;
    }

    private void pickUpItem(Point point) {
        items.remove(point);
        carryingItem = true;
        System.out.println("Picked up item at: (" + point.getX() + ", " + point.getY() + ")");
        System.out.println("realjoint.io: Picked up item");
    }

    private void dropOffItem(Point point) {
        carryingItem = false;
        System.out.println("Dropped off item at: (" + point.getX() + ", " + point.getY() + ")");
        System.out.println("realjoint.io: Dropped off item");
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

    public Point getCurrentPoint() {
        return currentPoint;
    }

    public List<Point> getSensorPoints(int radius) {
        List<Point> sensorPoints = new ArrayList<>();
        for (int i = 1; i <= radius; i++) {
            sensorPoints.add(new Point(currentPoint.x + i, currentPoint.y)); 
            sensorPoints.add(new Point(currentPoint.x - i, currentPoint.y)); 
            sensorPoints.add(new Point(currentPoint.x, currentPoint.y + i));
            sensorPoints.add(new Point(currentPoint.x, currentPoint.y - i)); 
        }
        return sensorPoints;
    }
}
public class Main {
    public static void main(String[] args) {
        Vector vectorInstance = new Vector();
        vectorInstance.generateRandomTargetPoint();
        vectorInstance.startGeneratingCoordinates(); 

        JFrame frame = new JFrame("Robot Grid");
        GridPanel gridPanel = new GridPanel();
        frame.add(gridPanel);
        frame.setSize(520, 540);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Timer timer = new Timer(1000, e -> {
            List<Point> points = vectorInstance.getPoints();
            List<Point> obstacles = vectorInstance.getObstacles();
            List<Point> items = vectorInstance.getItems();
            Point dropOffPoint = vectorInstance.getDropOffPoint();
            List<Point> sensorPoints = vectorInstance.getSensorPoints(1); 
            gridPanel.updateGrid(points, obstacles, items, dropOffPoint, sensorPoints);
        });
        timer.start();
    }
}

class GridPanel extends JPanel {
    private List<Point> points;
    private List<Point> obstacles;
    private List<Point> items;
    private Point dropOffPoint;
    private List<Point> sensorPoints;

    public void updateGrid(List<Point> points, List<Point> obstacles, List<Point> items, Point dropOffPoint, List<Point> sensorPoints) {
        this.points = points;
        this.obstacles = obstacles;
        this.items = items;
        this.dropOffPoint = dropOffPoint;
        this.sensorPoints = sensorPoints;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        int cellSize = 20;
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }
        }

        if (obstacles != null) {
            g.setColor(Color.RED);
            for (Point obstacle : obstacles) {
                g.fillRect(obstacle.x * cellSize, obstacle.y * cellSize, cellSize, cellSize);
            }
        }

        if (items != null) {
            g.setColor(Color.BLUE);
            for (Point item : items) {
                g.fillRect(item.x * cellSize, item.y * cellSize, cellSize, cellSize);
            }
        }

        if (dropOffPoint != null) {
            g.setColor(Color.GREEN);
            g.fillRect(dropOffPoint.x * cellSize, dropOffPoint.y * cellSize, cellSize, cellSize);
        }

        if (points != null) {
            g.setColor(Color.BLACK);
            for (Point point : points) {
                g.fillRect(point.x * cellSize, point.y * cellSize, cellSize, cellSize);
            }
        }

        if (sensorPoints != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.setColor(Color.ORANGE);
            for (Point sensorPoint : sensorPoints) {
                g2d.fillRect(sensorPoint.x * cellSize, sensorPoint.y * cellSize, cellSize, cellSize);
            }
            g2d.dispose();
        }
    }
}