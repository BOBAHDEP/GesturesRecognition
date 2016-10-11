package logic;

import gui.MainPanel;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import properties.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Class for image recognition
 */
public class Processor {
    public static final int CAM_NUMBER = 1;

    //cascade contains info about object to be recognized
    public static final String CASCADE_FIST_FILENAME = "classifiers/fist.xml";                  //need to copy to add cascade

    public static final String CASCADE_PALM_FILENAME = "classifiers/palm.xml";

    public String cascadeCheat = null;

    public static final String CASCADE_OK_FILENAME = "classifiers/ok_.xml";

    public static final List<String> CASCADE_FILENAMES = new ArrayList<>();

    private List<CascadeContainer> cascadeContainers = new ArrayList<>();

    private Mat webCamImage = new Mat();
    private VideoCapture capture = new VideoCapture(CAM_NUMBER);

    public Processor(){

        CASCADE_FILENAMES.add(CASCADE_FIST_FILENAME);
        CASCADE_FILENAMES.add(CASCADE_PALM_FILENAME);                                           //need to copy to add cascade
        CASCADE_FILENAMES.add(CASCADE_OK_FILENAME);

        for (String cascadeName: CASCADE_FILENAMES) {
            CascadeClassifier cascadeClassifier = new CascadeClassifier(cascadeName);
            if(cascadeClassifier.empty()) {
                throw new IllegalArgumentException("cascade is empty!!");
            }
            cascadeContainers.add(new CascadeContainer(cascadeClassifier, cascadeName));
        }
    }

    /**
     * Detects object
     *
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
//        Imgproc.cvtColor(mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);
//        Imgproc.equalizeHist(mGrey, mGrey);
//        Imgproc.cvtColor(mGrey, mGrey, Imgproc.COLOR_GRAY2BGR);

        Imgproc.cvtColor(mGrey, mGrey, Imgproc.COLOR_BGR2HSV);

        //filter skin
        byte[] zeros = {1,1,1};
        for(int r=0; r<mGrey.rows(); ++r) {
            for (int c = 0; c < mGrey.cols(); ++c) {
                // 0<H<0.25  -   0.15<S<0.9    -    0.2<V<0.95
//                if( (mGrey.get(r,c)[0]>0) && (mGrey.get(r,c)[0] < 255) && (mGrey.get(r,c)[1]>38) && (mGrey.get(r,c)[1]<255) /*&& (mGrey.get(r,c)[2]>51) && (mGrey.get(r,c)[2]<242) */); // do nothing
//                else for(int i=0; i<3; ++i)	mGrey.put(r,c, zeros);
            }
        }
        Imgproc.cvtColor(mGrey, mGrey, Imgproc.COLOR_HSV2BGR);
//        Imgproc.cvtColor(mGrey, mGrey, Imgproc.COLOR_BGR2GRAY);
//        Imgproc.threshold(mGrey, mGrey, 60, 255, Imgproc.THRESH_BINARY);
//        Imgproc.morphologyEx(mGrey, mGrey, Imgproc.MORPH_ERODE, new Mat(3,3,1), new Point(-1, -1), 3);
//        Imgproc.morphologyEx(mGrey, mGrey, Imgproc.MORPH_OPEN, new Mat(7,7,1), new Point(-1, -1), 1);
//        Imgproc.morphologyEx(mGrey, mGrey, Imgproc.MORPH_CLOSE, new Mat(9,9,1), new Point(-1, -1), 1);

        mGrey.copyTo(inputFrame);



        // detect, save array of objects
        List<Rect> facesRect = new ArrayList<>();   //to save faces.toArray();
        List<String> typesOfFiguresDetected = new ArrayList<>();
        for (CascadeContainer cascadeContainer: cascadeContainers) {
            if (cascadeCheat == null /*&& (convertFileXMLNameToClassifierName(cascadeContainer.getXmlFileName()).equals("fist")
            || convertFileXMLNameToClassifierName(cascadeContainer.getXmlFileName()).equals("palm"))*/
                || convertFileXMLNameToClassifierName(cascadeContainer.getXmlFileName()).equals(cascadeCheat)) {
                cascadeContainer.getCascadeClassifier().detectMultiScale(mGrey, faces);
                for (Rect rect : faces.toArray()) {
                    if (rect.height > Property.getHeight())
                        facesRect.add(rect);
                    typesOfFiguresDetected.add(convertFileXMLNameToClassifierName(cascadeContainer.getXmlFileName()));
                }
            }
//            if (cascadeCheat != null && (convertFileXMLNameToClassifierName(cascadeContainer.getXmlFileName()).equals("fist") ||
//                    convertFileXMLNameToClassifierName(cascadeContainer.getXmlFileName()).equals("palm")) &&
//                    cascadeCheat.equals("1")) {
//                cascadeContainer.getCascadeClassifier().detectMultiScale(mGrey, faces);
//                for (Rect rect : faces.toArray()) {
//                    if (rect.height > Property.getHeight())
//                        facesRect.add(rect);
//                    typesOfFiguresDetected.add(convertFileXMLNameToClassifierName(cascadeContainer.getXmlFileName()));
//                }
//            }
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
            //draw rectangle around figure
//            Imgproc.rectangle(inputFrame, facesRect.get(i).br(), facesRect.get(i).tl(), new Scalar(255, 250, 255), 4, 8, 0);
            //display type
//            Imgproc.putText(inputFrame, typesOfFiguresDetected.get(i), facesRect.get(i).br(), Core.FONT_HERSHEY_COMPLEX, 1.0 , new  Scalar(255, 255, 255));
        }
        //to draw rectangle of another color around chosen figure
        Imgproc.rectangle(inputFrame, facesRect.get(j).br(), facesRect.get(j).tl(), new Scalar(255, 0, 255), 4, 8, 0);
        Imgproc.putText(inputFrame, typesOfFiguresDetected.get(j), facesRect.get(j).br(), Core.FONT_HERSHEY_COMPLEX, 1.0 , new  Scalar(255, 255, 255));

        res.setHeight(facesRect.get(j).height);
        res.setWidth(facesRect.get(j).width);
        res.setX(facesRect.get(j).x);
        res.setY(facesRect.get(j).y);
        res.setFigureType(typesOfFiguresDetected.get(j));
        System.out.println(res);
        return res;
    }

//    private Mat drawVideoInStyle(Mat inputFrame) {
//
//    }

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
        try {
            if (capture.isOpened()) {
                capture.read(webCamImage);
                if (!webCamImage.empty()) {
                    if (mainPanel != null) {
                        mainPanel.getFrame().setSize(mainPanel.getWidth(), webCamImage.height() + 50);
                    }
                    //-- 3. Apply the classifier to the captured image
                    res = detect(webCamImage);

                    //-- 4. Display the image
                    if (mainPanel != null) {
                        mainPanel.MatToBufferedImage(webCamImage);
                        mainPanel.repaint();
                    }
                } else {
                    throw new IllegalAccessException("No captured frame");
                }

            } else {
                throw new IllegalAccessException("Capture is not Loaded!");
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert file path to file name
     * @param fileName file path
     * @return file name
     */
    public String convertFileXMLNameToClassifierName(String fileName) {


        Pattern pattern = Pattern.compile(".*?\\/(.*?)\\.xml");
        Matcher matcher = pattern.matcher(fileName);

        if(matcher.matches()) {
            return matcher.group(1);
        }
        return matcher.group(1);
    }

}
