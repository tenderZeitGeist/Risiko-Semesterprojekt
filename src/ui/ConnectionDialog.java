package ui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 01.06.2017.
 */
public class ConnectionDialog {

    private ConnectionDataHandler handler = null;

    public ConnectionDialog(ConnectionDataHandler handler) {
        this.handler = handler;
    }


    public void createDialog() {
        JDialog connectionDialog = new JDialog();
        connectionDialog.setTitle("¯\\_(ツ)_/¯");
        connectionDialog.setSize(300, 180);
        connectionDialog.setLocationRelativeTo(null);

        JPanel connectionDialogPanel = new JPanel(new MigLayout("wrap1", "[]", "[][][][]"));
        connectionDialog.add(connectionDialogPanel);

        JLabel inputNameLabel = new JLabel("enter name please:");
        JLabel inputIpLabel = new JLabel("enter IP please:");
        JLabel inputPortLabel = new JLabel("enter port please:");

        JTextArea inputName = new JTextArea("", 1, 1);

        JTextArea inputPort = new JTextArea("", 1, 1);
        JTextArea inputIp = new JTextArea("", 1, 1);


        connectionDialogPanel.add(inputNameLabel);
        connectionDialogPanel.add(inputName, "growx, growy");
        connectionDialogPanel.add(inputIpLabel);
        connectionDialogPanel.add(inputIp, "growx, growy");
        connectionDialogPanel.add(inputPortLabel);
        connectionDialogPanel.add(inputPort, "growx, growy");

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Arial", Font.PLAIN, 20));
        connectionDialogPanel.add(confirmButton, "split2, spanx, left, wmin 100, hmin 30");
        JButton confirmButton2 = new JButton("Cancel");
        confirmButton2.setFont(new Font("Arial", Font.PLAIN, 20));
        connectionDialogPanel.add(confirmButton2, "left, wmin 100, hmin 30");


        inputNameLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        inputName.setBounds(10, 10, 10, 10);
        inputName.setFont(new Font("Arial", Font.PLAIN, 17));
        inputName.setOpaque(true);

        inputIpLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        inputIp.setBounds(10, 10, 10, 10);
        inputIp.setFont(new Font("Arial", Font.PLAIN, 17));
        inputIp.setOpaque(true);

        inputPortLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        inputPort.setBounds(10, 10, 10, 10);
        inputPort.setFont(new Font("Arial", Font.PLAIN, 17));
        inputPort.setOpaque(true);

        connectionDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        confirmButton.addActionListener(new ActionListener() { //confirmed
            public void actionPerformed(ActionEvent e) {
                String name;
                String ip;
                String port;
                boolean successful = false;


                name = inputName.getText();
                ip = inputIp.getText();
                port = inputPort.getText();
                confirm(name, ip, port);
                successful = true;

                if (successful) {
                    connectionDialog.dispose();
                }
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

    public void confirm(String name, String host, String port) {
        // liest Textfelder
        String[] s = new String[]{name, host, port};

        handler.setConnectionData(s);
    }
}
