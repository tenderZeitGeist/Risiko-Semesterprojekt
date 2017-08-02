package Client.ui.customUiElements;

import domain.Risiko;
import net.miginfocom.swing.MigLayout;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;
/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 09.07.2017.
 */
public class NumberSelectorDialog extends ModalDialog {
    private String InfoText;
    private ConnectionDataHandler handler;
    private Risiko risk;
    private Vector<String> connectionData = new Vector<>();


    public NumberSelectorDialog(String InfoText, ConnectionDataHandler handler) throws IOException {
        this.handler = handler;
        this.InfoText = InfoText;
        risk = new Risiko();
    }


    @Override
    public void createDialog() {
        JDialog connectionDialog = new JDialog();
        connectionDialog.setModal(true);
        connectionDialog.setTitle("Dialog");
        connectionDialog.setSize(400, 180);
        connectionDialog.setLocationRelativeTo(null);
        JPanel connectionDialogPanel = new JPanel(new MigLayout("wrap1", "[]", "[][]"));
        connectionDialog.add(connectionDialogPanel);
        JLabel inputTextLabel= new JLabel(InfoText);
        JTextArea inputTextField = new JTextArea("", 1, 1);
        inputTextLabel.setFont(new Font("Arial", Font.PLAIN, 17));

        connectionDialogPanel.add(inputTextLabel);
        connectionDialogPanel.add(inputTextField, "growx, growy");

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Arial", Font.PLAIN, 20));
        connectionDialogPanel.add(confirmButton, "split2, spanx, left, wmin 100, hmin 30");

        JButton confirmButton2 = new JButton("Cancel");
        confirmButton2.setFont(new Font("Arial", Font.PLAIN, 20));
        connectionDialogPanel.add(confirmButton2, "left, wmin 100, hmin 30");

        inputTextField.setBounds(10, 10, 10, 10);
        inputTextField.setFont(new Font("Arial", Font.PLAIN, 17));
        inputTextField.setOpaque(true);
        connectionDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        confirmButton.addActionListener(new ActionListener() { //confirmed
            public void actionPerformed(ActionEvent e) {


                if(inputTextField.getText() != null) {
                    String[] connectionDataString = inputTextField.getText().split("\\s+");
                    for (int i = 0; i <connectionDataString.length ; i++) {
                        connectionData.add(connectionDataString[i]);
                    }
                    confirm();
                    connectionDialog.dispose();
                } else {
                    connectionDialog.dispose();
                    createDialog();
                }

            }
        });
        confirmButton2.addActionListener(new ActionListener() { //cancelled
            public void actionPerformed(ActionEvent e) {
                connectionDialog.dispose();

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
