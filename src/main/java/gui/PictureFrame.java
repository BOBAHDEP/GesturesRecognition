package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Window displaying picture to be repeated
 */
public class PictureFrame extends JFrame {

    public static final String IMG_FIST_PATH = "img/prepare.jpg";
    public static final String IMG_OK_PATH = "img/ok.png";
    public static final String TEMPLATE_PATH = "img/*.png";
    public static final int WINDOW_SIZE = 300;


    private JLabel pictureDisplayed = null;

    public PictureFrame() {
        ImageIcon imageIconFist = new ImageIcon(IMG_FIST_PATH);
        this.setSize(WINDOW_SIZE, WINDOW_SIZE);
        pictureDisplayed = new JLabel("");
        pictureDisplayed.setText("GET READY!");
        pictureDisplayed.setFont(new Font("Serif", Font.PLAIN, 50));
        this.add(pictureDisplayed);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setOK() {
        pictureDisplayed.setText("");
        ImageIcon imageIconOK = new ImageIcon(IMG_OK_PATH);
        pictureDisplayed.setIcon(imageIconOK);
    }

    public void setDefault(boolean isCycle) {
        if (!isCycle) {
            pictureDisplayed.setText("GET READY!");
            pictureDisplayed.setFont(new Font("Serif", Font.PLAIN, 50));
            pictureDisplayed.setIcon(null);
        } else {
            pictureDisplayed.setText("GET READY!");
            pictureDisplayed.setFont(new Font("Serif", Font.PLAIN, 50));
            pictureDisplayed.setIcon(null);
        }
    }

    public void setText(long time) {
        pictureDisplayed.setText(String.valueOf(time));
        pictureDisplayed.setFont(new Font("Serif", Font.PLAIN, 50));
        pictureDisplayed.setIcon(null);
    }

    /**
     * Set picture name
     * @param name must equal to name of figure in cascade!
     */
    public void setFigure(String name) {
        ImageIcon imageIconOK = new ImageIcon(TEMPLATE_PATH.replace("*", name));
        pictureDisplayed.setText("");
        pictureDisplayed.setIcon(imageIconOK);
    }
}
