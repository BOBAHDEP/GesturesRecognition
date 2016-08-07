package gui;

/*
 * Panel window displaying video from camera & resolved figures
 */

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.*;

import org.opencv.core.Mat;

public class MainPanel extends JPanel {
    public static final String WINDOW_NAME = "Capture - Face detection";

    private BufferedImage image;

    private JFrame frame;

    public JFrame getFrame() {
        return frame;
    }

    public MainPanel(KeyAdapter keyAdapter) {
        super();
        frame = new JFrame(WINDOW_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        frame.setContentPane(this);
        frame.setVisible(true);
        frame.addKeyListener(keyAdapter);
    }

    /**
     * Shows image ???
     *
     * @param matBGR img
     * @return
     */
    public boolean MatToBufferedImage(Mat matBGR) {
        long startTime = System.nanoTime();
        int width = matBGR.width(), height = matBGR.height(), channels = matBGR.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        matBGR.get(0, 0, sourcePixels);
        // create new image and get reference to backing data
        image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
        long endTime = System.nanoTime();
//        System.out.println(String.format("Elapsed time: %.2f ms", (float) (endTime - startTime) / 1000000));
        return true;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.image == null) return;
        g.drawImage(this.image, 0, 0, this.image.getWidth(), this.image.getHeight(), null);
    }
}