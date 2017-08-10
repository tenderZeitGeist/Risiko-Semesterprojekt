
//

import customUiElements.JTextAreaOutputStream;
import events.GameActionEvent;
import events.GameControlEvent;
import events.GameEvent;
import exceptions.PlayerAlreadyExistsException;
import net.miginfocom.swing.MigLayout;
import valueobjects.Country;
import valueobjects.Player;
import valueobjects.Turn;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static events.GameControlEvent.GameControlEventType.GAME_STARTED;
import static events.GameControlEvent.GameControlEventType.NEXT_TURN;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 17.07.2017.
 */
public class RiskGUI extends UnicastRemoteObject implements GameEventListener {

    private static final long serialVersionUID = -1337133713371337L;

    //Value Objects
    private Player player;
    //this should be replaced with server later
    private RemoteRisk risiko;


    //UI
    private JFrame windowJFrame;
    private JButton startGameButton;
    private JButton nextPhaseButton;
    private JButton loadGameButton;
    private JButton saveGameButton;
    private JLabel fgPictureLabel = new JLabel();
    private JTextArea statusTextArea;
    private JPanel gamePane;
    private JPanel statusPanel;
    private JPanel glass;
    private JPanel buttonPanel;
    private JTextArea customSysout;
    private JScrollPane console;
    private boolean admin = false;

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
            new RiskGUI();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RiskGUI() throws IOException {
        try {
            String serviceName = "RiskServer";
            Registry registry = LocateRegistry.getRegistry(1337);
            risiko = (RemoteRisk) registry.lookup(serviceName);



        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        risiko.addGameEventListener(this);

        //ScalingSliderDialog sc = new ScalingSliderDialog();
        //scalingFactor = sc.createScalingSliderDialog() / 100;
        scalingFactor = 0.7;
        System.out.println(scalingFactor);
        createPlayerDialog();
        //init Pictures
        initPictureFiles();


        for (Country c : risiko.getCountryList()) {
            c.setX((int) (c.getX() * scalingFactor));
            c.setY((int) (c.getY() * scalingFactor));
        }
        initMainWindow();


    }


    public void createPlayerDialog() throws RemoteException {
        try {
            //IDEA: we use the code as is, but ask the player to
            //enter the name of his "alliance" (separatists, empire, rebels)


            String name = JOptionPane.showInputDialog(windowJFrame, "Enter your alliances name:", "add alliance", JOptionPane.QUESTION_MESSAGE);

            player = new Player(0, name);
            if (risiko.createPlayer(player.getPlayerID(), player.getPlayerName())) {
                admin = true;
            }

        } catch (PlayerAlreadyExistsException e) {
            e.printStackTrace();
            createPlayerDialog();
        }
    }

    public void initMainWindow() throws RemoteException {
//Create Main windowJFrame
        windowJFrame = new JFrame("Star Risk: " + player.getPlayerName() + "    [ALPHA VERSION NOT FOR PUBLIC]");
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
        gamePane = new JPanel();
        gamePane.add(fgPictureLabel);
        gamePane.setBackground(Color.BLACK);
        gamePane.setForeground(Color.WHITE);

        // Glass Panel
        Dimension glassPaneSize = fgPictureLabel.getPreferredSize();
        glass = (JPanel) windowJFrame.getGlassPane();
        glass.setSize(glassPaneSize);
        glass.setVisible(true);
        glass.setLayout(null);


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
        windowJFrame.repaint();
        windowJFrame.pack();
        windowJFrame.setLocationRelativeTo(null);
        windowJFrame.setVisible(true);

        //this is some cool init stuffz:)
        paintFlagLabel();
        String workingDir = System.getProperty("user.dir");
        System.out.println("Current working directory : " + workingDir);

    }

    //Init functions
    public void initPictureFiles() {
        try {
            //extremely redundant scaling...
            bgPicture = ImageIO.read(RiskGUI.class.getResourceAsStream("/starRiskColorCoded.png"));
            bgPicture = resizeBuffImg(bgPicture, (int) ((bgPicture.getWidth() * 0.5) * scalingFactor), (int) ((bgPicture.getHeight() * 0.5) * scalingFactor));


            redFlag = ImageIO.read(RiskGUI.class.getResourceAsStream("/flag_icons/flag_red.png"));
            redFlag = redFlag.getScaledInstance((int) (60 * scalingFactor), (int) (60 * scalingFactor), 100);
            greenFlag = ImageIO.read(RiskGUI.class.getResourceAsStream("/flag_icons/flag_green.png"));
            greenFlag = greenFlag.getScaledInstance((int) (60 * scalingFactor), (int) (60 * scalingFactor), 100);

            fgPictureFix = ImageIO.read(RiskGUI.class.getResourceAsStream("/StarRiskBg.png"));
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
        startGameButton = new JButton("Start Game");
        saveGameButton = new JButton("Save Game");
        loadGameButton = new JButton("Load Game");
        nextPhaseButton.setFont(new Font("Arial", Font.PLAIN, (int) (30 * scalingFactor)));
        startGameButton.setFont(new Font("Arial", Font.PLAIN, (int) (30 * scalingFactor)));
        saveGameButton.setFont(new Font("Arial", Font.PLAIN, (int) (30 * scalingFactor)));
        loadGameButton.setFont(new Font("Arial", Font.PLAIN, (int) (30 * scalingFactor)));
        buttonPanel.add(nextPhaseButton, "wrap");
        buttonPanel.add(startGameButton, "wrap");
        buttonPanel.add(saveGameButton, "wrap");
        buttonPanel.add(loadGameButton, "wrap");
        if (admin) {
            startGameButton.setEnabled(true);
        } else {
            startGameButton.setEnabled(false);
        }
        nextPhaseButton.setEnabled(false);
        saveGameButton.setEnabled(false);
        loadGameButton.setEnabled(true);

        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    risiko.nextPhase();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });


        nextPhaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    risiko.nextPhase();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });

        saveGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        loadGameButton.addActionListener(new ActionListener() {
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

    public void createMouseHoverListener(JLabel fgPanel, final BufferedImage bgPicturex) {
        fgPanel.addMouseMotionListener(new MouseAdapter() {

            public void mouseMoved(MouseEvent e) {
                int packetInt = bgPicturex.getRGB(e.getX(), e.getY());
                Color color = new Color(packetInt, false);
                //RGB to Hex
                String hex = String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());

                if (!hex.equals("000000")) {
                    if (!hovered) {
                        Country tempSelectedCountry = null;
                        try {
                            tempSelectedCountry = risiko.compareHEX(hex);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
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

    public void paintFlagLabel() throws RemoteException {
        for (Country currentCountry : risiko.getCountryList()) {
            JLabel flag = new JLabel();
            int x = currentCountry.getX();
            int y = currentCountry.getY();

            if (currentCountry.getOwningPlayer().equals(player)) {
                flag.setIcon(new ImageIcon(greenFlag));
            } else {
                flag.setIcon(new ImageIcon(redFlag));
            }
            Dimension size = flag.getPreferredSize();
            flag.setBounds(x - ((int) (6 * scalingFactor)), y - ((int) (6 * scalingFactor)), size.width, size.height);
            glass.add(flag);
        }

        windowJFrame.repaint();

    }


    /**
     * END OF GUI!!!
     * FROM NOW ON ALL THE LOGIX
     * (dixhuxen)
     */

    public void roundManger() {
    }


    /**
     * WE DONT KNOW ANY OF THIS STUFF, ALL FROM TESCHKE
     * @param event
     * @throws RemoteException
     */
    public void handleGameEvent(GameEvent event) throws RemoteException{
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
