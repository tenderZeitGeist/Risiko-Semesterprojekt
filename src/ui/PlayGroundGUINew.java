package ui;

import domain.Risiko;
import domain.events.GameActionEvent;
import domain.events.GameControlEvent;
import domain.events.GameEvent;
import domain.exceptions.PlayerAlreadyExistsException;
import net.miginfocom.swing.MigLayout;
import ui.customUiElements.JTextAreaOutputStream;
import ui.customUiElements.ScalingSliderDialog;
import valueobjects.Continent;
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
import java.util.Vector;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 17.07.2017.
 */
public class PlayGroundGUINew {

    //Value Objects
    private Player player;
    //this should be replaced with server later
    private Risiko risiko;


    //UI
    private JFrame windowJFrame;
    private JButton nextPhaseButton;
    private JButton loadGameButton;
    private JButton saveGameButton;
    private JLabel fgPictureLabel = new JLabel();
    private JTextArea statusTextArea;
    private JPanel statusPanel;
    private JPanel buttonPanel;
    private JTextArea customSysout;
    private JScrollPane console;

    //Objects
    private BufferedImage bgPicture;
    private BufferedImage fgPicture;
    private BufferedImage fgPictureFix;
    private Image redFlag, greenFlag;


    //vars
    private Double scalingFactor;
    private boolean hovered = false;


    public static void main(String[] args) {
        //catch exceptions maybe?!

        try {
            new PlayGroundGUINew();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayGroundGUINew() throws IOException {
        //create server connection in here

        //this should be replaced with server later
        risiko = new Risiko();

        ScalingSliderDialog sc = new ScalingSliderDialog();


        scalingFactor = sc.createScalingSliderDialog()/100;

        System.out.println(scalingFactor);
        try {
            //IDEA: we use the code as is, but ask the player to
            //enter the name of his "alliance" (separatists, empire, rebels)
            risiko.createPlayer(0, "Separatists");
            player = new Player(0,"xXxPussyD3str0yazxXx");
        } catch (PlayerAlreadyExistsException e) {
            e.printStackTrace();
        }
        //init Pictures
        initPictureFiles();
        try {
            risiko.readData("Risiko-Semesterprojekt/countryList.txt");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        for (Country c : risiko.getCountryList()) {
            c.setOwningPlayer(risiko.getCurrentPlayer());
        }


        for (Country c : risiko.getCountryList()) {
            c.setX((int) (c.getX() * scalingFactor * 2));
            c.setY((int) (c.getY() * scalingFactor * 2));
        }
        initMainWindow();


    }

    public void initMainWindow() {
//Create Main windowJFrame
        windowJFrame = new JFrame("Star Risk: "+player.getPlayerName()+"    [ALPHA VERSION NOT FOR PUBLIC]");
        //windowJFrame.setBounds(1000, 1000, 1000, 1000);


        windowJFrame.setLayout(new MigLayout(
                "",
                "[][]",
                "[][][]"));
        windowJFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //exit game securely through here
                System.exit(0);
            }
        });

        windowJFrame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                windowJFrame.revalidate();
                windowJFrame.repaint();
                windowJFrame.pack();
            }
        });
        windowJFrame.getContentPane().setForeground(Color.WHITE);
        windowJFrame.getContentPane().setBackground(Color.BLACK);



//create new Elements
        //Init new Systemout
        initSysout();

        //Picture Panel
        JPanel gamePane = new JPanel();
        gamePane.add(fgPictureLabel);
        gamePane.setBackground(Color.BLACK);
        gamePane.setForeground(Color.WHITE);
        //Status Panel
        initStatusPanel();
        //ButtonPanel
        buttonPanel = new JPanel(new MigLayout(
                "ins 5",
                "",
                "[][][]"
        ));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setForeground(Color.WHITE);
        //mouse hover effects init
        createMouseHoverListener(fgPictureLabel, bgPicture);


//Add those Elements to main Frame

        windowJFrame.add(gamePane, "span 1 2");
        windowJFrame.add(statusPanel, "wrap");
        windowJFrame.add(buttonPanel, "aligny top, center, wrap");
        windowJFrame.add(console, "grow");

//Init some Elements after adding them
        //Button Panel
        initButtonPanel();


//End Initialisation

        windowJFrame.revalidate();
        windowJFrame.repaint();
        windowJFrame.pack();
        windowJFrame.setLocationRelativeTo(null);
        windowJFrame.setVisible(true);

        //this is some cool init stuffz:)
        paintFlagLabel ( );
        String workingDir = System.getProperty("user.dir");
        System.out.println("Current working directory : " + workingDir);

    }

    //Init functions
    public void initPictureFiles() {
        try {
            //extremely redundant scaling...
            bgPicture = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/starRiskColorCoded.png"));
            bgPicture = resizeBuffImg(bgPicture, (int) ((bgPicture.getWidth() * 0.5) * scalingFactor), (int) ((bgPicture.getHeight() * 0.5) * scalingFactor));

            redFlag = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/flag_icons/flag_red.png"));
            redFlag = redFlag.getScaledInstance(60, 60, 10);
            greenFlag = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/flag_icons/flag_green.png"));
            greenFlag = greenFlag.getScaledInstance(60, 60, 10);

            fgPictureFix = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/StarRiskBg.png"));
            fgPictureFix = resizeBuffImg(fgPictureFix, (int) ((fgPictureFix.getWidth() * 0.5) * scalingFactor), (int) ((fgPictureFix.getHeight() * 0.5) * scalingFactor));
            fgPicture = fgPictureFix;

            fgPictureLabel = new JLabel(new ImageIcon(fgPicture));
            //fgPictureLabel = new JLabel(new ImageIcon(fgPicture));
            //fgPictureLabel.paint();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void initStatusPanel() {
        statusPanel = new JPanel();
        statusTextArea = new JTextArea("Star: none \nMarines: none \nAlliance: none", 3, 18);
        statusPanel.add(statusTextArea, "growx growy");
        int scale = (int) (40 * scalingFactor);
        Font f = new Font("Arial", 0, scale);
        statusPanel.setForeground(Color.WHITE);
        statusPanel.setBackground(Color.BLACK);
        statusTextArea.setFont(f);
        statusTextArea.setForeground(Color.WHITE);
        statusTextArea.setBackground(Color.BLACK);
        statusTextArea.setEditable(false);
    }

    public void initButtonPanel() {
        //TODO change names of buttons maybe?!
        nextPhaseButton = new JButton("Next Phase");
        saveGameButton = new JButton("Save Game");
        loadGameButton = new JButton("Load Game");
        nextPhaseButton.setFont(new Font("Arial", Font.PLAIN, (int) (30 * scalingFactor)));

        saveGameButton.setFont(new Font("Arial", Font.PLAIN, (int) (30 * scalingFactor)));
        loadGameButton.setFont(new Font("Arial", Font.PLAIN, (int) (30 * scalingFactor)));
        buttonPanel.add(nextPhaseButton, "wrap");
        buttonPanel.add(saveGameButton, "wrap");
        buttonPanel.add(loadGameButton, "wrap");
        nextPhaseButton.setEnabled(false);
        saveGameButton.setEnabled(false);
        loadGameButton.setEnabled(true);


        nextPhaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        saveGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public void initSysout() {
        customSysout = new JTextArea("", 6, 20);
        customSysout.setFont(new Font("Arial", Font.PLAIN, (int) (30 * scalingFactor)));
        customSysout.setEditable(false);
        customSysout.setForeground(Color.BLACK);
        customSysout.setBackground(Color.GRAY);

        console = new JScrollPane(customSysout,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        console.setBorder(null);
        JTextAreaOutputStream out = new JTextAreaOutputStream(customSysout);
        System.setOut(new PrintStream(out));
        //following does not work, the window must be painted before!
        //String workingDir = System.getProperty("user.dir");
        //System.out.println("Current working directory : " + workingDir);
    }
    //End of init functions

    public static BufferedImage resizeBuffImg(BufferedImage img, int newW, int newH) {
        //Thanks Stackoverflow...
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_REPLICATE);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }

    public void createMouseHoverListener(JLabel fgPanel, BufferedImage bgPicture) {
        fgPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int packetInt = bgPicture.getRGB(e.getX(), e.getY());
                Color color = new Color(packetInt, false);
                //RGB to Hex
                String hex = String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());

                if (!hex.equals("000000")) {
                    if (!hovered) {
                        Country tempSelectedCountry = risiko.compareHEX(hex);
                        if (tempSelectedCountry != null) {
                            statusTextArea.setText("Star: " + tempSelectedCountry.getCountryName() + "\nMarines: " + tempSelectedCountry.getLocalForces() + "\nAlliance: " + tempSelectedCountry.getOwningPlayer().getPlayerName() + "");
                            hovered = true;
                        }
                    }
                } else if (hex.equals("000000")) {
                    statusTextArea.setText("Star: none \nMarines: none \nAlliance: none");
                    hovered = false;
                }
            }
        });
    }

    public void paintFlagLabel( ){
            for( Country currentCountry : risiko.getCountryList () ){
                JLabel flag = new JLabel (  );
                int x = currentCountry.getX ();
                int y = currentCountry.getY ();
                if( currentCountry.getOwningPlayer ().equals ( this.player ) ){
                    flag.setIcon ( new ImageIcon ( greenFlag ) );
                } else {
                    flag.setIcon ( new ImageIcon ( redFlag ) );
                }
                String coordinates = "pos " + x + " " + y ;
                fgPictureLabel.add ( flag, coordinates );
                fgPictureLabel.revalidate ();
                fgPictureLabel.repaint ();
        }

    }


    /**
     * END OF GUI!!!
     * FROM NOW ON ALL THE LOGIX
     * (dixhuxen)
     */

    public void roundManger() {
    }

    public void handleGameEvent(GameEvent event) {
        if (event instanceof GameControlEvent) {
            GameControlEvent gce = (GameControlEvent) event;
            switch (gce.getType()) {
                case GAME_STARTED:
                    JOptionPane.showMessageDialog(windowJFrame,
                            "The game has just begun... It's player " + gce.getPlayer().getPlayerName() + "'s turn.",
                            "Game Started",
                            JOptionPane.INFORMATION_MESSAGE);
                    // break statement deliberately omitted,
                    // since game event also carries information on next turn
                    //	  break;
                case NEXT_TURN:
                    Turn currentTurn = gce.getTurn();
                    Player currentPlayer = gce.getPlayer();
                    if (currentPlayer.equals(player)) {
                        // It is this player's turn!
                        // Update UI, e.g. enable UI elements such as buttons
                        System.out.println("Game Action: Player " + currentPlayer.getPlayerName() + " in Phase " + currentTurn.getPhase());
                        //btnGameAction.setEnabled(true);
                        nextPhaseButton.setEnabled(true);
                    } else {
                        // It is another player's turn!
                        // Nothing to do; just deactivate UI...
                        System.out.println("Waiting for my turn...");
                        //btnGameAction.setEnabled(false);
                        nextPhaseButton.setEnabled(false);
                    }
                    break;
                case GAME_OVER:
                    //btnGameAction.setEnabled(false);
                    JOptionPane.showMessageDialog(windowJFrame,
                            "Game over. Winner is " + gce.getPlayer().getPlayerName() + ".",
                            "Game Over",
                            JOptionPane.INFORMATION_MESSAGE);
                /*try {
					server.removeGameEventListener(this);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
                    System.exit(0);
                    break;
                default:
            }
        } else if (event instanceof GameActionEvent) {
            GameActionEvent gae = (GameActionEvent) event;
            if (!gae.getPlayer().equals(this.player)) {
                // Event originates from other player and is relevant for me:
                switch (gae.getType()) {
                    case ATTACK:
                        JOptionPane.showMessageDialog(windowJFrame,
                                "You are attacked by player " + gae.getPlayer().getPlayerName() + ".",
                                "Attack!",
                                JOptionPane.WARNING_MESSAGE);
                        break;
                    case NEW_OWNER:
                        JOptionPane.showMessageDialog(windowJFrame,
                                "Some territory has been conquered by player " + gae.getPlayer().getPlayerName() + ".",
                                "UI Update!",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    default:
                }
            } else {
                // Event originates from me and is relevant for me:
                switch (gae.getType()) {
                    case BUY_ITEM:
                        JOptionPane.showMessageDialog(windowJFrame,
                                "Do you want to buy an item, " + gae.getPlayer().getPlayerName() + "?",
                                "Special Offer!!",
                                JOptionPane.QUESTION_MESSAGE);
                        break;
                    default:
                }
            }
        }
    }

}
