package logic;

/**
 * Class to keep information about detected figure
 */
public class DetectedFigure {
    //TODO add type (ENUM)
    int x;
    int y;
    int width;
    int height;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getDistance(DetectedFigure detectedFigure) {
        return Math.sqrt((x - detectedFigure.getX())*(x - detectedFigure.getX()) + (y - detectedFigure.getY())*(y - detectedFigure.getY()));
    }
}
