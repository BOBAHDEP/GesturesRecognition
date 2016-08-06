package logic;

/**
 * Class to keep information about detected figure
 */
public class DetectedFigure {
    //TODO add type (ENUM)
    private int x;
    private int y;
    private int width;
    private int height;
    private String figureType;

    public String getFigureType() {
        return figureType;
    }

    public void setFigureType(String figureType) {
        this.figureType = figureType;
    }

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

    @Override
    public String toString() {
        return "DetectedFigure{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", figureType='" + figureType + '\'' +
                '}';
    }

    public double getDistance(DetectedFigure detectedFigure) {
        return Math.sqrt((x - detectedFigure.getX())*(x - detectedFigure.getX()) + (y - detectedFigure.getY())*(y - detectedFigure.getY()));
    }

    public double getSizeDifference(DetectedFigure detectedFigure) {
        return Math.sqrt((height - detectedFigure.getHeight())*(height - detectedFigure.getHeight()) +
                (width - detectedFigure.getWidth())*(width - detectedFigure.getWidth()));
    }
}
