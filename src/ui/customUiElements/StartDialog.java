package ui.customUiElements;

import domain.Risiko;
import domain.exceptions.PlayerAlreadyExistsException;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Collections;

/**
 * Created by ZeitGeist on 07.07.2017.
 */
public class StartDialog extends JFrame{

    public Risiko startGameDialog ( Risiko risk ) {

        // Create starting GUI
        this.setLayout(new MigLayout(
                "debug",
                "[]",
                "[]"));

        JPanel startPanel = new JPanel(new MigLayout(
                "ins 24",
                "",
                "[]16[]")
        );


        this.add(startPanel, "cell 0 0");
        this.setResizable(false);
        startPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        //Insert and create buttons
        JButton startGameButton = new JButton("Start Game");
        startPanel.add(startGameButton, "cell 0 0");
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean isValid = false;
                int playerCount = 0;
                boolean playerValidation = false;

                // Query for player creation
                // TODO Change continue to proper loop solution
                while (!isValid) {
                    while (!playerValidation) {
                        try {
                            playerCount = Integer.parseInt(JOptionPane.showInputDialog("Please insert the amount of players."));
                            if (playerCount < 2 || playerCount > 6) {
                                JOptionPane.showMessageDialog(null, "The recommended player numbers of players lies between 2 and 6 people. Please enter a different value.", "Invalid value.", JOptionPane.ERROR_MESSAGE);
                            } else {
                                playerValidation = true;
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "Invalid value.", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    // Creating players
                    int playerIndex = 0;
                    while (playerIndex < playerCount) {
                        try {
                            String playerName = JOptionPane.showInputDialog("Please insert the name of Player " + (playerIndex+1));
                            risk.createPlayer(0, playerName);
                            playerIndex++;
                        } catch (PlayerAlreadyExistsException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage() + " Please use another name.", "Player already exists.", JOptionPane.ERROR_MESSAGE);
                            playerIndex--;
                        }
                    }
                    isValid = true;
                }
                //No!: risk.startTurn(risk.getCurrentPlayer());

                setVisible ( false );
                dispose ();
            }
        });

        JButton loadGameButtonInitial = new JButton("Load Game");
        startPanel.add(loadGameButtonInitial, "cell 0 1");
        loadGameButtonInitial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Function is not yet implemented.", "Unavailable", JOptionPane.ERROR_MESSAGE);
            }
        });

        // At last, creates the frame to display the GUI elements
        this.setContentPane(startPanel);
        this.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Essential methods for playing the game are being invoked here
        //dont do this here
        return risk;
    }
}
