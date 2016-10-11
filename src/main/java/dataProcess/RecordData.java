package dataProcess;

import logic.DetectedFigure;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * TO implement logic of data recording
 */
public class RecordData {

    private File logFile = new File("log.txt");

    public void recordStart(String typeOfFigure) {

    }

    public void recordDetected(DetectedFigure detectedFigure) {
        String msg = null;
        if (detectedFigure == null) {
            msg = "Error detectedFigure is null";
        } else {
            msg = detectedFigure.toString();
        }
        msg += Calendar.getInstance().toString();
        Charset utf8 = StandardCharsets.UTF_8;
        try {
            Files.write(Paths.get("log.txt"), Arrays.asList(msg), utf8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
