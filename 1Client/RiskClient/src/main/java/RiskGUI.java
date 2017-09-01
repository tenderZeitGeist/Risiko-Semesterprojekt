import customUiElements.JTextAreaOutputStream;
import customUiElements.ScalingSliderDialog;
import events.GameActionEvent;
import events.GameControlEvent;
import events.GameEvent;
import exceptions.NoAlliedCountriesNearException;
import exceptions.NoEnemyCountriesNearException;
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

    private MouseMotionListener mml = null;
    private boolean isClicked = false;

    private Country tempCountry1 = null, TempCountry2 = null;


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
        createPlayerDialog();
        //init Pictures
        initPictureFiles();
        initMainWindow();
    }


    public void createPlayerDialog() throws RemoteException {
        try {
            //IDEA: we use the code as is, but ask the player to
            //enter the name of his "alliance" (separatists, empire, rebels)


            String name = JOptionPane.showInputDialog(windowJFrame, "Enter your alliances name:",
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
        //paintFlagLabel();
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
            windowJFrame.setTitle(windowJFrame.getTitle() + " ADMIN ");
        } else {
            startGameButton.setEnabled(false);
        }
        nextPhaseButton.setEnabled(false);
        saveGameButton.setEnabled(false);
        loadGameButton.setEnabled(false);

        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    risiko.distributeCountries();
                    risiko.startGame();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }

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
                    risiko.nextTurn();
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
        fgPanel.addMouseListener(new MouseAdapter() {
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
                            if (tempSelectedCountry != null) {
                                isClicked = true;
                            }
                            break;
                        case ATTACK:
                            isClicked = false;
                            glass.removeAll();

                            try {
                                if (tempCountry1 == null) {

                                    for (Country currentCountry : risiko.loadAttackingCountriesList(player)) {
                                        if (currentCountry.getCountryName().equals(tempSelectedCountry.getCountryName())) {
                                            Vector<Country> cV = new Vector<>();
                                            cV.add(tempSelectedCountry);
                                            paintFlagLabel(risiko.loadNeighbouringCountriesListForAttackingPhase(tempSelectedCountry), "red");
                                            paintFlagLabel(cV, "green");
                                            isClicked = true;
                                        }
                                    }


                                } else {
                                    for (int i : tempCountry1.getNeighbouringCountries()) {
                                        if (i == tempSelectedCountry.getCountryID()) {
                                            if (tempCountry1.getOwningPlayerName().equals(player.getPlayerName())
                                                    && !tempSelectedCountry.getOwningPlayer().equals(player)) {
                                                isClicked = true;
                                                System.out.println("Player made a correct selection.");
                                            } else {
                                                isClicked = false;
                                                tempCountry1 = null;
                                                System.out.println("Deleting tempCountry1.");
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
                            isClicked = false;
                            try {
                                if (tempCountry1 == null) {
                                    for (Country currentCountry : risiko.loadOwnedCountryListWithMoreThanOneForce(player)) {
                                        if (currentCountry.getCountryName().equals(tempSelectedCountry.getCountryName())) {
                                            Vector<Country> countryVector = new Vector<>();
                                            countryVector.add(tempSelectedCountry);
                                            glass.removeAll();
                                            paintFlagLabel(risiko.loadNeighbouringCountriesListForDistributionPhase(tempSelectedCountry), "green");
                                            paintFlagLabel(countryVector, "red");
                                            isClicked = true;
                                        }
                                    }

                                } else {
                                    for (int i : tempCountry1.getNeighbouringCountries()) {
                                        if (i == tempSelectedCountry.getCountryID()) {
                                            if (tempSelectedCountry.getOwningPlayerName().equals(player.getPlayerName())
                                                    && tempCountry1.getOwningPlayerName().equals(player.getPlayerName())) {
                                                isClicked = true;
                                                System.out.println("Player made a correct selection!");
                                            } else {
                                                isClicked = false;
                                                tempSelectedCountry = null;
                                                System.out.println("Wrong selection!");
                                            }
                                        }
                                    }
                                }
                            } catch (NoAlliedCountriesNearException | RemoteException e1) {
                                e1.printStackTrace();
                            }
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
                                paintFlagLabel(risiko.loadAttackingCountriesList(player), "green");
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
                                paintFlagLabel(risiko.loadOwnedCountryListWithMoreThanOneForce(player), "green");
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }
                            isClicked = false;
                            hovered = false;
                            tempCountry1 = null;
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
        fgPanel.removeMouseMotionListener(mml);
        mml = new MouseAdapter() {

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
                            statusTextArea.setText("Star: " + tempSelectedCountry.getCountryName()
                                    + "\nMarines: " + tempSelectedCountry.getLocalForces()
                                    + "\nAlliance: " + tempSelectedCountry.getOwningPlayer().getPlayerName() + "");

                            switch (currentPhase) {
                                case DISTRIBUTE:
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
                                                        paintFlagLabel(risiko.loadNeighbouringCountriesListForAttackingPhase(tempSelectedCountry), "red");
                                                        paintFlagLabel(countryVector, "green");
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
                                                    paintFlagLabel(risiko.loadNeighbouringCountriesListForDistributionPhase(tempSelectedCountry), "green");
                                                    paintFlagLabel(countryVector, "red");
                                                }
                                            }
                                        } catch (NoAlliedCountriesNearException | RemoteException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    break;

                                default:
                                    break;
                            }
                            hovered = true;
                        }
                    }
                } else if (hex.equals("000000")) {
                    statusTextArea.setText("Star: none \nMarines: none \nAlliance: none");
                    switch (currentPhase) {
                        case DISTRIBUTE:
                            break;
                        case ATTACK:
                            if (!isClicked) {
                                try {
                                    glass.removeAll();
                                    paintFlagLabel(risiko.loadAttackingCountriesList(player), "green");
                                } catch (RemoteException | NoEnemyCountriesNearException e1) {

                                }
                            } else {

                            }
                            break;
                        case REDISTRIBUTE:
                            glass.removeAll();
                            if (!isClicked)
                                try {
                                    paintFlagLabel(risiko.loadOwnedCountryListWithMoreThanOneForce(player), "green");
                                } catch (RemoteException e1) {
                                    e1.printStackTrace();
                                }
                            break;
                        default:
                            break;
                    }
                    hovered = false;
                }
            }
        };

        fgPanel.addMouseMotionListener(mml);
    }


    public void paintFlagLabel(Vector<Country> countriesToPaint, String color) throws RemoteException {

        for (Country currentCountry : countriesToPaint) {
            JLabel flag = new JLabel();
            int x = (int) (currentCountry.getX() * scalingFactor);
            int y = (int) (currentCountry.getY() * scalingFactor);

            switch (color) {
                case "red":
                    flag.setIcon(new ImageIcon(redFlag));
                    break;
                case "green":
                    flag.setIcon(new ImageIcon(greenFlag));
                    break;
                case "blue":
                    break;
                case "yellow":
                    break;
                case "purple":
                    break;
                default:
                    break;
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
     *
     * @param event
     * @throws RemoteException
     */
    public void handleGameEvent(GameEvent event) throws RemoteException {
        if (event instanceof GameControlEvent) {
            GameControlEvent gce = (GameControlEvent) event;
            switch (gce.getType()) {
                case GAME_STARTED:
                    createMouseHoverListener(fgPictureLabel, bgPicture);
                    startGameButton.setEnabled(false);
                    System.out.println("GO GO GO MOTHERFUCKER");
                    System.out.println("The game has just begun... It's player " + gce.getPlayer().getPlayerName() + "'s turn.");

                case NEXT_TURN:

                    forcesLeft = risiko.returnForcesPerRoundsPerPlayer(player);
                    Turn currentTurn = gce.getTurn();
                    Player currentPlayer = gce.getPlayer();
                    if (currentPlayer.equals(player)) {
                        System.out.println("Player " + currentPlayer.getPlayerName() + " in Phase " + currentTurn.getPhase());


                        currentPhase = currentTurn.getPhase();
                        phaseHandler();

                        //nextPhaseButton.setEnabled(true);
                    } else {

                        System.out.println("Still not my turner");

                        nextPhaseButton.setEnabled(false);
                    }
                    break;
                case GAME_OVER:
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
                        JOptionPane.showMessageDialog(windowJFrame,
                                "You are were attacked by player " + gae.getPlayer().getPlayerName() + ".",
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

    public void phaseHandler() throws RemoteException {
        switch (currentPhase) {
            case DISTRIBUTE:
                glass.removeAll();
                System.out.println("you have " + forcesLeft + " forces left this round");
                paintFlagLabel(risiko.loadOwnedCountryList(player), "green");
                createMouseClickListener(fgPictureLabel, bgPicture);

                break;
            case ATTACK:
                glass.removeAll();
                windowJFrame.repaint();
                isClicked = false;
                try {
                    paintFlagLabel(risiko.loadAttackingCountriesList(player), "green");
                } catch (NoEnemyCountriesNearException e) {

                }

                break;
            case REDISTRIBUTE:
                glass.removeAll();
                windowJFrame.repaint();
                isClicked = false;
                try {
                    paintFlagLabel(risiko.loadOwnedCountryListWithMoreThanOneForce(player), "green");
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }

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
                while (!t) {
                    int forcesSet = Integer.parseInt(JOptionPane.showInputDialog(windowJFrame,
                            "How many forces do you want to set?\n"
                                    + "You have " + forcesLeft + " forces.",
                            "Set forces!",
                            JOptionPane.OK_CANCEL_OPTION));
                    if (forcesSet <= forcesLeft && forcesSet > 0) {
                        forcesLeft -= forcesSet;
                        risiko.setForcesToCountry(country, country.getLocalForces() + forcesSet);

                        t = true;
                        System.out.println("You have " + forcesLeft + " forces left this round");
                        if (forcesLeft == 0) {
                            nextPhaseButton.setEnabled(true);
                        }

                    } else {
                        JOptionPane.showMessageDialog(windowJFrame,
                                "You did not enter a valid value",
                                "Wrong amount!",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
                break;

            case ATTACK:

                if (tempCountry1 == null) {
                    tempCountry1 = country;
                } else {
                    boolean isConquered = false;
                    int attackingForces = Integer.parseInt(JOptionPane.showInputDialog(windowJFrame,
                            "How many forces do you want to use for the attack?\n" +
                                    country.getCountryName() + " has " + country.getLocalForces() + " forces\n" +
                                    "You can use a total " + (tempCountry1.getLocalForces() - 1) + " forces.\n" +
                                    "But you may only select up to 3 forces per roll.",
                            "!",
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
                                            + "You can move about " + (attackingCountry.getLocalForces() - 1) + ".",
                                    "!",
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
        System.out.println(broadcastText);
    }

}
