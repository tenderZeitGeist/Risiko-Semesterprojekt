package ui;
//package customUiElements;

import domain.Risiko;
import valueobjects.Player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ui.customUiElements.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 19.05.2017.
 */

public class PlayGroundGUI {


    private JFrame meinFrame;
    private JPanel leftTopPanel;

    public static void main(String[] args) {
        PlayGroundGUI GUI = new PlayGroundGUI();

        GUI.createWindow();

    }


    public void createWindow() {
        JFrame meinFrame = new JFrame("shizzle");

        /* Wir setzen die Breite und die Höhe
           unseres Fensters auf 200 Pixel */
        meinFrame.setSize(500, 350);
        meinFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitGameSecurely(meinFrame);
                //super.windowClosing(e);
            }
        });
        meinFrame.setVisible(true);
        JPanel mainPanel = new JPanel(new BorderLayout());
        meinFrame.setContentPane(mainPanel);

        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

        leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));


        mainPanel.add(leftPanel, BorderLayout.LINE_START);


        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitGameSecurely(meinFrame);
            }
        });

        JButton newGameButton = new JButton("load game");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //start a new game
                System.out.println("yo");
            }
        });

        leftPanel.add(quitButton);
        quitButton.setPreferredSize(new Dimension(leftPanel.getWidth(), 10));
        quitButton.setMinimumSize(new Dimension(leftPanel.getWidth(), 10));


        quitButton.setPreferredSize(new Dimension(leftPanel.getWidth(), 10));
        leftPanel.add(newGameButton);


    }


    public void exitGameSecurely(JFrame frame) {
        frame.setVisible(false);
        //execude Code to Securely exit the frame!

        frame.dispose();
    }


}
