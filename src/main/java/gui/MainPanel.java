package gui;

/*
 * Captures the camera stream with OpenCV
 * Search for the faces
 * Display a circle around the faces using Java
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.*;
import org.opencv.core.Mat;

class MainPanel extends JPanel{
    private BufferedImage image;

    public MainPanel(){
        super();
    }

    /**
     * Shows image ???
     * @param matBGR in=mg
     * @return
     */
    public boolean MatToBufferedImage(Mat matBGR){
        long startTime = System.nanoTime();
        int width = matBGR.width(), height = matBGR.height(), channels = matBGR.channels() ;
        byte[] sourcePixels = new byte[width * height * channels];
        matBGR.get(0, 0, sourcePixels);
        // create new image and get reference to backing data
        image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
        long endTime = System.nanoTime();
        System.out.println(String.format("Elapsed time: %.2f ms", (float)(endTime - startTime)/1000000));
        return true;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (this.image==null) return;
        g.drawImage(this.image, 0, 0, this.image.getWidth(), this.image.getHeight(), null);
    }
}