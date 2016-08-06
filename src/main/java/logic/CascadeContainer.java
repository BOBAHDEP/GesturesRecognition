package logic;

import org.opencv.objdetect.CascadeClassifier;

/**
 * Keeps cascade and name of its .xml file
 */
public class CascadeContainer {
    private CascadeClassifier cascadeClassifier;
    private String xmlFileName;

    public CascadeContainer(CascadeClassifier cascadeClassifier, String xmlFileName) {
        this.cascadeClassifier = cascadeClassifier;
        this.xmlFileName = xmlFileName;
    }

    public CascadeClassifier getCascadeClassifier() {
        return cascadeClassifier;
    }

    public void setCascadeClassifier(CascadeClassifier cascadeClassifier) {
        this.cascadeClassifier = cascadeClassifier;
    }

    public String getXmlFileName() {
        return xmlFileName;
    }

    public void setXmlFileName(String xmlFileName) {
        this.xmlFileName = xmlFileName;
    }

}
