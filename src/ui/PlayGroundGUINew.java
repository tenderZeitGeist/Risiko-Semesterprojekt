package ui;

import domain.Risiko;
import net.miginfocom.swing.MigLayout;
import valueobjects.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 17.07.2017.
 */
public class PlayGroundGUINew {

    //Value Objects
    private Player player;
    //this should be replaced with server later
    private Risiko risiko;


    //UI
    private JFrame windowJFrame;
    private JButton nextTurnButton;
    private JButton dummyButton;

    //Objects
    private BufferedImage bgPicture;
    private BufferedImage bgPicturefix;
    private BufferedImage fgPicture;
    private BufferedImage fgPictureFix;
    private Image redFlag, greenFlag;

    //vars
    private double scalingFactor = 0.5;


    public static void main(String[] args) {
        //catch exceptions maybe?!
        try {
            new PlayGroundGUINew();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayGroundGUINew() throws IOException {
        //create server connection in here

        //this should be replaced with server later
        risiko = new Risiko();

        //init Pictures
        initPictureFiles();
        initMainWindow();
        windowJFrame.setVisible(true);


    }

    public void initMainWindow() {
        windowJFrame = new JFrame();
        windowJFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //exit game securely through here
                System.exit(0);
            }
        });
        //windowJFrame.setBounds(1000, 1000, 1000, 1000);
        windowJFrame.setLayout(new MigLayout(
                "debug",
                "[][]",
                "[][][]"));

    }

    public void initPictureFiles() {
        try {
            //extremely redundant scaling...
            bgPicture = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/starRiskColorCoded.png"));
            bgPicture = resizeBuffImg(bgPicture, (int) (bgPicture.getWidth() * scalingFactor), (int) (bgPicture.getHeight() * scalingFactor));
            redFlag = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/flag_icons/flag_red.png"));
            redFlag = redFlag.getScaledInstance(60, 60, 10);
            greenFlag = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/flag_icons/flag_green.png"));
            greenFlag = greenFlag.getScaledInstance(60, 60, 10);

            fgPictureFix = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/StarRiskBg.png"));
            fgPictureFix = resizeBuffImg(fgPictureFix, (int) (fgPictureFix.getWidth() * scalingFactor), (int) (fgPictureFix.getHeight() * scalingFactor));
            fgPicture = fgPictureFix;

            //fgPictureLabel = new JLabel(new ImageIcon(fgPicture));
            //fgPictureLabel.paint();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }



    public static BufferedImage resizeBuffImg(BufferedImage img, int newW, int newH) {
        //Thanks Stackoverflow...
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_REPLICATE);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }
}
