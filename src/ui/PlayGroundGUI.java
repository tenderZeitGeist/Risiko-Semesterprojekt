package ui;

import domain.Risiko;
import domain.exceptions.PlayerAlreadyExistsException;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ZeitGeist on 14.06.2017.
 */
public class PlayGroundGUI extends JFrame {
    private JButton startGameButton, loadGameButton, button1;
    private JPanel gamePanel;
    private Risiko risk;
    private BufferedImage in;


    public static void main ( String[] args ) {
        SwingUtilities.invokeLater ( new Runnable ( ) {
            @Override
            public void run ( ) {
                new PlayGroundGUI ( ).startGame ( );
            }
        } );
    }

    private void exitGameSecurely() {
        //TODO add end game context
        System.exit (this.EXIT_ON_CLOSE);
    }

    public void startGame ( ) {
        // Create link to Risiko class
        risk = new Risiko ( );  // Übergabe von Daten zum Einlesen wurde deaktiviert!!!

        // Create starting GUI
        gamePanel = new JPanel ( new MigLayout (
                "ins 24",
                "",
                "[]16[]" )
        );

        gamePanel.setBorder ( BorderFactory.createLineBorder ( Color.black ) );
        this.add ( gamePanel );
        this.setResizable ( false );

        //Insert and create buttons
        startGameButton = new JButton ( "Start Game" );
        gamePanel.add ( startGameButton, "cell 0 0" );
        startGameButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                risk.createGameFile ( );
                boolean isValid = false;
                int playerCount = 0;

                // Query for player creation
                while ( ! isValid ) {
                    try {
                        playerCount = Integer.parseInt ( JOptionPane.showInputDialog ( "Please insert the amount of players." ) );
                    } catch ( NumberFormatException ex ) {
                        JOptionPane.showMessageDialog ( null, "You have entered an invalid value. Please try again", "Invalid value.", JOptionPane.ERROR_MESSAGE );
                        continue;
                    }
                    if ( ( playerCount < 2 && playerCount > 6 ) ) {
                        JOptionPane.showMessageDialog ( null, "The recommended player numbers of players lies between 2 and 6 people. Please enter a different value.", "Invalid value.", JOptionPane.ERROR_MESSAGE );
                        continue;
                    }
                    isValid = true;

                    // Creating players
                    int playerAmount = 0;
                    while ( playerAmount < playerCount ) {
                        try {
                            String playerName = JOptionPane.showInputDialog ( "Please insert the name of Player " + ( playerAmount + 1 ) );
                            risk.createPlayer ( playerAmount++, playerName );
                        } catch ( PlayerAlreadyExistsException ex ) {
                            JOptionPane.showMessageDialog ( null, ex.getMessage ( ) + " Please use another name.", "Player already exists.", JOptionPane.ERROR_MESSAGE );
                            playerAmount--;
                        }
                    }
                    try {
                        createGameGUI ( );
                    } catch ( Exception ex ) {
                        System.out.println ( ex.getMessage ( ) );
                    }
                }
            }
        } );

        loadGameButton = new JButton ( "Load Game" );
        gamePanel.add ( loadGameButton, "cell 0 1" );
        loadGameButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                JOptionPane.showMessageDialog ( null, "Function is not yet implemented.", "Unavailable", JOptionPane.ERROR_MESSAGE );
            }
        } );

        // At last, creates the frame to display the GUI elements
        this.setContentPane ( gamePanel );
        this.addWindowListener ( new WindowAdapter ( ){
            @Override
            public void windowClosing(WindowEvent e) {
                exitGameSecurely();
            }
        });
        // this.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        this.pack ( );
        this.setLocationRelativeTo ( null );
        this.setVisible ( true );

    }

    public void createGameGUI ( ) throws Exception {

        // Set screen size resolution of the GUI
        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );
        int width = ( int ) ( screenSize.getWidth ( ) * 0.5 );
        int height = ( int ) ( screenSize.getHeight ( ) * 0.5 );
        this.setSize ( width, height );

        // Get the board
        BufferedImage myPicture;
        JLabel playBoard = new JLabel ( );

        try {
            myPicture = ImageIO.read ( new File ( "./src/ui/rescourcen/RiskBoardGame.jpg" ) );
            playBoard = new JLabel ( new ImageIcon ( myPicture.getScaledInstance ( ( int ) ( myPicture.getWidth ( ) * 0.5 ), ( int ) ( myPicture.getHeight ( ) * 0.5 ), Image.SCALE_FAST ) ) );

        } catch ( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace ( );
        } finally {
            // Remove all former elements
            gamePanel.removeAll ( );
        }

        //----------->
        // Add new UI elements into the panel
        gamePanel.setLayout ( new MigLayout (
                "debug, gap rel 12",
                "[][][]",
                "[][]" ) );

        button1 = new JButton ( "Next Phase" );
        button1.setFont ( new Font ( "Arial", Font.PLAIN, 20 ) );
        JButton button2 = new JButton ( "button2" );
        button2.setFont ( new Font ( "Arial", Font.PLAIN, 20 ) );
        JButton button3 = new JButton ( "button3" );
        button3.setFont ( new Font ( "Arial", Font.PLAIN, 20 ) );
        JButton button4 = new JButton ( "button4" );
        button4.setFont ( new Font ( "Arial", Font.PLAIN, 20 ) );
        JButton button5 = new JButton ( "button5" );
        button5.setFont ( new Font ( "Arial", Font.PLAIN, 20 ) );
        JTextArea actionPerformedText = new JTextArea ( "", 10, 20 );
        actionPerformedText.setEditable ( false );
        // JTextArea placeholderText1 = new JTextArea ( "placeholderText1", 10, 20 );
        JScrollPane console = new JScrollPane ( actionPerformedText,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        JTextAreaOutputStream out = new JTextAreaOutputStream ( actionPerformedText );
        System.setOut ( new PrintStream ( out ) );
        //---
        //--- Add content to mainWindowPanel
        //gamePanel.add ( placeholderText1, "left, growy" );
        gamePanel.add ( playBoard, "left, growx, growy" );
        gamePanel.add ( button1, "sg b, cell 3 0, flowy, al 100% 10%, wmin 140, hmin 40" );
        gamePanel.add ( button2, "sg b, cell 3 0, flowy" );
        gamePanel.add ( button3, "sg b, cell 3 0, flowy" );
        gamePanel.add ( button4, "sg b, cell 3 0, flowy" );
        gamePanel.add ( button5, "sg b, cell 3 0, flowy, wrap" );
        gamePanel.add ( console, "pushx, span, growx" );

        this.revalidate ( );
        this.repaint ( );
        this.pack ( );
        this.setLocationRelativeTo ( null );

        // In this part, the necessary methods for the gameplay are being called
        risk.distributeCountries ( );
        risk.distributeMissions ( );
        gameRound ( );
    }

    public void gameRound ( ) {
        // Primitive data necessary to play the game

        boolean game = true, distributePhase = true, attackPhase = true, movePhase = true;
        int playerPointer = 0;
        int gameRound = 0;

        // List of all phases
        ArrayList<Boolean> gamePhaseList = new ArrayList <> (  );
        gamePhaseList.add ( distributePhase );
        gamePhaseList.add ( attackPhase );
        gamePhaseList.add ( movePhase );

        // Shuffle the order of players
        Collections.shuffle ( risk.getPlayerList ( ) );

        game:
        while ( game ) {
            playerPointer = playerPointer % risk.getPlayerList ().size ();
            JOptionPane.showMessageDialog ( null, "It is " + risk.getPlayerList ( ).get ( playerPointer ).getPlayerName ( ) + "'s turn!" );
            distributePhase:
            while ( distributePhase ) {
                System.out.println ( "Please distribute your troops." );
                System.out.println ( "" );
            }
            while ( attackPhase ){
                System.out.println ("You may attack now!" );

            }
            while ( movePhase ){
                System.out.println ( "Move and fortify your countries." );
            }

            //Resets all boolean to true
            for ( boolean b : gamePhaseList ){
                b = true;
            }
        }
    }
}
