package ui;

import domain.Risiko;
import domain.exceptions.NoAlliedCountriesNearException;
import domain.exceptions.NoEnemyCountriesNearException;
import domain.exceptions.PlayerAlreadyExistsException;
import net.miginfocom.swing.MigLayout;
import valueobjects.Country;
import valueobjects.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;

/**
 * Created by ZeitGeist on 14.06.2017.
 */
public class PlayGroundGUI extends JFrame {
    private Risiko risk;
    private BufferedImage in;
    private int gamePhase;
    private Player thisPlayer;
    private JButton nextPhaseButton;
    private JButton saveGameButton;
    private JButton loadGameButton;

    public static void main(String[] args) {


                try {
                    new PlayGroundGUI().startGame();
                } catch (IOException e) {
                    e.printStackTrace();
                }


    }

    private void exitGameSecurely() {
        //TODO add end game context
        System.exit(this.EXIT_ON_CLOSE);
    }


    public void startGame() throws IOException {
        // Create link to Risiko class
        risk = new Risiko();  // Übergabe von Daten zum Einlesen wurde deaktiviert!!!

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
                risk.createGameFile();
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
                            String playerName = JOptionPane.showInputDialog("Please insert the name of Player " + (playerID+1));
                            risk.createPlayer(playerID+1, playerName);
                        } catch (PlayerAlreadyExistsException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage() + " Please use another name.", "Player already exists.", JOptionPane.ERROR_MESSAGE);
                            playerID--;
                        }
                        playerID++;
                    }
                    isValid = true;

                }
                initGameGUI();
                //roundManager(0);

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
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitGameSecurely();
            }
        });
        // this.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void initGameGUI() {
        // Set screen size resolution of the GUI
        // TODO Necessary?

        getContentPane().removeAll();
        this.setLayout(new MigLayout(
                "debug",
                "[][]",
                "[][][]"));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.5);
        int height = (int) (screenSize.getHeight() * 0.5);

        // Get the board
        JLabel fgPictureLabel = new JLabel();
        BufferedImage fgPicture;
        BufferedImage bgPicture = null;

        try {
            //extremely redundant scaling...
            bgPicture = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/starRiskColorCoded.png"));
            bgPicture = resizeBuffImg(bgPicture, (int) (bgPicture.getWidth() * 0.5), (int) (bgPicture.getHeight() * 0.5));

            //extremely redundant scaling...
            fgPicture = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/StarRiskBg.png"));
            fgPicture = resizeBuffImg(fgPicture, (int) (fgPicture.getWidth() * 0.5), (int) (fgPicture.getHeight() * 0.5));

            fgPictureLabel = new JLabel(new ImageIcon(fgPicture));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        createMapClickListener(fgPictureLabel, bgPicture);


        nextPhaseButton = new JButton("Next Phase");
        saveGameButton = new JButton("Save Game");
        loadGameButton = new JButton("Load Game");
        nextPhaseButton.setFont(new Font("Arial", Font.PLAIN, 20));
        saveGameButton.setFont(new Font("Arial", Font.PLAIN, 20));
        loadGameButton.setFont(new Font("Arial", Font.PLAIN, 20));

        nextPhaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                risk.nextPhase();
            }
        });
        nextPhaseButton.setEnabled(false);


        //----------->
        // Add new UI elements into the panel
        JPanel gamePanel = new JPanel();
        JPanel playerPanel = new JPanel();
        JPanel consolePanel = new JPanel();
        JPanel buttonPanel = new JPanel(new MigLayout(
                "ins 5",
                "",
                "[][][]"
        ));

        // Creating buttons
        


        // Creating textarea for sysout
        JTextArea actionPerformedText = new JTextArea("", 10, 20);
        actionPerformedText.setEditable(false);
        JScrollPane console = new JScrollPane(actionPerformedText,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JTextAreaOutputStream out = new JTextAreaOutputStream(actionPerformedText);
        System.setOut(new PrintStream(out));

        // Creating player list pane for displaying active players
        JTextArea playerListText = new JTextArea("", 10, 20);
        playerListText.setEditable(false);
        JScrollPane playerListPane = new JScrollPane(playerListText,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


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
        gamePanel.add(fgPictureLabel);
        //gamePanel.add(fgPictureLabel);

        String workingDir = System.getProperty("user.dir");
        System.out.println("Current working directory : " + workingDir);

        consolePanel.add(console);
        playerPanel.add(playerListPane);



        //
        this.add(gamePanel);
        this.add(buttonPanel, "aligny top, center, wrap");
        this.add(console, "grow");
        this.add(playerPanel);

        buttonPanel.add(nextPhaseButton);
        buttonPanel.add(saveGameButton);
        buttonPanel.add(loadGameButton);
        //
        this.revalidate();
        this.repaint();
        this.pack();
        this.setLocationRelativeTo(null);

        // In this part, the necessary methods for the gameplay are being called
        Collections.shuffle(risk.getPlayerList());
        risk.distributeCountries();
        risk.distributeMissions();
        //gameRound ( );


    }

    public void roundManager(int currentPlayerID) {
        Player currentPlayer = risk.getPlayerList().get(currentPlayerID);
            switch (risk.getTurn().getPhase()){
                case DISTRIBUTE:

                    risk.nextPhase();
                    break;
                case ATTACK:
                    nextPhaseButton.setEnabled(true);

                    risk.nextPhase();
                    break;
                case MOVE:

                    risk.nextTurn(currentPlayer);
                    break;
            }
            currentPlayerID++;
            int nextplayer = currentPlayerID % risk.getPlayerList().size();
            roundManager(nextplayer);

    }


    public void createMapClickListener(JLabel fgPanel, BufferedImage bgPicture) {
        fgPanel.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                int packetInt = bgPicture.getRGB(e.getX(), e.getY());
                Color color = new Color(packetInt, true);

                String RGBString = "" + color.getRed() + color.getGreen() + color.getBlue();
                int RGBvalue = Integer.parseInt(RGBString);
                System.out.println(RGBvalue);
                checkClickedCountry(risk.compareRGB(RGBvalue));

            }
        });
    }


    public void checkClickedCountry(Country selectedCountry) {
        switch (risk.getTurn().getPhase()){
            case DISTRIBUTE:


                risk.nextPhase();
                break;
            case ATTACK:
                nextPhaseButton.setEnabled(true);

                risk.nextPhase();
                break;
            case MOVE:

                risk.nextTurn();
                break;
        }

    }

    public void distributePhase(Player currentPlayer) {
        boolean distributeDone = false;

        createButtonsdist();

        try {
            risk.loadDistributionCountriesList(currentPlayer);
        } catch (NoAlliedCountriesNearException e) {
            //e.printStackTrace();
            //distributeDone = true;
        }
        while (!distributeDone) {
            //distribute
            gamePhase = 1;


        }
    }

    public void attackingPhase(Player currentPlayer) {
        boolean attackingDone = false;
        try {
            risk.loadAttackingCountriesList(currentPlayer);
        } catch (NoEnemyCountriesNearException e) {
            //e.printStackTrace();
            attackingDone = true;
        }
        while (!attackingDone) {
            //attack
            gamePhase = 2;

        }
    }

    public void movePhase(Player currentPlayer) {
        boolean movingDone = false;
        try {
            risk.loadDistributionCountriesList(currentPlayer);
        } catch (NoAlliedCountriesNearException e) {
            //e.printStackTrace();
            movingDone = true;
        }
        while (!movingDone) {
            //attack
            gamePhase = 3;


        }
    }


    public static BufferedImage resizeBuffImg(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public void createButtonsdist() {
        JButton nextPhaseButton = new JButton("Next Phase");
        JButton saveGameButton = new JButton("Save Game");
        JButton loadGameButton = new JButton("Load Game");
        nextPhaseButton.setFont(new Font("Arial", Font.PLAIN, 20));
        saveGameButton.setFont(new Font("Arial", Font.PLAIN, 20));
        loadGameButton.setFont(new Font("Arial", Font.PLAIN, 20));

        /*buttonPanel.add(nextPhaseButton, "cell 0 0, sg b, wmin 140, hmin 40");
        buttonPanel.add(saveGameButton, "cell 0 1, sg b");
        buttonPanel.add(loadGameButton, "cell 0 2, sg b");*/

    }

}