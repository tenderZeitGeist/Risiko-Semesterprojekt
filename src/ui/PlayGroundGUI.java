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
    private Vector<Country> disabledCountriesList;
    private Vector<Country> enabledCountriesList;
    private Turn turn;
    int initForces;

    public static void main(String[] args) throws IOException {
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


    public void startGame() throws IOException {
        //risk = new Risiko();  // Übergabe von Daten zum Einlesen wurde deaktiviert!!!
        startDialog = new StartDialog();
        risk = startDialog.startGameDialog(risk);

        initGameGUI();
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

        JLabel fgPictureLabel = new JLabel();
        BufferedImage fgPicture;
        BufferedImage bgPicture = null;

        try {
            //extremely redundant scaling...
            bgPicture = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/starRiskColorCoded.png"));
            bgPicture = resizeBuffImg(bgPicture, (int) (bgPicture.getWidth() * 0.4), (int) (bgPicture.getHeight() * 0.4));

            fgPicture = ImageIO.read(new File("./Risiko-Semesterprojekt/src/ui/rescourcen/StarRiskBg.png"));
            fgPicture = resizeBuffImg(fgPicture, (int) (fgPicture.getWidth() * 0.4), (int) (fgPicture.getHeight() * 0.4));


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


        //GUI ERSTELLT, LOGIK UND LADEN ERST AB HIER!

        nextPhaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                risk.nextPhase();
            }
        });
        nextPhaseButton.setEnabled(false);


        System.setOut(new PrintStream(out));
        String workingDir = System.getProperty("user.dir");
        System.out.println("Current working directory : " + workingDir);


        try {
            risk.readData("Risiko-Semesterprojekt/countryList.txt");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

//        Collections.shuffle(risk.getPlayerList());
//        risk.setPlayerIDs();
//        risk.distributeCountries();
//        risk.distributeMissions();
//        risk.startTurn(risk.getCurrentPlayer());
        //roundManager(risk.getCurrentPlayer());

        //_________________________
    }

    public void roundManager(Player currentPlayer) {

        switch (risk.getTurn().getPhase()) {
            case DISTRIBUTE:
                Vector<Country> ownedCountriesList = risk.loadOwnedCountryList(currentPlayer);
                initForces = risk.returnForcesPerRoundsPerPlayer(currentPlayer);
                displayCountries(ownedCountriesList);

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
                    System.out.println(c.getCountryName() + " " + c.getX() + " " + c.getY());
                    int x = e.getX();
                    int y = e.getY();

                    //printCoords
                    Graphics2D g = (Graphics2D) fgPictureLabel.getGraphics();
                    g.setColor(Color.red);
                    g.setStroke(new BasicStroke(3));
                    g.drawOval(x - 10, y - 10, 20, 20);
                    g.dispose();

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

    public void displayCountries(Vector<Country> ownedCountriesList) {
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