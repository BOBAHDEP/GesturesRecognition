package logic;

import gui.MainPanel;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

/**
 *  Class for image recognition
 */
public class Processor {
    public static final String CASCADE_FILENAME = "classifiers/fist.xml";
    public static final int CAM_NUMBER = 0;
    //cascade contains info about object to be recognized
    private CascadeClassifier faceCascade;

    private Mat webCamImage = new Mat();
    private VideoCapture capture = new VideoCapture(CAM_NUMBER);

    public Processor(){
        faceCascade = new CascadeClassifier(CASCADE_FILENAME);
        if(faceCascade.empty()) {
            throw new IllegalArgumentException("cascade is empty!!");
        }
        else {
            System.out.println("Face classifier loaded up");
        }
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
        faceCascade.detectMultiScale(mGrey, faces);
        long endTime = System.nanoTime();
        System.out.println(String.format("Detect time: %.2f ms", (float)(endTime - startTime)/1000000));
        System.out.println(String.format("Detected %s faces", faces.toArray().length));

        int size = -1, j = 0;
        Rect[] facesRect = faces.toArray();
        if (facesRect.length == 0) {
            return null;
        }
        for(int i = 0; i < facesRect.length; i++) {
            if (size < facesRect[i].height) {
                j = i;
                size = facesRect[i].height;
            }
            Imgproc.rectangle(inputFrame, facesRect[i].br(), facesRect[i].tl(), new Scalar(255, 250 * 1, 255), 4, 8, 0);

        }
        Imgproc.rectangle(inputFrame, facesRect[j].br(), facesRect[j].tl(), new Scalar(255, 250 * 0, 255), 4, 8, 0);
        res.setHeight(facesRect[j].height);
        res.setWidth(facesRect[j].width);
        res.setX(facesRect[j].x);
        res.setY(facesRect[j].y);
        return res;
    }

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

}
