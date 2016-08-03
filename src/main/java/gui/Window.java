package gui;

import logic.Processor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.JFrame;

public class Window {

    public static final String WINDOW_NAME = "Capture - Face detection";

    public static void main(String[] args) throws IllegalAccessException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        JFrame frame = new JFrame(WINDOW_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        Processor processor = new Processor();
        MainPanel mainPanel = new MainPanel();
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
        //Read the video stream
        Mat webCamImage = new Mat();
        VideoCapture capture = new VideoCapture(0);
        if( capture.isOpened()) {
            while(true) {   //TODO add timer?
                capture.read(webCamImage);
                if(!webCamImage.empty()) {
                    frame.setSize(webCamImage.width(),webCamImage.height());
                    //-- 3. Apply the classifier to the captured image
                    webCamImage=processor.detect(webCamImage);

                    //-- 4. Display the image
                    mainPanel.MatToBufferedImage(webCamImage);
                    mainPanel.repaint();
                }
                else {
                    throw new IllegalAccessException("No captured frame");
                }
            }
        } else {
            throw new IllegalAccessException("Capture is not Loaded!");
        }
    }
}
