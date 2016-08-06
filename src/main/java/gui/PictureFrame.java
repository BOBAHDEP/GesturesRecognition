package gui;

import javax.swing.*;

/**
 * Created by BOBAHDEP on 06.08.16.
 */
public class PictureFrame extends JFrame {

    public static final String IMG_FIST_PATH = "img/fist.jpg";
    public static final String IMG_OK_PATH = "img/ok1.png";

    private JLabel pictureDisplayed = null;

    PictureFrame() {
        ImageIcon imageIconFist = new ImageIcon(IMG_FIST_PATH);
        this.setSize(imageIconFist.getIconWidth(), imageIconFist.getIconHeight() + 25);
        pictureDisplayed = new JLabel(imageIconFist);
        this.add(pictureDisplayed);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void setOK() {
        ImageIcon imageIconOK = new ImageIcon(IMG_OK_PATH);
        pictureDisplayed.setIcon(imageIconOK);
    }
}
