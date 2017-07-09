package ui;

import domain.Risiko;
import domain.exceptions.PlayerAlreadyExistsException;
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
import java.util.Vector;

/**
 * Created by ZeitGeist on 14.06.2017.
 */
public class PlayGroundGUI extends JFrame implements ConnectionDataHandler {
    private Risiko risk;
    private StartDialog startDialog;
    private BufferedImage in;
    private Graphics canvas;
    private BufferedImage fgPicture;
    private JLabel fgPictureLabel = new JLabel();
    private Image redFlag, greenFlag;

    private int gamePhase;
    private Player thisPlayer;
    private JButton nextPhaseButton;
    private JButton saveGameButton;
    private JButton loadGameButton;
    private Vector<Country> disabledCountriesList;
    private Vector<Country> enabledCountriesList;
    private Turn turn;
    int initForces;

    double scalingFactor = 0.5;
    //private String[] connectionData = new String[4];


    @Override
    public void setConnectionData(String[] connectionData) {
        System.out.println("Received connection data");
        for (int i = 0; i < connectionData.length; i++) {
            try {
                risk.createPlayer(i, connectionData[i]);
            } catch (PlayerAlreadyExistsException e) {
                e.printStackTrace();
            }
        }
        this.initGamelogic();
    }

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PlayGroundGUI();
            }
        });
    }


    public PlayGroundGUI() {
        super();
        try {
            startGame(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void startGame(ConnectionDataHandler cDH) throws IOException {
        risk = new Risiko();  // Übergabe von Daten zum Einlesen wurde deaktiviert!!!

        //risk = startDialog.startGameDialog(risk);

        initGameGUI();
        playDialog connectionDialog = new playDialog(cDH); // erwartet in Konstruktor einen ConnectionDataHandler
        connectionDialog.createDialog();
    }

    public void initGameGUI() {
        // Set screen size resolution of the GUI
        // TODO Necessary?


        this.setLayout(new MigLayout(
                "debug",
                "[][]",
                "[][][]"));
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitGameSecurely();
            }
        });


        BufferedImage bgPicture = null;

        try {
            //extremely redundant scaling...

            Toolkit tk = this.getToolkit();
            bgPicture = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/starRiskColorCoded.png"));
            bgPicture = resizeBuffImg(bgPicture, (int) (bgPicture.getWidth() * scalingFactor), (int) (bgPicture.getHeight() * scalingFactor));
            redFlag = ImageIO.read( new File ("./Risiko-Semesterprojekt/src/ui/rescourcen/flag_icons/flag_red.png"));
            //redFlag = resizeBuffImg(redFlag, (int)(redFlag.getWidth() * 0.1), (int)(redFlag.getHeight() * 0.1));
            greenFlag = ImageIO.read( new File ("./Risiko-Semesterprojekt/src/ui/rescourcen/flag_icons/flag_green.png"));
            //greenFlag = resizeBuffImg(greenFlag, (int)(greenFlag.getWidth() * 0.1), (int)(greenFlag.getHeight() * 0.1));

            fgPicture = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/StarRiskBg.png"));
            fgPicture = resizeBuffImg(fgPicture, (int) (fgPicture.getWidth() * scalingFactor), (int) (fgPicture.getHeight() * scalingFactor));


            fgPictureLabel = new JLabel(new ImageIcon(fgPicture));
            //fgPictureLabel.paint();
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
        JTextArea actionPerformedText = new JTextArea("", 6, 20);
        actionPerformedText.setFont(actionPerformedText.getFont().deriveFont(30f));
        actionPerformedText.setEditable(false);
        JScrollPane console = new JScrollPane(actionPerformedText,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JTextAreaOutputStream out = new JTextAreaOutputStream(actionPerformedText);
        System.setOut(new PrintStream(out));
        String workingDir = System.getProperty("user.dir");
        System.out.println("Current working directory : " + workingDir);

        // Creating player list pane for displaying active players
        JTextArea playerListText = new JTextArea("", 10, 20);
        playerListText.setEditable(false);
        JScrollPane playerListPane = new JScrollPane(playerListText,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        gamePanel.add(fgPictureLabel);


        consolePanel.add(console);
        playerPanel.add(playerListPane);

        this.add(gamePanel);
        this.add(buttonPanel, "aligny top, center, wrap");
        this.add(console, "grow");
        this.add(playerPanel);

        buttonPanel.add(nextPhaseButton);
        buttonPanel.add(saveGameButton);
        buttonPanel.add(loadGameButton);

        this.revalidate();
        this.repaint();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);


        //GUI ERSTELLT, LOGIK UND LADEN ERST AB HIER! (doch nicht)
        //_________________________
    }

    public void initGamelogic() {


        nextPhaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                risk.nextPhase();
            }
        });
        nextPhaseButton.setEnabled(false);
        //fgPictureLabel.repaint();


        try {
            risk.readData("Risiko-Semesterprojekt/countryList.txt");
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        for (Country c : risk.getCountryList()) {
            c.setX((int) (c.getX() * scalingFactor*2));
            c.setY((int) (c.getY() * scalingFactor*2));
        }

        // this is meh
        //Collections.shuffle(risk.getPlayerList());
        risk.setPlayerIDs();
        risk.distributeCountries();
        risk.distributeMissions();
        risk.startTurn(risk.getCurrentPlayer());
        roundManager(risk.getCurrentPlayer());
    }

    public void roundManager(Player currentPlayer) {

        switch (risk.getTurn().getPhase()) {
            case DISTRIBUTE:
                initForces = risk.returnForcesPerRoundsPerPlayer(currentPlayer);
                displayCountries(risk.loadOwnedCountryList ( currentPlayer ), risk.loadEnemyCountriesList ( currentPlayer ));

                break;
            case ATTACK:
                nextPhaseButton.setEnabled(true);

                risk.nextPhase();
                break;
            case MOVE:

                risk.nextTurn(currentPlayer);
                risk.setNextPlayer();
                roundManager(risk.getCurrentPlayer());
                break;
        }


    }


    public void createMapClickListener(JLabel fgPanel, BufferedImage bgPicture) {
        fgPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int packetInt = bgPicture.getRGB(e.getX(), e.getY());
                Color color = new Color(packetInt, true);
                //RGB to Hex
                String hex = String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());

                if (!hex.equals("000000")) {
                    System.out.print(hex + "  ");
                    Country c = risk.compareHEX(hex);
                    System.out.println(c.getCountryName() + " " + c.getX() + " " + c.getY() + "  " + c.getOwningPlayer().getPlayerName());
                    int x = e.getX();
                    int y = e.getY();
                } else {
                }
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

    private void exitGameSecurely() {
        //TODO add end game context
        System.exit(this.EXIT_ON_CLOSE);
    }

    public void displayCountries(Vector<Country> ownedCountriesList, Vector<Country> enemyCountriesList) {
        //TODO show green glow(or sth) on countries that belong to you...
        Graphics2D g2d = (Graphics2D) fgPicture.getGraphics();

        for (Country country : ownedCountriesList) {
            System.out.println(country.getCountryName());
            //g2d.setColor(Color.RED);
            //g2d.setStroke(new BasicStroke(10));
            int x = country.getX();
            int y = country.getY();
            //g2d.drawOval(x-10, y-10, 20, 20 );
            g2d.drawImage(greenFlag, (x - 10), (y - 30), this);
        }

        for( Country country : enemyCountriesList ){
            int x = country.getX ();
            int y = country.getY ();
            g2d.drawImage ( redFlag, (x- 10), (y - 30), this);
        }
        g2d.dispose();
        fgPictureLabel.repaint();
    }
}