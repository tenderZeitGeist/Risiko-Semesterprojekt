package ui;

import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import valueobjects.Player;
import java.util.List;
import java.util.Vector;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 31.05.2017.
 */
public class PlayGroundGUI  {
    List<Player> playerList = new Vector<Player>();

    public PlayGroundGUI() {}

    public static void main(String[] args) {
        PlayGroundGUI p = new PlayGroundGUI();

        p.createGame();
    }

    public void createGame(){
        JFrame mainWindowFrame = new JFrame("WADDUPPP??!?!?!?");
        mainWindowFrame.setSize(2000, 1300);
        mainWindowFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitGameSecurely(mainWindowFrame);
            }
        });
        //- Add stuff to mainWindowFrame
        //-- Add ContentPanel
        JPanel mainWindowPanel = new JPanel(new MigLayout("debug, wrap 2, gap rel 7","[50][][][]","[][][][][][]"));
        mainWindowPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        mainWindowFrame.add(mainWindowPanel);

        //--- Add left side Button Row
        //---- Add picture
        BufferedImage myPicture;
        JLabel playGroundLabel = new JLabel();

        try {
            myPicture = ImageIO.read(new File("./src/ui/rescourcen/StarRiskBG.jpg"));
            playGroundLabel = new JLabel(new ImageIcon(myPicture.getScaledInstance((int)(myPicture.getWidth()*1.8), (int)(myPicture.getHeight()*1.8), Image.SCALE_FAST)));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //----
        JButton button1 = new JButton("Start");
        button1.setFont(new Font("Arial", Font.PLAIN, 30));
        JButton button2 = new JButton("button2");
        button2.setFont(new Font("Arial", Font.PLAIN, 30));
        JButton button3 = new JButton("button3");
        button3.setFont(new Font("Arial", Font.PLAIN, 30));
        JButton button4 = new JButton("button4");
        button4.setFont(new Font("Arial", Font.PLAIN, 30));
        JButton button5 = new JButton("button5");
        button5.setFont(new Font("Arial", Font.PLAIN, 30));
        JTextArea actionPerformedText = new JTextArea("actionPerformedText",10,20);
        JTextArea placeholderText1 = new JTextArea("placeholderText1",10,20);
        JTextArea placeholderText2 = new JTextArea("placeholderText2",10,20);

        //---
        //--- Add content to mainWindowPanel
        mainWindowPanel.add(button1, "left, growx, growy, wmin 160, hmin 70");
        mainWindowPanel.add(playGroundLabel,"center, spanx 5, spany 6");
        mainWindowPanel.add(button2, "left, growx, growy, wmin 160, hmin 70");
        mainWindowPanel.add(button3, "left, growx, growy, wmin 160, hmin 70");
        mainWindowPanel.add(button4, "left, growx, growy, wmin 160, hmin 70");
        mainWindowPanel.add(button5, "left, growx, growy, wmin 160, hmin 70");
        mainWindowPanel.add(actionPerformedText, "growx, growy");
        mainWindowPanel.add(placeholderText1, "split2, spanx, growx, growy");
        mainWindowPanel.add(placeholderText2, "growx, growy");
        //---
        //--
        mainWindowFrame.setVisible(true);
        mainWindowFrame.setResizable(false);
    }



    public void exitGameSecurely(JFrame window) {
        //TODO add end game context
        window.dispose();
    }

}
