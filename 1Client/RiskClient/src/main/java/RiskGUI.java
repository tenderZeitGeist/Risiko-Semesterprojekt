import customUiElements.CardSelectionWindow;
import customUiElements.JTextAreaOutputStream;
import customUiElements.ScalingSliderDialog;
import events.GameActionEvent;
import events.GameControlEvent;
import events.GameEvent;
import exceptions.CountryAlreadyExistsException;
import exceptions.NoAlliedCountriesNearException;
import exceptions.NoEnemyCountriesNearException;
import exceptions.PlayerAlreadyExistsException;
import net.miginfocom.swing.MigLayout;
import valueobjects.Country;
import valueobjects.Player;
import valueobjects.Turn;
import valueobjects.customCard;

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
import java.util.Vector;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 17.07.2017.
 */
public class RiskGUI extends UnicastRemoteObject implements GameEventListener {

    private static final long serialVersionUID = -1337133713371337L;

    //Value Objects
    private Player player;
    //this should be replaced with server later
    private RemoteRisk risiko;

    private Turn.Phase currentPhase = null;
    private int forcesLeft = 0;


    private int bonusForces = 0;

    //UI
    private JFrame windowJFrame;
    private JButton startGameButton;
    private JButton nextPhaseButton;
    private JButton loadGameButton;
    private JButton saveGameButton;
    private JLabel fgPictureLabel = new JLabel();
    private JTextArea planetTextArea;
    private JPanel gamePane;
    private JPanel planetPanel;

    private JPanel statusPanel;
    private JTextArea statusPanelTextArea;
    private JLabel statusPanelImage;

    private JPanel glass;
    private JPanel buttonPanel;
    private JTextArea customSysout;
    private JScrollPane console;

    //Objects
    private BufferedImage bgPicture;
    private BufferedImage fgPicture;
    private BufferedImage fgPictureFix;
    private Image redFlag, greenFlag, yellowFlag, blueFlag, purpleFlag, whiteflag;
    private Image redFlag2, greenFlag2, yellowFlag2, blueFlag2, purpleFlag2, whiteflag2;
    private Image playerIcon = null;
    private Image playerIconHighlight = null;
    private MouseListener mcl = null;
    private MouseMotionListener mml = null;
    private Country tempCountry1 = null;

    //vars
    private Double scalingFactor;
    private boolean hovered = false;
    private boolean isClicked = false;
    private boolean loadedGame = false;
    private boolean admin = false;


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
            risiko.addGameEventListener(this);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        ScalingSliderDialog sc = new ScalingSliderDialog();
        scalingFactor = sc.createScalingSliderDialog() / 100;
        //scalingFactor = 0.7;
        System.out.println(scalingFactor);

        if (risiko.getPlayerList().size() < 7) {
            createPlayerDialog();
            //init Pictures
            initPictureFiles();
            initMainWindow();
        } else {
            JOptionPane.showMessageDialog(null, "You can't join this game. There are already 6 players.");
        }
    }


    public void createPlayerDialog() throws RemoteException {
        try {
            //IDEA: we use the code as is, but ask the player to
            //enter the name of his "alliance" (separatists, empire, rebels)
            String name = JOptionPane.showInputDialog(windowJFrame, "Enter the name of your alliance:",
                    "add alliance",
                    JOptionPane.QUESTION_MESSAGE);
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
        windowJFrame = new JFrame("Star Risk: " + player.getPlayerName() + "  [ALPHA VERSION NOT FOR PUBLIC]");
        //windowJFrame.setBounds(1000, 1000, 1000, 1000);

        windowJFrame.setLayout(new MigLayout(
                "",
                "[][]",
                "[][][][][]"));
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

        //Add those Elements to main Fram
        windowJFrame.add(gamePane, "span 1 4");
        windowJFrame.add(planetPanel, "wrap");
        windowJFrame.add(buttonPanel, "aligny top, wrap");
        windowJFrame.add(statusPanel, "aligny, top, wrap");
        windowJFrame.add(statusPanelImage, "aligny top, wrap");
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
        //paintFlagLabel();
        //String workingDir = System.getProperty("user.dir");
        //System.out.println("Current working directory : " + workingDir);

    }

    //Init functions
    public void initPictureFiles() {
        try {
            //extremely redundant scaling...
            bgPicture = ImageIO.read(RiskGUI.class.getResourceAsStream("/starRiskColorCoded.png"));
            bgPicture = resizeBuffImg(bgPicture, (int) ((bgPicture.getWidth() * 0.5) * scalingFactor), (int) ((bgPicture.getHeight() * 0.5) * scalingFactor));

            redFlag = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_red.png"));
            redFlag = redFlag.getScaledInstance((int) (60 * scalingFactor), (int) (60 * scalingFactor), 100);
            greenFlag = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_green.png"));
            greenFlag = greenFlag.getScaledInstance((int) (60 * scalingFactor), (int) (60 * scalingFactor), 100);
            purpleFlag = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_purple.png"));
            purpleFlag = purpleFlag.getScaledInstance((int) (60 * scalingFactor), (int) (60 * scalingFactor), 100);
            yellowFlag = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_yellow.png"));
            yellowFlag = yellowFlag.getScaledInstance((int) (60 * scalingFactor), (int) (60 * scalingFactor), 100);
            blueFlag = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_blue.png"));
            blueFlag = blueFlag.getScaledInstance((int) (60 * scalingFactor), (int) (60 * scalingFactor), 100);
            whiteflag = ImageIO.read(RiskGUI.class.getResourceAsStream("star_wars_flag_icons/flag_white.png"));
            whiteflag = whiteflag.getScaledInstance((int) (60 * scalingFactor), (int) (60 * scalingFactor), 100);


            blueFlag2 = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_blue_green.png"));
            blueFlag2 = blueFlag2.getScaledInstance((int) (60 * scalingFactor), (int) (60 * scalingFactor), 100);
            yellowFlag2 = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_yellow_green.png"));
            yellowFlag2 = yellowFlag2.getScaledInstance((int) (60 * scalingFactor), (int) (60 * scalingFactor), 100);
            purpleFlag2 = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_purple_green.png"));
            purpleFlag2 = purpleFlag2.getScaledInstance((int) (60 * scalingFactor), (int) (60 * scalingFactor), 100);
            greenFlag2 = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_green_green.png"));
            greenFlag2 = greenFlag2.getScaledInstance((int) (60 * scalingFactor), (int) (60 * scalingFactor), 100);
            redFlag2 = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_red_green.png"));
            redFlag2 = redFlag2.getScaledInstance((int) (60 * scalingFactor), (int) (60 * scalingFactor), 100);
            whiteflag2 = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_white_green.png"));
            whiteflag2 = whiteflag2.getScaledInstance((int) (60 * scalingFactor), (int) (60 * scalingFactor), 100);

            fgPictureFix = ImageIO.read(RiskGUI.class.getResourceAsStream("/StarRiskBg.png"));
            fgPictureFix = resizeBuffImg(fgPictureFix, (int) ((fgPictureFix.getWidth() * 0.5) * scalingFactor), (int) ((fgPictureFix.getHeight() * 0.5) * scalingFactor));
            fgPicture = fgPictureFix;
            fgPictureLabel = new JLabel(new ImageIcon(fgPicture));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void initStatusPanel() {
        planetPanel = new JPanel();
        planetTextArea = new JTextArea("Star: none \nMarines: none \nAlliance: none", 3, 18);
        planetPanel.add(planetTextArea, "growx growy");
        int scale = (int) (40 * scalingFactor);
        Font f = new Font("Arial", 0, scale);
        planetPanel.setForeground(Color.WHITE);
        planetPanel.setBackground(Color.BLACK);
        planetTextArea.setFont(f);
        planetTextArea.setForeground(Color.WHITE);
        planetTextArea.setBackground(Color.BLACK);
        planetTextArea.setEditable(false);

        statusPanel = new JPanel();
        statusPanelImage = new JLabel();

        statusPanelTextArea = new JTextArea("" +
                "Alliance:   none \n" +
                "Marines:   none \n" +
                "Phase:   none \n" +
                "Planets:   none \n" +
                "Cards: none \n" +
                "Mission: none \n" +
                "", 3, 18);
        statusPanelTextArea.setLineWrap(true);
        statusPanel.add(statusPanelTextArea);

        scale = (int) (32 * scalingFactor);
        f = new Font("Arial", 0, scale);
        statusPanel.setForeground(Color.WHITE);
        statusPanel.setBackground(Color.BLACK);
        statusPanelTextArea.setFont(f);
        statusPanelTextArea.setForeground(Color.WHITE);
        statusPanelTextArea.setBackground(Color.BLACK);
        statusPanelTextArea.setEditable(false);


        //flagPanel.add();


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
        buttonPanel.add(nextPhaseButton, "grow, wrap");
        buttonPanel.add(startGameButton, "grow, wrap");
        buttonPanel.add(saveGameButton, "grow, wrap");
        buttonPanel.add(loadGameButton, "grow, wrap");
        if (admin) {
            startGameButton.setEnabled(true);
            loadGameButton.setEnabled(true);
            windowJFrame.setTitle(windowJFrame.getTitle() + " ADMIN ");
        } else {
            startGameButton.setEnabled(false);
            loadGameButton.setEnabled(false);
        }

        nextPhaseButton.setEnabled(false);
        saveGameButton.setEnabled(false);


        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!loadedGame) {
                        risiko.distributeCountries();
                        risiko.distributeMissions();
                        risiko.setPlayerIDs();
                        risiko.startGame();
                    } else {
                        risiko.startGame();
                    }
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });

        nextPhaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    risiko.nextTurn();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });

        saveGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    risiko.saveGame(player);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        loadGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    risiko.loadGame();
                } catch (IOException | ClassNotFoundException | CountryAlreadyExistsException e1) {
                    e1.printStackTrace();
                }
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

    public void createMouseClickListener(JLabel fgPanel, final BufferedImage bgPicturex) {

        fgPanel.addMouseListener(mcl = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int packetInt = bgPicturex.getRGB(e.getX(), e.getY());
                Color color = new Color(packetInt, false);

                String hex = String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());

                Country tempSelectedCountry = null;
                try {
                    tempSelectedCountry = risiko.compareHEX(hex);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }

                if (!hex.equals("000000") || !isClicked) {
                    switch (currentPhase) {
                        case DISTRIBUTE:
                            updateStatusPanel();
                            if (tempSelectedCountry != null && tempSelectedCountry.getOwningPlayer().equals(player)) {
                                isClicked = true;
                            } else {
                                isClicked = false;
                            }
                            break;
                        case ATTACK:
                            isClicked = false;
                            glass.removeAll();
                            updateStatusPanel();
                            try {
                                if (tempCountry1 == null) {
                                    for (Country currentCountry : risiko.loadAttackingCountriesList(player)) {
                                        if (currentCountry.getCountryName().equals(tempSelectedCountry.getCountryName())) {
                                            Vector<Country> cV = new Vector<>();
                                            cV.add(tempSelectedCountry);
                                            paintFlagLabel(cV, playerIconHighlight);
                                            isClicked = true;
                                        }
                                    }
                                    for (Country c2 : risiko.loadNeighbouringCountriesListForAttackingPhase(tempSelectedCountry)) {
                                        if (!(c2.getOwningPlayer().getPlayerName().equals(player.getPlayerName()))) {
                                            Vector<Country> tempCountryList2 = new Vector<>();
                                            tempCountryList2.add(c2);
                                            paintFlagLabel(tempCountryList2, getPlayerColor(c2.getOwningPlayer().getPlayerID()));
                                        }
                                    }


                                } else {
                                    for (int i : tempCountry1.getNeighbouringCountries()) {
                                        if (i == tempSelectedCountry.getCountryID()) {
                                            if (tempCountry1.getOwningPlayerName().equals(player.getPlayerName())
                                                    && !tempSelectedCountry.getOwningPlayer().equals(player)) {
                                                isClicked = true;
                                                //System.out.println("Player made a correct selection.");
                                            } else {
                                                isClicked = false;
                                                tempCountry1 = null;

                                            }
                                        }
                                    }
                                }
                            } catch (NoEnemyCountriesNearException | RemoteException e1) {
                                e1.printStackTrace();
                            }

                            break;
                        case REDISTRIBUTE:
                            glass.removeAll();
                            updateStatusPanel();
                            isClicked = false;
                            try {
                                if (tempCountry1 == null) {
                                    for (Country currentCountry : risiko.loadOwnedCountryListWithMoreThanOneForce(player)) {
                                        if (currentCountry.getCountryName().equals(tempSelectedCountry.getCountryName())) {
                                            Vector<Country> countryVector = new Vector<>();
                                            countryVector.add(tempSelectedCountry);
                                            glass.removeAll();
                                            paintFlagLabel(risiko.loadNeighbouringCountriesListForDistributionPhase(tempSelectedCountry), playerIconHighlight);
                                            paintFlagLabel(countryVector, playerIconHighlight);
                                            isClicked = true;
                                        }
                                    }

                                } else {
                                    for (int i : tempCountry1.getNeighbouringCountries()) {
                                        if (i == tempSelectedCountry.getCountryID()) {
                                            if (tempSelectedCountry.getOwningPlayerName().equals(player.getPlayerName())
                                                    && tempCountry1.getOwningPlayerName().equals(player.getPlayerName())) {
                                                isClicked = true;
                                                //System.out.println("Player made a correct selection!");
                                            } else {
                                                isClicked = false;
                                                tempSelectedCountry = null;
                                                //System.out.println("Wrong selection!");
                                            }
                                        }
                                    }
                                }
                            } catch (NoAlliedCountriesNearException | RemoteException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case SAVE:
                            break;
                        default:
                            break;
                    }
                } else if (hex.equals("000000") || isClicked) {
                    switch (currentPhase) {
                        case DISTRIBUTE:

                            break;
                        case ATTACK:
                            glass.removeAll();
                            try {
                                paintFlagLabel(excludeCountryListShit(risiko.loadAttackingCountriesList(player), risiko.loadOwnedCountryList(player)), playerIcon);
                                paintFlagLabel(risiko.loadAttackingCountriesList(player), playerIconHighlight);
                            } catch (RemoteException | NoEnemyCountriesNearException e1) {
                                e1.printStackTrace();
                            }
                            hovered = false;
                            isClicked = false;
                            tempCountry1 = null;
                            break;
                        case REDISTRIBUTE:
                            glass.removeAll();
                            try {
                                paintFlagLabel(risiko.loadOwnedCountryListWithMoreThanOneForce(player), playerIconHighlight);
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }
                            isClicked = false;
                            hovered = false;
                            tempCountry1 = null;
                            break;
                        case SAVE:
                            break;

                        default:
                            break;
                    }
                }
                if (isClicked) {
                    hovered = true;
                    try {
                        countryClicked(tempSelectedCountry);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();

                    }
                }
            }
        });
    }

    public void createMouseHoverListener(JLabel fgPanel, final BufferedImage bgPicturex) {

        fgPanel.addMouseMotionListener(mml = new MouseAdapter() {

            public void mouseMoved(MouseEvent e) {
                updateStatusPanel();
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
                            planetTextArea.setText("Star: " + tempSelectedCountry.getCountryName()
                                    + "\nMarines: " + tempSelectedCountry.getLocalForces()
                                    + "\nAlliance: " + tempSelectedCountry.getOwningPlayer().getPlayerName() + "");

                            switch (currentPhase) {
                                case DISTRIBUTE:
                                    updateStatusPanel();
                                    break;
                                case ATTACK:
                                    if (!isClicked) {
                                        try {
                                            for (Country c : risiko.loadAttackingCountriesList(player)) {
                                                if (c.getCountryName().equals(tempSelectedCountry.getCountryName())) {
                                                    if (tempSelectedCountry.getOwningPlayer().equals(player)) {
                                                        Vector<Country> countryVector = new Vector<>();
                                                        countryVector.add(tempSelectedCountry);
                                                        glass.removeAll();

                                                        paintFlagLabel(countryVector, playerIconHighlight);
                                                    }
                                                    for (Country c2 : risiko.loadNeighbouringCountriesListForAttackingPhase(tempSelectedCountry)) {
                                                        if (!(c2.getOwningPlayer().getPlayerName().equals(player.getPlayerName()))) {
                                                            Vector<Country> tempCountryList2 = new Vector<>();
                                                            tempCountryList2.add(c2);
                                                            paintFlagLabel(tempCountryList2, getPlayerColor(c2.getOwningPlayer().getPlayerID()));
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (NoEnemyCountriesNearException | RemoteException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    break;

                                case REDISTRIBUTE:
                                    if (!isClicked) {
                                        try {
                                            for (Country currentCountry : risiko.loadOwnedCountryListWithMoreThanOneForce(player)) {
                                                if (currentCountry.getCountryName().equals(tempSelectedCountry.getCountryName())) {
                                                    Vector<Country> countryVector = new Vector<>();
                                                    countryVector.add(tempSelectedCountry);
                                                    glass.removeAll();
                                                    paintFlagLabel(risiko.loadNeighbouringCountriesListForDistributionPhase(tempSelectedCountry), playerIcon);
                                                    paintFlagLabel(countryVector, playerIconHighlight);
                                                }
                                            }
                                        } catch (NoAlliedCountriesNearException | RemoteException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    break;
                                case SAVE:
                                    break;
                                default:
                                    break;
                            }
                            hovered = true;
                        }
                    }
                } else if (hex.equals("000000")) {
                    planetTextArea.setText("Star: none \nMarines: none \nAlliance: none");
                    switch (currentPhase) {
                        case DISTRIBUTE:
                            break;
                        case ATTACK:
                            if (!isClicked) {
                                try {
                                    glass.removeAll();
                                    paintFlagLabel(excludeCountryListShit(risiko.loadAttackingCountriesList(player), risiko.loadOwnedCountryList(player)), playerIcon);
                                    paintFlagLabel(risiko.loadAttackingCountriesList(player), playerIconHighlight);
                                    paintEnemyCountries();

                                } catch (RemoteException | NoEnemyCountriesNearException e1) {

                                }
                            } else {

                            }
                            break;
                        case REDISTRIBUTE:
                            glass.removeAll();
                            if (!isClicked)
                                try {
                                    paintFlagLabel(risiko.loadOwnedCountryListWithMoreThanOneForce(player), playerIcon);
                                } catch (RemoteException e1) {
                                    e1.printStackTrace();
                                }
                            break;
                        case SAVE:

                            break;
                        default:
                            break;
                    }
                    hovered = false;
                }
            }
        });
    }


    public void paintFlagLabel(Vector<Country> countriesToPaint, Image tmpImage) throws RemoteException {
        ImageIcon tmpIcon = new ImageIcon(tmpImage);

        for (Country currentCountry : countriesToPaint) {
            JLabel flag = new JLabel();
            int x = (int) (currentCountry.getX() * scalingFactor);
            int y = (int) (currentCountry.getY() * scalingFactor);

            flag.setIcon(tmpIcon);

            Dimension size = flag.getPreferredSize();
            flag.setBounds(x - ((int) (17 * scalingFactor)), y - ((int) (16 * scalingFactor)), size.width, size.height);
            glass.add(flag);
        }
        windowJFrame.repaint();
    }


    /**
     * END OF GUI!!!
     * FROM NOW ON ALL THE LOGIX
     * (dixhuxen)
     */


    /**
     * WE DONT KNOW ANY OF THIS STUFF, ALL FROM TESCHKE
     *
     * @param event
     * @throws RemoteException
     */
    public void handleGameEvent(GameEvent event) throws RemoteException {
        if (event instanceof GameControlEvent) {
            GameControlEvent gce = (GameControlEvent) event;
            switch (gce.getType()) {
                case GAME_STARTED:

                    loadGameButton.setEnabled(false);
                    startGameButton.setEnabled(false);
                    System.out.println("The game has just begun... It's player " + gce.getPlayer().getPlayerName() + "'s turn.");

                    for (Player p : risiko.getPlayerList()) {
                        if (p.getPlayerName().equals(player.getPlayerName())) {
                            player.setPlayerID(p.getPlayerID());
                        }
                    }
                    playerIcon = getPlayerColor(player.getPlayerID());
                    playerIconHighlight = getPlayerColor((player.getPlayerID()) + 6);
                    ImageIcon icon = new ImageIcon(playerIcon);


                    try {
                        statusPanelImage.setIcon(setFactionSymbol(playerIcon));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    System.out.println("> Mission: " + risiko.getMissionPerPlayer(player).getDescription());

                case NEXT_TURN:
                    checkCards();
                    forcesLeft = risiko.returnForcesPerRoundsPerPlayer(player);
                    forcesLeft += bonusForces;

                    Turn currentTurn = gce.getTurn();
                    Player currentPlayer = gce.getPlayer();
                    System.out.println("> Player " + currentPlayer.getPlayerName() + " in Phase " + currentTurn.getPhase());
                    if (currentPlayer.equals(player)) {
                        currentPhase = currentTurn.getPhase();
                        phaseHandler();
                        updateStatusPanel();
                        //nextPhaseButton.setEnabled(true);
                    } else {
                        saveGameButton.setEnabled(false);
                        nextPhaseButton.setEnabled(false);
                    }
                    break;
                case GAME_LOADED:

                    loadGameButton.setEnabled(false);
                    loadedGame = true;
                    for (Player p : risiko.getPlayerList()) {
                        if (p.getPlayerName().equals(player.getPlayerName())) {
                            player = p;
                            playerIcon = getPlayerColor(player.getPlayerID());
                            playerIconHighlight = getPlayerColor((player.getPlayerID()) + 6);

                            try {
                                statusPanelImage.setIcon(setFactionSymbol(playerIcon));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ;
                            JOptionPane.showMessageDialog(windowJFrame,
                                    "A previous instance of Star Risk has been successfully loaded.\n" +
                                            "Prepare to resume your last game.",
                                    "Loaded game.",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    break;

                case GAME_SAVED:
                    saveGameButton.setEnabled(false);
                    int i = JOptionPane.showConfirmDialog(windowJFrame,
                            "The current game was saved.\n" +
                                    "Do you want to quit the game?",
                            "Game saved.",
                            JOptionPane.YES_NO_OPTION);
                    if (i == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                    break;

                case GAME_OVER:
                    updateStatusPanel();
                    JOptionPane.showMessageDialog(windowJFrame,
                            "Game over. Winner is " + gce.getPlayer().getPlayerName() + ".",
                            "Game Over",
                            JOptionPane.INFORMATION_MESSAGE);

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
                        updateStatusPanel();
                        JOptionPane.showMessageDialog(windowJFrame,
                                gae.getPlayer().getPlayerName() + " attacked a country",
                                "Attack!",
                                JOptionPane.WARNING_MESSAGE);
                        break;
                    case NEW_OWNER:
                        updateStatusPanel();
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

    public void phaseHandler() throws RemoteException {
        switch (currentPhase) {
            case DISTRIBUTE:
                glass.removeAll();
                System.out.println("> You have " + forcesLeft + " forces to distribute this round");
                paintFlagLabel(risiko.loadOwnedCountryList(player), playerIcon);
                paintEnemyCountries();
                createMouseClickListener(fgPictureLabel, bgPicture);
                createMouseHoverListener(fgPictureLabel, bgPicture);

                break;
            case ATTACK:
                glass.removeAll();
                windowJFrame.repaint();
                isClicked = false;
                try {
                    paintFlagLabel(excludeCountryListShit(risiko.loadAttackingCountriesList(player), risiko.loadOwnedCountryList(player)), playerIcon);
                    paintFlagLabel(risiko.loadAttackingCountriesList(player), playerIconHighlight);
                    paintEnemyCountries();
                } catch (NoEnemyCountriesNearException e) {
                    e.printStackTrace();
                }
                break;
            case REDISTRIBUTE:
                glass.removeAll();
                windowJFrame.repaint();
                isClicked = false;
                try {
                    paintFlagLabel(risiko.loadOwnedCountryListWithMoreThanOneForce(player), playerIcon);
                    paintEnemyCountries();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                break;
            case SAVE:
                bonusForces = 0;
                saveGameButton.setEnabled(true);
                glass.removeAll();
                windowJFrame.repaint();
                System.out.println("> You can save if you want! \n Otherwise press nextPhase");
                fgPictureLabel.removeMouseListener(mcl);
                fgPictureLabel.removeMouseMotionListener(mml);
                break;
        }
    }

    public void deleteMouseClickListener(JLabel fgPanel, final BufferedImage bgPicturex) {
        fgPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
    }

    /**
     * executed when u clicked a country that belongs to you
     *
     * @param country
     */
    public void countryClicked(Country country) throws RemoteException {
        boolean t = false;

        switch (currentPhase) {
            case DISTRIBUTE:

                    int forcesSet = Integer.parseInt(JOptionPane.showInputDialog(windowJFrame,
                            "How many forces do you want to set?\n"
                                    + "You have " + forcesLeft + " forces.",
                            "Set forces!",
                            JOptionPane.OK_CANCEL_OPTION));

                        if (forcesSet <= forcesLeft && forcesSet > 0) {
                            forcesLeft -= forcesSet;
                            risiko.setForcesToCountry(country, country.getLocalForces() + forcesSet);

                            System.out.println("> You have " + forcesLeft + " forces left this round");
                            if (forcesLeft == 0) {
                                nextPhaseButton.setEnabled(true);
                            }

                        } else {
                            JOptionPane.showMessageDialog(windowJFrame,
                                    "You did not enter a valid value",
                                    "Wrong amount!",
                                    JOptionPane.WARNING_MESSAGE);
                        }

                isClicked = false;
                break;

            case ATTACK:

                if (tempCountry1 == null) {
                    tempCountry1 = country;
                } else {
                    boolean isConquered = false;
                    int attackingForces = Integer.parseInt(JOptionPane.showInputDialog(windowJFrame,
                            "How many forces do you want to use for the attack?\n" +
                                    country.getCountryName() + " has " + country.getLocalForces() + " forces\n" +
                                    "You can use a total of " + (tempCountry1.getLocalForces() - 1) + " forces.\n" +
                                    "But you may only select up to 3 forces per roll.",
                            "Attack Phase",
                            JOptionPane.WARNING_MESSAGE));
                    if (!(attackingForces < 1) && !(attackingForces > 3) && attackingForces < tempCountry1.getLocalForces()) {
                        isConquered = risiko.battle(tempCountry1, country, attackingForces);
                    } else {
                        JOptionPane.showMessageDialog(windowJFrame,
                                "You did not enter a valid value",
                                "Wrong amount!",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    if (isConquered) {
                        while (!t) {

                            Country attackingCountry = risiko.getCountryByID(tempCountry1.getCountryID());
                            Country defendingCountry = risiko.getCountryByID(country.getCountryID());
                            int redistributeForces = Integer.parseInt(JOptionPane.showInputDialog(windowJFrame,
                                    "How many forces do you want to move from " + attackingCountry.getCountryName() + " to " + defendingCountry.getCountryName() + "?\n"
                                            + attackingCountry.getCountryName() + " has " + attackingCountry.getLocalForces() + " forces. "
                                            + "You can move " + (attackingCountry.getLocalForces() - 1) + " in total.",
                                    "Move Forces",
                                    JOptionPane.QUESTION_MESSAGE));

                            if ((redistributeForces > (attackingCountry.getLocalForces() - 1)) || (redistributeForces < 0)) {
                                JOptionPane.showMessageDialog(windowJFrame,
                                        "You did not enter a valid value",
                                        "Wrong amount!",
                                        JOptionPane.WARNING_MESSAGE);
                            } else {
                                t = true;
                                risiko.setForcesToCountry(attackingCountry, attackingCountry.getLocalForces() - redistributeForces);
                                risiko.setForcesToCountry(defendingCountry, defendingCountry.getLocalForces() + redistributeForces);
                            }
                        }
                    }
                    tempCountry1 = null;
                    isClicked = false;
                }
                break;

            case REDISTRIBUTE:
                if (tempCountry1 == null) {
                    tempCountry1 = country;
                } else {
                    int forces = (tempCountry1.getLocalForces() - 1);
                    int redistributeForces = Integer.parseInt(JOptionPane.showInputDialog(windowJFrame,
                            "How many forces do you want to set?\n"
                                    + "You have " + forces + " forces.",
                            "Set forces!",
                            JOptionPane.OK_CANCEL_OPTION));
                    if (redistributeForces > forces || !(redistributeForces >= 0)) {
                        JOptionPane.showMessageDialog(windowJFrame,
                                "You did not enter a valid value",
                                "Wrong amount!",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        risiko.setForcesToCountry(tempCountry1, tempCountry1.getLocalForces() - redistributeForces);
                        risiko.setForcesToCountry(country, country.getLocalForces() + redistributeForces);
                    }
                    tempCountry1 = null;
                    isClicked = false;
                }
                break;
        }
    }

    @Override
    public void broadcast(String broadcastText) {
        System.out.println("> " + broadcastText);
    }


    public Image getPlayerColor(int playerID) {
        switch (playerID) {
            case 0:
                return purpleFlag;
            case 1:
                return greenFlag;
            case 2:
                return blueFlag;
            case 3:
                return yellowFlag;
            case 4:
                return redFlag;
            case 5:
                return whiteflag;

            case 6:
                return purpleFlag2;
            case 7:
                return greenFlag2;
            case 8:
                return blueFlag2;
            case 9:
                return yellowFlag2;
            case 10:
                return redFlag2;
            case 11:
                return whiteflag2;
        }
        return null;

    }

    public void updateStatusPanel() {
        try {
            statusPanelTextArea.setText("" +
                    "Alliance:\t" + player.getPlayerName() + " \n" +
                    "Marines:\t" + forcesLeft + " \n" +
                    "Phase:\t" + currentPhase.toString().toLowerCase() + " \n" +
                    "Planets:\t" + risiko.loadOwnedCountryList(player).size() + " \n" +
                    "Cards:\t"+ risiko.getPlayersCardList(player).size() + "\n" +
                    "Mission: \n" + risiko.getMissionPerPlayer(player).getDescription() +
                    "");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public Vector<Country> excludeCountryListShit(Vector<Country> removeThisVector, Vector<Country> keepThisVector) {
        for (Country c2 : removeThisVector) {
            keepThisVector.removeIf(country -> country.getCountryName().equals(c2.getCountryName()));
        }
        return keepThisVector;
    }

    public void paintEnemyCountries() throws RemoteException {
        Vector<Country> tempCountryList = risiko.getCountryList();

        for (Country c : tempCountryList) {
            if (!(c.getOwningPlayer().getPlayerName().equals(player.getPlayerName()))) {
                Vector<Country> tempCountryList2 = new Vector<>();
                tempCountryList2.add(c);
                paintFlagLabel(tempCountryList2, getPlayerColor(c.getOwningPlayer().getPlayerID()));

            }
        }
    }


    public ImageIcon setFactionSymbol(Image playerIcon) throws IOException {
        Image tmpflag = null;
        if (playerIcon.equals(redFlag)) {
            tmpflag = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_red.png"));
        } else if (playerIcon.equals(greenFlag)) {
            tmpflag = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_green.png"));
        } else if (playerIcon.equals(purpleFlag)) {
            tmpflag = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_purple.png"));
        } else if (playerIcon.equals(yellowFlag)) {
            tmpflag = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_yellow.png"));
        } else if (playerIcon.equals(whiteflag)) {
            tmpflag = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_white.png"));
        } else if (playerIcon.equals(blueFlag)) {
            tmpflag = ImageIO.read(RiskGUI.class.getResourceAsStream("/star_wars_flag_icons/flag_blue.png"));
        }

        tmpflag = tmpflag.getScaledInstance((int) (300 * scalingFactor), (int) (300 * scalingFactor), 100);
        return new ImageIcon(tmpflag);
    }


    public void checkCards() throws RemoteException {
        Vector<customCard> ownedCards = risiko.getPlayersCardList(player);
        Vector<customCard> deletedCards = null;
        if (ownedCards.size() > 3) {
            CardSelectionWindow csw = new CardSelectionWindow();
            Vector<Integer> selectedCardsList = csw.CardSelectionWindow(ownedCards, player);
            bonusForces += selectedCardsList.size() / 3;
        }
    }
}
