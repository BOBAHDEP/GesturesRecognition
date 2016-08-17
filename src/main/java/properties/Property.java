package properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * get properties for program
 */
public class Property {
    private static FileInputStream fis;
    private static Properties properties = new Properties();
    static {
        try {
            fis = new FileInputStream("config.properties");
            properties.load(fis);
        } catch (IOException e) {
            properties = null;
            System.err.println("error: file with properties does not exist!");
        }
    }

    public static String getLibPath() {
        if (properties == null) {
            return null;
        }
        return properties.getProperty("libPath");
    }

    public static int getFrameRate() {
        if (properties == null) {
            throw new IllegalArgumentException("config.properties was not found");
        }
        return Integer.parseInt(properties.getProperty("frameRate"));
    }

    public static int getAmountOfSavedFigures() {
        if (properties == null) {
            throw new IllegalArgumentException("config.properties was not found");
        }
        return Integer.parseInt(properties.getProperty("amountOfSavedFigures"));
    }

    public static int getMaxDistanceBetweenSameFigures() {
        if (properties == null) {
            throw new IllegalArgumentException("config.properties was not found");
        }
        return Integer.parseInt(properties.getProperty("maxDistanceBetweenSameFigures"));
    }

    public static int getMaxDistanceBetweenSizes() {
        if (properties == null) {
            throw new IllegalArgumentException("config.properties was not found");
        }
        return Integer.parseInt(properties.getProperty("maxDistanceBetweenSizes"));
    }
}
