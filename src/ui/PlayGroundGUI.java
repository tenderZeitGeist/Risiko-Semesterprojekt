package ui;

import domain.Risiko;
import domain.exceptions.PlayerAlreadyExistsException;
import net.miginfocom.swing.MigLayout;
import valueobjects.Continent;
import valueobjects.Country;

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
import java.util.Vector;

/**
 * Created by ZeitGeist on 14.06.2017.
 */
public class PlayGroundGUI extends JFrame {
    private Risiko risk;
    private BufferedImage in;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new PlayGroundGUI().startGame();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
                    int playerAmount = 0;
                    while (playerAmount < playerCount) {
                        try {
                            String playerName = JOptionPane.showInputDialog("Please insert the name of Player " + (playerAmount + 1));
                            risk.createPlayer(playerAmount++, playerName);
                        } catch (PlayerAlreadyExistsException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage() + " Please use another name.", "Player already exists.", JOptionPane.ERROR_MESSAGE);
                            playerAmount--;
                        }
                    }
                    isValid = true;
                }
                createGameGUI();

            }
        });

        JButton loadGameButton = new JButton("Load Game");
        startPanel.add(loadGameButton, "cell 0 1");
        loadGameButton.addActionListener(new ActionListener() {
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

    public void createGameGUI() {


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
        JButton button1 = new JButton("Next Phase");
        button1.setFont(new Font("Arial", Font.PLAIN, 20));
        JButton button2 = new JButton("Save Game");
        button2.setFont(new Font("Arial", Font.PLAIN, 20));
        JButton button3 = new JButton("Load Game");
        button3.setFont(new Font("Arial", Font.PLAIN, 20));

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
        buttonPanel.add(button1, "cell 0 0, sg b, wmin 140, hmin 40");
        buttonPanel.add(button2, "cell 0 1, sg b");
        buttonPanel.add(button3, "cell 0 2, sg b");

        //
        this.add(gamePanel);
        this.add(buttonPanel, "aligny top, center, wrap");
        this.add(console, "grow");
        this.add(playerPanel);

        //
        this.revalidate();
        this.repaint();
        this.pack();
        this.setLocationRelativeTo(null);

        // In this part, the necessary methods for the gameplay are being called
        risk.distributeCountries();
        risk.distributeMissions();
        //gameRound ( );


    }

    public void gameRound() {

        // Primitive data necessary to play the game
        boolean game = true, distributePhase = true, attackPhase = true, movePhase = true;
        int playerPointer = 0;
        int gameRound = 0;

        // List of all phases
        ArrayList<Boolean> gamePhaseList = new ArrayList<>();
        gamePhaseList.add(distributePhase);
        gamePhaseList.add(attackPhase);
        gamePhaseList.add(movePhase);

        // Shuffle the order of players
        Collections.shuffle(risk.getPlayerList());

        game:
        while (game) {
            playerPointer = playerPointer % risk.getPlayerList().size();
            JOptionPane.showMessageDialog(null, "It is " + risk.getPlayerList().get(playerPointer).getPlayerName() + "'s turn!");
            distributePhase:
            while (distributePhase) {
                System.out.println("Please distribute your troops.");
                System.out.println("");
            }
            while (attackPhase) {
                System.out.println("You may attack now!");

            }
            while (movePhase) {
                System.out.println("Move and fortify your countries.");
            }

            //Resets all boolean to true
            for (boolean b : gamePhaseList) {
                b = true;
            }
        }
    }


    public void createMapClickListener(JLabel fgPanel, BufferedImage bgPicture) {
        fgPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int packetInt = bgPicture.getRGB(e.getX(), e.getY());
                Color color = new Color(packetInt, true);
                //System.out.print("red:  " + Integer.toString(color.getRed()));
                //System.out.print(" green:  " + Integer.toString(color.getGreen()));
                //System.out.print(" blue:  " + Integer.toString(color.getBlue()));
                //System.out.println(" alpha:  " + Integer.toString(color.getAlpha()));

                String RGBString = "" + color.getRed() + color.getGreen() + color.getBlue();
                int RGBvalue = Integer.parseInt(RGBString);
                System.out.println(RGBvalue);
                //risk.compareRGB(RGBvalue);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public static BufferedImage resizeBuffImg(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

}