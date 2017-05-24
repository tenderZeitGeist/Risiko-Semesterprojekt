package ui;

import domain.Risiko;
import valueobjects.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.*;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 19.05.2017.
 */
public class PlayGroundGUI {


//    public PlayGroundGUI(String file) throws IOException {
//
//        risiko = new Risiko(file);
//        in = new BufferedReader(new InputStreamReader(System.in));
//    }
    public static void main(String[] args) {
        JFrame meinFrame = new JFrame("Beispiel JFrame");
        /* Wir setzen die Breite und die Höhe
           unseres Fensters auf 200 Pixel */
        meinFrame.setSize(200, 200);
        // Wir lassen unseren Frame anzeigen
        meinFrame.setVisible(true);


    }
}
