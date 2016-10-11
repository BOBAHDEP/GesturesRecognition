package logic;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.ml.SVM;
import org.opencv.objdetect.HOGDescriptor;

/**
 * Created by BOBAHDEP on 22.09.16.
 */
public class HOG {

    public static void main(String[] args) {
        HOGDescriptor hogDescriptor = new HOGDescriptor(new Size(93, 201), new Size(1,1), new Size(1,1), new Size(8, 16), 8);

        int pos = 5;
        int neg = 11;

        Mat labels = new Mat(pos + neg,1, CvType.CV_32FC1, new Scalar(-1.0));
//        SVM cvSVM = new SVM();
//        cvSVM.setType();
//
//        CvSVM svm;
    }
}
