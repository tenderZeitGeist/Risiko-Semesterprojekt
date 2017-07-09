package ui.customUiElements;

import com.sun.org.apache.xpath.internal.operations.Mod;
import domain.PlayGround;
import domain.Risiko;
import net.miginfocom.swing.MigLayout;
import ui.customUiElements.ConnectionDataHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 01.06.2017.
 */
public class CreatePlayerDialog extends ModalDialog {

    private ConnectionDataHandler handler = null;
    private Vector<String> connectionData = new Vector<>();
    private Risiko risk;


    public CreatePlayerDialog(ConnectionDataHandler handler) throws IOException {
        this.handler = handler;
        risk = new Risiko();
    }

    public static void main(String[] args) {

    }

    @Override
    public void createDialog() {
        JDialog connectionDialog = new JDialog();
        connectionDialog.setModal(true);
        connectionDialog.setTitle("New Game");
        connectionDialog.setSize(300, 180);
        connectionDialog.setLocationRelativeTo(null);
        JPanel connectionDialogPanel = new JPanel(new MigLayout("wrap1", "[]", "[][]"));
        connectionDialog.add(connectionDialogPanel);
        JLabel inputPlayerCount = new JLabel("Enter number of Players please");
        JTextArea playerCount = new JTextArea("", 1, 1);
        connectionDialogPanel.add(inputPlayerCount);
        connectionDialogPanel.add(playerCount, "growx, growy");
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Arial", Font.PLAIN, 20));
        connectionDialogPanel.add(confirmButton, "split2, spanx, left, wmin 100, hmin 30");
        JButton confirmButton2 = new JButton("Cancel");
        confirmButton2.setFont(new Font("Arial", Font.PLAIN, 20));
        connectionDialogPanel.add(confirmButton2, "left, wmin 100, hmin 30");
        inputPlayerCount.setFont(new Font("Arial", Font.PLAIN, 17));
        playerCount.setBounds(10, 10, 10, 10);
        playerCount.setFont(new Font("Arial", Font.PLAIN, 17));
        playerCount.setOpaque(true);
        connectionDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        confirmButton.addActionListener(new ActionListener() { //confirmed
            public void actionPerformed(ActionEvent e) {
                int numberOfPlayers = Integer.parseInt(playerCount.getText());
                for (int i = 0; i < numberOfPlayers; i++) {
                    createPlayerDialog(i);
                }
                connectionDialog.dispose();
                confirm();
            }
        });
        confirmButton2.addActionListener(new ActionListener() { //cancelled
            public void actionPerformed(ActionEvent e) {
                connectionDialog.dispose();
                System.exit(0);
            }
        });
        connectionDialog.setResizable(false);
        connectionDialog.pack();
        connectionDialog.setVisible(true);
    }

    public void createPlayerDialog(int playerID) {
        JDialog connectionDialog = new JDialog();
        connectionDialog.setModal(true);
        connectionDialog.setTitle("Enter Player Name");
        connectionDialog.setSize(300, 180);
        connectionDialog.setLocationRelativeTo(null);
        JPanel connectionDialogPanel = new JPanel(new MigLayout("wrap1", "[]", "[][]"));
        connectionDialog.add(connectionDialogPanel);
        JLabel inputPlayerCount = new JLabel("Enter the Name of player " + (playerID + 1));
        JTextArea playerCount = new JTextArea("", 1, 1);
        connectionDialogPanel.add(inputPlayerCount);
        connectionDialogPanel.add(playerCount, "growx, growy");
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Arial", Font.PLAIN, 20));
        connectionDialogPanel.add(confirmButton, "split2, spanx, left, wmin 100, hmin 30");
        JButton confirmButton2 = new JButton("Cancel");
        confirmButton2.setFont(new Font("Arial", Font.PLAIN, 20));
        connectionDialogPanel.add(confirmButton2, "left, wmin 100, hmin 30");
        inputPlayerCount.setFont(new Font("Arial", Font.PLAIN, 17));
        playerCount.setBounds(10, 10, 10, 10);
        playerCount.setFont(new Font("Arial", Font.PLAIN, 17));
        playerCount.setOpaque(true);
        connectionDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        confirmButton.addActionListener(new ActionListener() { //confirmed
            public void actionPerformed(ActionEvent e) {
                String playerName = (String) playerCount.getText();
                connectionData.add(playerName);
                connectionDialog.dispose();
            }
        });
        confirmButton2.addActionListener(new ActionListener() { //cancelled
            public void actionPerformed(ActionEvent e) {
                connectionDialog.dispose();
                System.exit(0);
            }
        });
        connectionDialog.setResizable(false);
        connectionDialog.pack();
        connectionDialog.setVisible(true);


    }

    @Override
    public void confirm() {

        String[] s = connectionData.toArray(new String[0]);
        handler.setConnectionData(s);
    }
}
