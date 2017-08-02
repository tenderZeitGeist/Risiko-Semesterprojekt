package Client.ui;

import net.miginfocom.swing.MigLayout;
import Client.ui.customUiElements.ConnectionDataHandler;
import Client.ui.customUiElements.ConnectionDialog;
import valueobjects.Card;
import valueobjects.Continent;
import valueobjects.Mission;
import valueobjects.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Vector;


/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 31.05.2017.
 */
public class PlayGroundGUIPrototype extends JFrame implements ConnectionDataHandler {
    private List<Player> playerList = new Vector<Player>();
    private Vector<Continent> continentList = new Vector<Continent>();
    private Mission currentMission = null;
    private Player currentPlayer = null;
    private Card currentCard = null;

    private Socket socket = null;
    private BufferedReader in;
    private PrintStream out;


    private String connectionIp;
    private int connectionPort;

    private String[] connectionData = new String[4];


    public PlayGroundGUIPrototype() {
        super();
    }


    public static void main(String[] args) {
        PlayGroundGUIPrototype p = new PlayGroundGUIPrototype();
        p.openServerConnection();
        //p.establishConnection(p.currentPlayer.getPlayerName(), p.connectionIp, p.connectionPort);


    }

    public void establishConnection(String title, String host, int port) {
        try {
            socket = new Socket(host, port);

            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());

        } catch (IOException e) {
            /* Fehlerbehandlung, z.B. Socket schließen  */
            System.out.println("can't open socket");
        }

        try {
            String message = in.readLine();
            System.out.println(message);
        } catch (IOException e) { /* Fehlerbehandlung */
            System.out.println("can't handshake with server");
        }
    }


    public void createGame() {
//        JFrame mainWindowFrame = new JFrame("WADDUPPP??!?!?!?");
        this.setSize(1000, 600);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitGameSecurely();
            }
        });
        //- Add stuff to mainWindowFrame
        //-- Add ContentPanel
        JPanel mainWindowPanel = new JPanel(new MigLayout("wrap 2, gap rel 7", "[50][][][]", "[][][][][][]"));
        mainWindowPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(mainWindowPanel);

        //--- Add left side Button Row
        //---- Add picture
        BufferedImage myPicture;
        JLabel playGroundLabel = new JLabel();

        try {
            myPicture = ImageIO.read(new File("./src/ui/rescourcen/StarRiskBG.jpg"));
            playGroundLabel = new JLabel(new ImageIcon(myPicture.getScaledInstance((int) (myPicture.getWidth() * 0.9), (int) (myPicture.getHeight() * 0.9), Image.SCALE_FAST)));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //----
        JButton button1 = new JButton("Start");
        button1.setFont(new Font("Arial", Font.PLAIN, 30));
        JButton button2 = new JButton("button2");
        button2.setFont(new Font("Arial", Font.PLAIN, 30));
        JButton button3 = new JButton("button3");
        button3.setFont(new Font("Arial", Font.PLAIN, 30));
        JButton button4 = new JButton("button4");
        button4.setFont(new Font("Arial", Font.PLAIN, 30));
        JButton button5 = new JButton("button5");
        button5.setFont(new Font("Arial", Font.PLAIN, 30));
        JTextArea actionPerformedText = new JTextArea("actionPerformedText", 10, 20);
        JTextArea placeholderText1 = new JTextArea("placeholderText1", 10, 20);
        JTextArea placeholderText2 = new JTextArea("placeholderText2", 10, 20);
        //---
        //--- Add content to mainWindowPanel
        mainWindowPanel.add(button1, "left, growx, growy, wmin 140, hmin 40");
        mainWindowPanel.add(playGroundLabel, "center, spanx 5, spany 6");
        mainWindowPanel.add(button2, "left, growx, growy, wmin 140, hmin 40");
        mainWindowPanel.add(button3, "left, growx, growy, wmin 140, hmin 40");
        mainWindowPanel.add(button4, "left, growx, growy, wmin 140, hmin 40");
        mainWindowPanel.add(button5, "left, growx, growy, wmin 140, hmin 40");
        mainWindowPanel.add(actionPerformedText, "growx, growy");
        mainWindowPanel.add(placeholderText1, "split2, spanx, growx, growy");
        mainWindowPanel.add(placeholderText2, "growx, growy");
        //---
        //--
        this.setVisible(true);
        this.pack();

        this.setResizable(false);

    }


    private void exitGameSecurely() {
        //TODO add end game context
        this.dispose();
    }


    public boolean openServerConnection() {

        boolean connectionSuccessful = false;
        ConnectionDialog connectionDialog = new ConnectionDialog(this); // erwartet in Konstruktor einen ConnectionDataHandler
        connectionDialog.createDialog();


        return connectionSuccessful;
    }


    /* Interface

     */
    @Override
    public void setConnectionData(String[] connectionData) {
        System.out.println("Received connection data");

        this.connectionData = connectionData;
        currentPlayer = new Player(1, this.connectionData[0]);
        connectionIp = this.connectionData[1];
        connectionPort = Integer.parseInt(this.connectionData[2]);
        createGame();
    }
}
