package gui;

import logic.DetectedFigure;
import logic.Processor;
import org.opencv.core.Core;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Window {

    public static final int AMOUNT_OF_SAVED_FIGURES = 6;
    public static final int MAX_DISTANCE_BETWEEN_SAME_FIGURES = 50;
    public static final int MAX_DISTANCE_BETWEEN_SIZES = 50;
    //keep last figures for detection
    private Queue<DetectedFigure> detectedFigures = new LinkedList<>();
    private Processor processor = new Processor();

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws IllegalAccessException, InterruptedException {
        Window window = new Window();
        window.action();
    }

    //TODO type difference, pictures, button actions
    private void action() {
        int detectedFigureNumber = 0;   //to save. Each AMOUNT_OF_SAVED_FIGURES is set to 0
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainPanel mainPanel = new MainPanel();
                PictureFrame pictureFrame = new PictureFrame();
                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            detectedFigures.add(processor.processCam(mainPanel));
                            if (detectedFigures.size() == AMOUNT_OF_SAVED_FIGURES+1) {
                                detectedFigures.remove();
                            }
                            if (checkSavedFigures()) {
                                pictureFrame.setOK();
                            }
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                timer.start();
            }
        });

    }

    private boolean checkSavedFigures() {
        if (detectedFigures.size() != AMOUNT_OF_SAVED_FIGURES) {
//            System.out.println("detectedFigures.size =  " + detectedFigures.size());
            return false;
        }
        List<DetectedFigure> tempDetectedFigures = new ArrayList(detectedFigures);
        for (int i = 0; i < tempDetectedFigures.size(); i++) {
            for (int j = i; j < tempDetectedFigures.size(); j++) {
                if (tempDetectedFigures.get(i) == null || tempDetectedFigures.get(j) == null ||
                        tempDetectedFigures.get(i).getDistance(tempDetectedFigures.get(j)) > MAX_DISTANCE_BETWEEN_SAME_FIGURES ||
                        tempDetectedFigures.get(i).getSizeDifference(tempDetectedFigures.get(j)) > MAX_DISTANCE_BETWEEN_SIZES) {
                    return false;
                }
            }
        }
        return true;
    }
}