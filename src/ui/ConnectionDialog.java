package ui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

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
        connectionDialog.setTitle("enter connection details");
        connectionDialog.setSize(400, 300);

        JPanel connectionDialogPanel = new JPanel(new MigLayout("wrap1", "[]", "[][][][]"));
        connectionDialog.add(connectionDialogPanel);

        JLabel inputNameLabel = new JLabel("enter name please:");
        JLabel inputIpLabel = new JLabel("enter IP please:");
        JLabel inputPortLabel = new JLabel("enter port please:");

        JTextArea inputName = new JTextArea("",1,1);

        JTextArea inputPort = new JTextArea("",1,1);
        JTextArea inputIp = new JTextArea("",1,1);


        connectionDialogPanel.add(inputNameLabel);
        connectionDialogPanel.add(inputName, "growx, growy");
        connectionDialogPanel.add(inputIpLabel);
        connectionDialogPanel.add(inputIp, "growx, growy");
        connectionDialogPanel.add(inputPortLabel);
        connectionDialogPanel.add(inputPort, "growx, growy");

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Arial", Font.PLAIN, 30));
        connectionDialogPanel.add(confirmButton, "split2, spanx, left, wmin 200, hmin 70");
        JButton confirmButton2 = new JButton("Cancel");
        confirmButton2.setFont(new Font("Arial", Font.PLAIN, 30));
        connectionDialogPanel.add(confirmButton2, "left, wmin 200, hmin 70");


        inputNameLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        inputName.setBounds(10, 10, 10 ,10);
        inputName.setFont(new Font("Arial", Font.PLAIN, 30));
        inputName.setOpaque(true);

        inputIpLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        inputIp.setBounds(10, 10, 10 ,10);
        inputIp.setFont(new Font("Arial", Font.PLAIN, 30));
        inputIp.setOpaque(true);

        inputPortLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        inputPort.setBounds(10, 10, 10 ,10);
        inputPort.setFont(new Font("Arial", Font.PLAIN, 30));
        inputPort.setOpaque(true);

        connectionDialog.setResizable(false);
        connectionDialog.pack();

        connectionDialog.setVisible(true);
    }

    public void confirm() {
        // liest Textfelder
        handler.setConnectionData(null);
    }
}
