package logic;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 *  Class for image recognition
 */
public class Processor {
    public static final String CASCADE_FILENAME = "C:\\work\\1.xml";
    //cascade contains info about object to be recognized
    private CascadeClassifier faceCascade;

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
    public Mat detect(Mat inputFrame){
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

        int i = 0; //todo del
        for(Rect rect: faces.toArray()) {
            System.out.println(rect.toString());
            Imgproc.rectangle(mRgba, rect.br(), rect.tl(), new Scalar(255, 250 * i, 255), 4, 8, 0);
            i++;
            //TODO more logic
        }
        return mRgba;
    }
}
