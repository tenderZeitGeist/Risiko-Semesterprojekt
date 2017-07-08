package ui;

import domain.Risiko;
import net.miginfocom.swing.MigLayout;
import valueobjects.Country;
import valueobjects.Player;
import valueobjects.Turn;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by ZeitGeist on 14.06.2017.
 */
public class PlayGroundGUI extends JFrame {
    private Risiko risk;
    private StartDialog startDialog;
    private BufferedImage in;
    private Graphics canvas;
    private BufferedImage fgPicture;
    private JLabel fgPictureLabel = new JLabel();

    private int gamePhase;
    private Player thisPlayer;
    private JButton nextPhaseButton;
    private JButton saveGameButton;
    private JButton loadGameButton;
    private Vector < Country > disabledCountriesList;
    private Vector < Country > enabledCountriesList;
    private Turn turn;
    int initForces;

    public static void main ( String[] args ) throws IOException {
        SwingUtilities.invokeLater ( new Runnable ( ) {
            @Override
            public void run ( ) {
                try {
                    new PlayGroundGUI ( ).startGame ( );
                } catch ( IOException e ) {
                    e.printStackTrace ( );
                }
            }
        } );

    }

    private void exitGameSecurely ( ) {
        //TODO add end game context
        System.exit ( this.EXIT_ON_CLOSE );
    }


    public void startGame ( ) throws IOException {
        // Create link to Risiko class
        risk = new Risiko ( );  // Übergabe von Daten zum Einlesen wurde deaktiviert!!!
        startDialog = new StartDialog ( );
        SwingUtilities.invokeLater ( new Runnable ( ) {
            @Override
<<<<<<< HEAD
            public void actionPerformed(ActionEvent e) {
                try {
                    risk.readData("Risiko-Semesterprojekt/countryList.txt");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
                    int playerID = 0;
                    while (playerID < playerCount) {
                        try {
                            String playerName = JOptionPane.showInputDialog("Please insert the name of Player " + (playerID + 1));
                            risk.createPlayer(playerID + 1, playerName);
                        } catch (PlayerAlreadyExistsException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage() + " Please use another name.", "Player already exists.", JOptionPane.ERROR_MESSAGE);
                            playerID--;
                        }
                        playerID++;
                    }
                    isValid = true;

                }
                initGameGUI();


            }
        });

        JButton loadGameButtonInitial = new JButton("Load Game");
        startPanel.add(loadGameButtonInitial, "cell 0 1");
        loadGameButtonInitial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Function is not yet implemented.", "Unavailable", JOptionPane.ERROR_MESSAGE);
=======
            public void run ( ) {
               risk = startDialog.startGameDialog ( risk );
>>>>>>> GUI
            }

        } );
        initGameGUI ( );
    }

    public void initGameGUI ( ) {
        // Set screen size resolution of the GUI
        // TODO Necessary?


        this.setLayout ( new MigLayout (
                "debug",
                "[][]",
                "[][][]" ) );
        this.addWindowListener ( new WindowAdapter ( ) {
            @Override
            public void windowClosing ( WindowEvent e ) {
                exitGameSecurely ( );
            }
        } );

        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );
        int width = ( int ) ( screenSize.getWidth ( ) * 0.5 );
        int height = ( int ) ( screenSize.getHeight ( ) * 0.5 );

        // Get the board
<<<<<<< HEAD


=======
        JLabel fgPictureLabel = new JLabel ( );
        BufferedImage fgPicture;
>>>>>>> GUI
        BufferedImage bgPicture = null;

        try {
            //extremely redundant scaling...
            bgPicture = ImageIO.read ( new File ( "./Risiko-Semesterprojekt/src/ui/rescourcen/starRiskColorCoded.png" ) );
            bgPicture = resizeBuffImg ( bgPicture, ( int ) ( bgPicture.getWidth ( ) * 0.4 ), ( int ) ( bgPicture.getHeight ( ) * 0.4 ) );

            //extremely redundant scaling...
<<<<<<< HEAD
            fgPicture = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/starRiskBG.png"));
            fgPicture = resizeBuffImg(fgPicture, (int) (fgPicture.getWidth() * 0.5), (int) (fgPicture.getHeight() * 0.5));
=======
            fgPicture = ImageIO.read ( new File ( "./Risiko-Semesterprojekt/src/ui/rescourcen/StarRiskBg.png" ) );
            fgPicture = resizeBuffImg ( fgPicture, ( int ) ( fgPicture.getWidth ( ) * 0.4 ), ( int ) ( fgPicture.getHeight ( ) * 0.4 ) );
>>>>>>> GUI

            fgPictureLabel = new JLabel ( new ImageIcon ( fgPicture ) );

<<<<<<< HEAD

        } catch (IOException e) {
=======
        } catch ( IOException e ) {
>>>>>>> GUI
            // TODO Auto-generated catch block
            e.printStackTrace ( );
        }


        createMapClickListener ( fgPictureLabel, bgPicture );


        nextPhaseButton = new JButton ( "Next Phase" );
        saveGameButton = new JButton ( "Save Game" );
        loadGameButton = new JButton ( "Load Game" );
        nextPhaseButton.setFont ( new Font ( "Arial", Font.PLAIN, 20 ) );
        saveGameButton.setFont ( new Font ( "Arial", Font.PLAIN, 20 ) );
        loadGameButton.setFont ( new Font ( "Arial", Font.PLAIN, 20 ) );

        nextPhaseButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                risk.nextPhase ( );
            }
        } );
        nextPhaseButton.setEnabled ( false );


        //----------->
        // Add new UI elements into the panel
        JPanel gamePanel = new JPanel ( );
        JPanel playerPanel = new JPanel ( );
        JPanel consolePanel = new JPanel ( );
        JPanel buttonPanel = new JPanel ( new MigLayout (
                "ins 5",
                "",
                "[][][]"
        ) );

        // Creating buttons


        // Creating textarea for sysout
        JTextArea actionPerformedText = new JTextArea ( "", 6, 20 );
        actionPerformedText.setFont ( actionPerformedText.getFont ( ).deriveFont ( 30f ) );
        actionPerformedText.setEditable ( false );
        JScrollPane console = new JScrollPane ( actionPerformedText,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
        JTextAreaOutputStream out = new JTextAreaOutputStream ( actionPerformedText );
        System.setOut ( new PrintStream ( out ) );

        // Creating player list pane for displaying active players
        JTextArea playerListText = new JTextArea ( "", 10, 20 );
        playerListText.setEditable ( false );
        JScrollPane playerListPane = new JScrollPane ( playerListText,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );


        /*//---
        //--- Add content to mainWindowPanel
        gamePanel.add ( playBoard, "left, growx, growy" );
        gamePanel.add ( button1, "sg b, cell 3 0, flowy, al 100% 10%, wmin 140, hmin 40" );
        gamePanel.add ( button2, "sg b, cell 3 0, flowy" );
        gamePanel.add ( button3, "sg b, cell 3 0, flowy" );
        gamePanel.add ( button4, "sg b, cell 3 0, flowy" );
        gamePanel.add ( button5, "sg b, cell 3 0, flowy, wrap" );
        gamePanel.add ( console, "pushx, span, growx" );
*/

        // Add the objects into the provided panels
        //gamePanel.add(bgPictureLabel);
        gamePanel.add ( fgPictureLabel );
        //gamePanel.add(fgPictureLabel);

        String workingDir = System.getProperty ( "user.dir" );
        System.out.println ( "Current working directory : " + workingDir );

        consolePanel.add ( console );
        playerPanel.add ( playerListPane );


        //
        this.add ( gamePanel );
        this.add ( buttonPanel, "aligny top, center, wrap" );
        this.add ( console, "grow" );
        this.add ( playerPanel );

        buttonPanel.add ( nextPhaseButton );
        buttonPanel.add ( saveGameButton );
        buttonPanel.add ( loadGameButton );
        //
<<<<<<< HEAD
        this.revalidate();
        //this.repaint();
        this.pack();
        this.setLocationRelativeTo(null);
=======
        this.revalidate ( );
        this.repaint ( );
        this.pack ( );
        this.setLocationRelativeTo ( null );
        this.setVisible ( true );
>>>>>>> GUI

        // In this part, the necessary methods for the gameplay are being called
        // Rausgenommen und in die StartDialog-Klasse hinzugefügt.
        //gameRound ( );
        risk.startTurn(risk.getCurrentPlayer());
        roundManager(risk.getCurrentPlayer());


    }

    public void roundManager ( Player currentPlayer ) {

        switch ( risk.getTurn ( ).getPhase ( ) ) {
            case DISTRIBUTE:
<<<<<<< HEAD

                fgPictureLabel.repaint();
                Vector<Country> ownedCountriesList = risk.loadOwnedCountryList(currentPlayer);
                initForces = risk.returnForcesPerRoundsPerPlayer(currentPlayer);
                displayCountries(ownedCountriesList);

=======
                Vector < Country > ownedCountriesList = risk.loadOwnedCountryList ( currentPlayer );
                initForces = risk.returnForcesPerRoundsPerPlayer ( currentPlayer );
                displayCountries ( ownedCountriesList );
>>>>>>> GUI

                break;
            case ATTACK:
                nextPhaseButton.setEnabled ( true );

                risk.nextPhase ( );
                break;
            case MOVE:

                risk.nextTurn ( currentPlayer );
                risk.setNextPlayer ( );
                roundManager ( risk.getCurrentPlayer ( ) );
                break;
        }


    }


    public void createMapClickListener ( JLabel fgPanel, BufferedImage bgPicture ) {
        fgPanel.addMouseListener ( new MouseAdapter ( ) {
            @Override
            public void mouseClicked ( MouseEvent e ) {
                int packetInt = bgPicture.getRGB ( e.getX ( ), e.getY ( ) );
                Color color = new Color ( packetInt, true );
                //RGB to Hex
                String hex = String.format ( "%02X%02X%02X", color.getRed ( ), color.getGreen ( ), color.getBlue ( ) );

<<<<<<< HEAD
                if (!hex.equals("000000")) {
                    System.out.print(hex + "  ");
                    Country c = risk.compareHEX(hex);
                    System.out.println(c.getCountryName() + " " + c.getX() + " " + c.getY());
                    int x = e.getX();
                    int y = e.getY();

                    //printCoords
                    Graphics2D g = (Graphics2D) fgPictureLabel.getGraphics();
                    g.setColor(Color.red);
                    g.setStroke(new BasicStroke(3));
                    g.drawOval(x - 10, y - 10, 20, 20);
                    g.dispose();
                    //--------------------






                    /*switch (risk.getTurn().getPhase()) {
=======
                if ( ! hex.equals ( "000000" ) ) {
                    System.out.print ( hex + "  " );
                    System.out.println ( risk.compareHEX ( hex ).getCountryName ( ) );
                    switch ( risk.getTurn ( ).getPhase ( ) ) {
>>>>>>> GUI
                        case DISTRIBUTE:
                            Vector < Country > ownedCountriesList = risk.loadOwnedCountryList ( risk.getCurrentPlayer ( ) );
                            //System.out.print(hex + "  ");
                            Country selectedCountry = risk.compareHEX ( hex );
                            //System.out.println(risk.compareHEX(hex).getCountryName());

<<<<<<< HEAD
                            if (ownedCountriesList.contains(selectedCountry)) {
=======
                            if ( ownedCountriesList.contains ( selectedCountry ) ) {
>>>>>>> GUI
                                //TODO handle if selected country is owned by current player....
                                System.out.println ( "I own  " + selectedCountry.getCountryName ( ) );
                                initForces--;
                            }

<<<<<<< HEAD
                            if (initForces < 1) {
                                risk.nextPhase();
=======
                            if ( initForces < 1 ) {
                                risk.nextPhase ( );
>>>>>>> GUI
                            }

                            break;
                        case ATTACK:


                            break;
                        case MOVE:


                            break;
                    }*/


                } else {
                    //printCoords

                    //---------------------
                }
            }
        } );
    }


    public static BufferedImage resizeBuffImg ( BufferedImage img, int newW, int newH ) {
        Image tmp = img.getScaledInstance ( newW, newH, Image.SCALE_SMOOTH );
        BufferedImage dimg = new BufferedImage ( newW, newH, BufferedImage.TYPE_INT_ARGB );

        Graphics2D g2d = dimg.createGraphics ( );
        g2d.drawImage ( tmp, 0, 0, null );
        g2d.dispose ( );

        return dimg;
    }

    public void createButtonsdist ( ) {
        JButton nextPhaseButton = new JButton ( "Next Phase" );
        JButton saveGameButton = new JButton ( "Save Game" );
        JButton loadGameButton = new JButton ( "Load Game" );
        nextPhaseButton.setFont ( new Font ( "Arial", Font.PLAIN, 20 ) );
        saveGameButton.setFont ( new Font ( "Arial", Font.PLAIN, 20 ) );
        loadGameButton.setFont ( new Font ( "Arial", Font.PLAIN, 20 ) );

        /*buttonPanel.add(nextPhaseButton, "cell 0 0, sg b, wmin 140, hmin 40");
        buttonPanel.add(saveGameButton, "cell 0 1, sg b");
        buttonPanel.add(loadGameButton, "cell 0 2, sg b");*/
    }


    public void displayCountries ( Vector < Country > ownedCountriesList ) {
        //TODO show green glow(or sth) on countries that belong to you...
        Graphics2D g2 = (Graphics2D) fgPictureLabel.getGraphics();
        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(10));
        for (Country country : ownedCountriesList) {
            System.out.println(country.getCountryName());


            int x = country.getX();
            int y = country.getY();

            g2.drawOval(x - 10, y - 10, 50, 50);

            System.out.println("painted " + country.getX());
            System.out.println("painted " + country.getY());
            System.out.println("");
        }
        g2.dispose();
    }

}