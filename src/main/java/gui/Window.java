package gui;

import dataProcess.RecordData;
import logic.DetectedFigure;
import logic.Processor;
import properties.Property;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class Window {

    public static final int AMOUNT_OF_SAVED_FIGURES = Property.getAmountOfSavedFigures();
    public static final int MAX_DISTANCE_BETWEEN_SAME_FIGURES = Property.getMaxDistanceBetweenSameFigures();
    public static final int MAX_DISTANCE_BETWEEN_SIZES = Property.getMaxDistanceBetweenSizes();
    public static final String OK_STATE_NAME = "OK";
    public static final String DEFAULT_STATE_NAME = "Default";
    public static final String FIST_LETTER = "F";
    public static final String FIST_NAME = "fist";      //need to copy to add
    public static final String PALM_NAME = "palm";
    public static final String HAND_OK_NAME = "ok_";
    public static final String PALM_LETTER = "P";
    public static final String HAND_OK_LETTER = "O";
    public static final String EDGE_NAME = "edge";
    public static final int SPACE_SYMBOL_CODE = 32;

    private long time = -1;
    private long timeForTimer = System.currentTimeMillis();
    private boolean timerRun = true;

    //keep last figures for detection
    private Queue<DetectedFigure> detectedFigures = new LinkedList<>();
    private Processor processor = new Processor();

    //windows
    private MainPanel mainPanel = null;
    private PictureFrame pictureFrame = null;

    //to record data about events
    private RecordData recordData = new RecordData();

    //state: "default" in default mode, name of figure in guessing mode, "OK" in OK mode
    private String state = null;

    JCheckBox isCycle = new JCheckBox("repeat");

    static {
        System.load(Property.getLibPath());
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

                isCycle.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainPanel.setPeriodEnabled();
                    }
                });
                ActionListener startA = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        startTimer();
                        setGuessingState(FIST_NAME);
                    }
                };
                ActionListener stopA = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        stopTimer();
                        setDefaultState();
                    }
                };
                mainPanel = new MainPanel(keyAdapter, startA, stopA, isCycle);

                setDefaultState();
                stopTimer();
                try{
                    detectedFigures.add(processor.processCam(mainPanel));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Timer timer = new Timer(Property.getFrameRate(), new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {

                            if (timerRun) {
                                mainPanel.setTime(getTime());
                                mainPanel.repaint();
                            }

                            if (state.equals(FIST_NAME) || state.equals(PALM_NAME) || state.equals(HAND_OK_NAME)) {
                                detectedFigures.add(processor.processCam(mainPanel));
                            }
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

    private long getTime() {
        return System.currentTimeMillis() - timeForTimer;
    }

    private void startTimer() {
        timerRun = true;
        timeForTimer = System.currentTimeMillis() - 1000;
    }

    private void stopTimer() {
        timerRun = false;
        timeForTimer = System.currentTimeMillis();
    }

    /**
     * checks if figure is guessed
     *
     * @return is guessed
     */
    private boolean checkSavedFigures() {

        if (detectedFigures.size() != AMOUNT_OF_SAVED_FIGURES) {
            System.out.println("detectedFigures.size =  " + detectedFigures.size());
            return false;
        }
        List<DetectedFigure> tempDetectedFigures = new ArrayList<>(detectedFigures);
        int numberOfFiguresToGuess = isCyclic()? tempDetectedFigures.size() -2 : 0;
        for (int i = tempDetectedFigures.size()-1; i >= numberOfFiguresToGuess; i--) {
            for (int j = tempDetectedFigures.size()-1; j >= numberOfFiguresToGuess; j--) {
                if (!isCyclic()) {
                    if (tempDetectedFigures.get(i) == null || tempDetectedFigures.get(j) == null ||
                            tempDetectedFigures.get(i).getDistance(tempDetectedFigures.get(j)) > MAX_DISTANCE_BETWEEN_SAME_FIGURES ||
                            tempDetectedFigures.get(i).getSizeDifference(tempDetectedFigures.get(j)) > MAX_DISTANCE_BETWEEN_SIZES ||
                            !state.equals(tempDetectedFigures.get(i).getFigureType()) ||
                            !tempDetectedFigures.get(j).getFigureType().equals(tempDetectedFigures.get(i).getFigureType())) {
                        return false;
                    }
                } else {
                    if (tempDetectedFigures.get(i) == null || tempDetectedFigures.get(j) == null ||
                            tempDetectedFigures.get(i).getDistance(tempDetectedFigures.get(j)) > MAX_DISTANCE_BETWEEN_SAME_FIGURES ||
                            tempDetectedFigures.get(i).getSizeDifference(tempDetectedFigures.get(j)) > MAX_DISTANCE_BETWEEN_SIZES ||
                            !tempDetectedFigures.get(j).getFigureType().equals(tempDetectedFigures.get(i).getFigureType())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * work with key typed:
     * space -> initial default state, key -> figure according to name (key is the first letter of figure name)
     *
     * @param e key event
     */
    private void processKeyEvent(KeyEvent e) {
        if (e.getKeyCode() == SPACE_SYMBOL_CODE) {
            setDefaultState();
            processor.cascadeCheat = null;
        }
        switch (KeyEvent.getKeyText(e.getKeyCode())) {
            case FIST_LETTER:
                setGuessingState(FIST_NAME);
                processor.cascadeCheat = FIST_NAME;
                startTimer();
                break;
            case PALM_LETTER:                       //need to copy to add
                setGuessingState(PALM_NAME);        //need to copy to add
                startTimer();
                processor.cascadeCheat = PALM_NAME;
                break;
            case HAND_OK_LETTER:
                setGuessingState(HAND_OK_NAME);
                startTimer();
                processor.cascadeCheat = HAND_OK_NAME;
                break;

        }
    }

    /**
     * In case of guessed figure
     */
    private void processGuessedFigure() {
        if (!isCyclic()) {
            recordData.recordDetected(detectedFigures.element());
            detectedFigures.clear();
            setOKStatus();
        } else {
            DetectedFigure detectedFigure = detectedFigures.element();
            recordData.recordDetected(detectedFigure);
            detectedFigures.clear();
            if (detectedFigure != null) {
                setGuessingState(detectedFigure.getFigureType());
                if (detectedFigure.getFigureType().equals("palm")) {
                    processor.cascadeCheat = "fist";
                } else {
                    processor.cascadeCheat = "palm";
                }

            }
        }
    }

    private void setOKStatus() {
        if (!isCyclic()) {
            mainPanel.setOK();
        }
        state = OK_STATE_NAME;
    }

    private void setDefaultState() {
        stopTimer();
        mainPanel.setTime(0L);
        mainPanel.repaint();
        if (!isCyclic()) {
            mainPanel.setFigure(null);
        }
        state = DEFAULT_STATE_NAME;
    }

    private boolean isCyclic() {
        return isCycle.isSelected();
    }

    private void setGuessingState(String name) {
        if (isCyclic()) {
            if (time != -1){
                if (!getGuessingState().equals(name)) {
                    mainPanel.setPeriod(System.currentTimeMillis() - time);
                    time = System.currentTimeMillis();
                    mainPanel.repaint();
                }
            } else {
                time = System.currentTimeMillis();
            }

        } else {
            mainPanel.setFigure(name);
        }
        state = name;
    }

    private String getGuessingState() {
        return state;
    }
}