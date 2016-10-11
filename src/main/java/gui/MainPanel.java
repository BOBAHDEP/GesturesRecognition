package gui;

/*
 * Panel window displaying video from camera & resolved figures
 */

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.*;

import org.opencv.core.Mat;

public class MainPanel extends JPanel {
    public static final String WINDOW_NAME = "Capture - Gesture detection";

    private BufferedImage image;

    private JFrame frame;

    private JLabel pictureDisplayed = null;
    public static final String TEMPLATE_PATH = "img/*.png";
    public static final String IMG_OK_PATH = "img/ok.png";

    private JLabel timerNameLabel = new JLabel("Timer");
    private JLabel timerLabel = new JLabel("");

    private JLabel period = new JLabel("Period");
    private JLabel periodRes = new JLabel("");
    private JCheckBox isCycle;

    public JFrame getFrame() {
        return frame;
    }

    public MainPanel(KeyAdapter keyAdapter, ActionListener start, ActionListener stop, JCheckBox isCycle) {
        super();
        this.isCycle = isCycle;
        frame = new JFrame(WINDOW_NAME);
        frame.setLayout(new GridLayout(4, 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);

        timerNameLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        timerLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        period.setFont(new Font("Serif", Font.PLAIN, 20));

        frame.setContentPane(this);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.addKeyListener(keyAdapter);

        pictureDisplayed = new JLabel("");

        JPanel p = new JPanel(new GridLayout(0, 1));
        p.addKeyListener(keyAdapter);

        frame.add(Box.createHorizontalStrut(630));
        p.add(timerNameLabel);
        p.add(Box.createVerticalStrut(10));
        JButton startB = new JButton("start");
        startB.addActionListener(start);
        p.add(startB);
        p.add(Box.createVerticalStrut(10));
        JButton stopB = new JButton("stop");
        stopB.addActionListener(stop);
        p.add(stopB);

        p.add(Box.createVerticalStrut(10));
        JButton submitB = new JButton("submit");
        stopB.addActionListener(stop);
        p.add(submitB);

        p.add(Box.createVerticalStrut(10));
        p.add(isCycle);
        p.add(Box.createVerticalStrut(10));
        p.add(Box.createHorizontalStrut(120));
        p.add(period);

        frame.add(p);
        frame.add(Box.createHorizontalStrut(630));
        frame.add(pictureDisplayed, BorderLayout.SOUTH);
        frame.add(Box.createVerticalStrut(120));

        setPeriodEnabled();
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
        g.drawImage(this.image, 10, 10, this.image.getWidth(), this.image.getHeight(), null);
    }

    public void setTime(Long timeInMSec) {
        if (timeInMSec >= 1000) {
            timerNameLabel.setText("Timer " + timeInMSec.toString().substring(0, timeInMSec.toString().length() - 3) + "s");
        }
    }

    public void setPeriod(long period) {
//        periodRes.setText(String.valueOf(period));
        this.period.setText("Period " + String.valueOf(period) + "ms");
    }

    public void setPeriodEnabled() {
        if (isCycle.isSelected()) {
            period.setText("Period");
        } else {
            period.setText("");
            periodRes.setText("");
        }
    }

    /**
     * Set picture name
     * @param name must equal to name of figure in cascade!
     */
    public void setFigure(String name) {
        if (name == null) {
            pictureDisplayed.setIcon(null);
            return;
        }
        ImageIcon imageIconOK = new ImageIcon(TEMPLATE_PATH.replace("*", name));
        pictureDisplayed.setText("");
        pictureDisplayed.setIcon(imageIconOK);
    }

    public void setOK() {
        pictureDisplayed.setText("");
        ImageIcon imageIconOK = new ImageIcon(IMG_OK_PATH);
        pictureDisplayed.setIcon(imageIconOK);
    }
}