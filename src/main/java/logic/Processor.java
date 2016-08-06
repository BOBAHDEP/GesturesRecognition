package logic;

import gui.MainPanel;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Class for image recognition
 */
public class Processor {
    public static final int CAM_NUMBER = 0;

    //cascade contains info about object to be recognized
    public static final String CASCADE_FIST_FILENAME = "classifiers/palm.xml";              //need to copy to add cascade

    public static final String CASCADE_PALM_FILENAME = "classifiers/fist.xml";

    private List<CascadeContainer> cascadeContainers = new ArrayList<>();

    private Mat webCamImage = new Mat();
    private VideoCapture capture = new VideoCapture(CAM_NUMBER);

    public Processor(){
        CascadeClassifier fistCascade = new CascadeClassifier(CASCADE_FIST_FILENAME);       //need to copy to add cascade
        if(fistCascade.empty()) {                                                           //need to copy to add cascade
            throw new IllegalArgumentException("cascade is empty!!");                       //need to copy to add cascade
        }                                                                                   //need to copy to add cascade
        CascadeClassifier palmCascade = new CascadeClassifier(CASCADE_PALM_FILENAME);
        if(palmCascade.empty()) {
            throw new IllegalArgumentException("cascade is empty!!");
        }
        cascadeContainers.add(new CascadeContainer(fistCascade, CASCADE_FIST_FILENAME));    //need to copy to add cascade
        cascadeContainers.add(new CascadeContainer(palmCascade, CASCADE_PALM_FILENAME));
    }

    /**
     * Detects object
     * @param inputFrame frame from video
     * @return ?
     */
    private DetectedFigure detect(Mat inputFrame){
        DetectedFigure res = new DetectedFigure();
        long startTime = System.nanoTime();

        //for picture quality improvement
        Mat mRgba = new Mat();
        Mat mGrey = new Mat();
        MatOfRect faces = new MatOfRect();
        inputFrame.copyTo(mRgba);
        inputFrame.copyTo(mGrey);
        Imgproc.cvtColor(mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist( mGrey, mGrey );

        // detect, save array of objects
        List<Rect> facesRect = new ArrayList<>();   //to save faces.toArray();
        List<String> typesOfFiguresDetected = new ArrayList<>();
        for (CascadeContainer cascadeContainer: cascadeContainers) {
            cascadeContainer.getCascadeClassifier().detectMultiScale(mGrey, faces);
            for (Rect rect: faces.toArray()) {
                facesRect.add(rect);
                typesOfFiguresDetected.add(convertFileXMLNameToClassifierName(cascadeContainer.getXmlFileName()));
            }
        }

        if (facesRect.size() == 0) {
            return null;
        }

        int size = -1, j = 0;
        for(int i = 0; i < facesRect.size(); i++) {
            if (size < facesRect.get(i).height) {
                j = i;
                size = facesRect.get(i).height;
            }
            Imgproc.rectangle(inputFrame, facesRect.get(i).br(), facesRect.get(i).tl(), new Scalar(255, 250 * 1, 255), 4, 8, 0);

        }
        Imgproc.rectangle(inputFrame, facesRect.get(j).br(), facesRect.get(j).tl(), new Scalar(255, 250 * 0, 255), 4, 8, 0);
        res.setHeight(facesRect.get(j).height);
        res.setWidth(facesRect.get(j).width);
        res.setX(facesRect.get(j).x);
        res.setY(facesRect.get(j).y);
        res.setFigureType(typesOfFiguresDetected.get(j));
        System.out.println(res);
        return res;
    }

    /**
     * main process of interaction with camera
     *
     * @param mainPanel panel to display found figures
     * @return figure
     * @throws IllegalAccessException in case of problem with cameras
     */

    public DetectedFigure processCam(MainPanel mainPanel) throws IllegalAccessException {
        DetectedFigure res;

        //Read the video stream
        if (capture.isOpened()) {
                capture.read(webCamImage);
                if (!webCamImage.empty()) {
                    mainPanel.getFrame().setSize(webCamImage.width(), webCamImage.height());
                    //-- 3. Apply the classifier to the captured image
                    res = detect(webCamImage);

                    //-- 4. Display the image
                    mainPanel.MatToBufferedImage(webCamImage);
                    mainPanel.repaint();
                } else {
                    throw new IllegalAccessException("No captured frame");
                }

        } else {
            throw new IllegalAccessException("Capture is not Loaded!");
        }
        return res;
    }

    public String convertFileXMLNameToClassifierName(String fileName) {


        Pattern pattern = Pattern.compile(".*?\\/(.*?)\\.xml");
        Matcher matcher = pattern.matcher(fileName);

        if(matcher.matches()) {
            return matcher.group(1);
        }
        return matcher.group(1);
    }

}
