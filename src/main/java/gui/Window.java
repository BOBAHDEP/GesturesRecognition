package gui;

import dataProcess.RecordData;
import logic.DetectedFigure;
import logic.Processor;
import org.opencv.core.Core;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class Window {

    public static final int AMOUNT_OF_SAVED_FIGURES = 6;
    public static final int MAX_DISTANCE_BETWEEN_SAME_FIGURES = 60;
    public static final int MAX_DISTANCE_BETWEEN_SIZES = 60;
    public static final String OK_STATE_NAME = "OK";
    public static final String DEFAULT_STATE_NAME = "Default";
    public static final String FIST_LETTER = "F";
    public static final String FIST_NAME = "fist";
    public static final String PALM_NAME = "palm";
    public static final String PALM_LETTER = "P";
    public static final int TIMER_DELAY = 1000;

    //keep last figures for detection
    private Queue<DetectedFigure> detectedFigures = new LinkedList<>();
    private Processor processor = new Processor();

    //windows
    MainPanel mainPanel = null;
    PictureFrame pictureFrame = null;

    //to record data about events
    RecordData recordData = new RecordData();

    //state: "default" in default mode, name of figure in guessing mode, "OK" in OK mode
    String state = null;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws IllegalAccessException, InterruptedException {
        Window window = new Window();
        window.action();
    }

    private void action() {

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                processKeyEvent(e);
            }
        };

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainPanel = new MainPanel(keyAdapter);

                pictureFrame = new PictureFrame();
                pictureFrame.addKeyListener(keyAdapter);

                setDefaultState();

                Timer timer = new Timer(TIMER_DELAY, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            detectedFigures.add(processor.processCam(mainPanel));
                            if (detectedFigures.size() == AMOUNT_OF_SAVED_FIGURES + 1) {
                                detectedFigures.remove();
                            }
                            if (checkSavedFigures()) {
                                processGuessedFigure();
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

    /**
     * checks if figure is guessed
     * @return is guessed
     */
    private boolean checkSavedFigures() {
        if (detectedFigures.size() != AMOUNT_OF_SAVED_FIGURES) {
//            System.out.println("detectedFigures.size =  " + detectedFigures.size());
            return false;
        }
        List<DetectedFigure> tempDetectedFigures = new ArrayList<>(detectedFigures);
        for (int i = 0; i < tempDetectedFigures.size(); i++) {
            for (int j = i; j < tempDetectedFigures.size(); j++) {
                if (tempDetectedFigures.get(i) == null || tempDetectedFigures.get(j) == null ||
                        tempDetectedFigures.get(i).getDistance(tempDetectedFigures.get(j)) > MAX_DISTANCE_BETWEEN_SAME_FIGURES ||
                        tempDetectedFigures.get(i).getSizeDifference(tempDetectedFigures.get(j)) > MAX_DISTANCE_BETWEEN_SIZES ||
                        !state.equals(tempDetectedFigures.get(i).getFigureType()) ||
                        !tempDetectedFigures.get(j).getFigureType().equals(tempDetectedFigures.get(i).getFigureType()) ) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * work with key typed:
     * space -> initial default state, key -> figure according to name (key is the first letter of figure name)
     * @param e key event
     */
    private void processKeyEvent(KeyEvent e) {
        if (e.getKeyCode() == 32) {
            setDefaultState();
        }
        switch (KeyEvent.getKeyText(e.getKeyCode())) {
            case FIST_LETTER:
                setGuessingState(FIST_NAME);
                break;
            case PALM_LETTER:
                setGuessingState(PALM_NAME);
                break;

        }
//        System.out.println(e.getKeyCode());
//        System.out.println(KeyEvent.getKeyText(e.getKeyCode()));
    }

    /**
     * In case of guessed figure
     */
    private void processGuessedFigure() {
        recordData.recordDetected(detectedFigures.element());
        detectedFigures.clear();
        setOKStatus();
    }

    private void setOKStatus() {
        pictureFrame.setOK();
        state = OK_STATE_NAME;
    }

    private void setDefaultState() {
        pictureFrame.setDefault();
        state = DEFAULT_STATE_NAME;
    }

    private void setGuessingState(String name) {
        pictureFrame.setFigure(name);
        state = name;
    }
}