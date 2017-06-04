package ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 27.05.2017.
 */
public class PlayGroundGUIForm {


    private JPanel mainPanel;
    private JButton startGameButton;
    private JButton button2;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JPanel backGroundImage;

    public PlayGroundGUIForm() {
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayStartGameMenu();
            }
        });
    }

    public static void main(String[] args) {
        PlayGroundGUIForm form = new PlayGroundGUIForm();
        JFrame mainFrame = new JFrame("mainFrame");
        mainFrame.setSize(new Dimension(1100, 600));
        mainFrame.setContentPane(form.mainPanel);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                form.exitGameSecurely(mainFrame);
                //super.windowClosing(e);
            }
        });
        mainFrame.setVisible(true);
        //form.setBackgroundImage(form.centerPanel);


    }


    //doesnt work...
    public void setBackgroundImage(JPanel panel) {
        ImageIcon back = createImageIcon("./Planet_02.png","./Planet_02.png");

        if (back != null) {
            JLabel picture = new JLabel("HERE IS ICON", back, JLabel.CENTER);
            //picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
            //picture.setHorizontalAlignment(JLabel.CENTER);
            picture.setIcon(back);
            panel.add(picture, BorderLayout.CENTER);
        } else {

        }
    }


    public void exitGameSecurely(JFrame frame) {
        frame.setVisible(false);
        //execude Code to Securely exit the frame!

        frame.dispose();
    }

    /** Returns an ImageIcon, or null if the path was invalid.
     * copyright: matheuslf*/
    protected ImageIcon createImageIcon(String path,
                                        String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public void displayStartGameMenu() {
        //StartGameMenu dialog = new StartGameMenu("das ist ein test");


    }

}
