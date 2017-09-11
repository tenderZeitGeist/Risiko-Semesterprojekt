package customUiElements;

import net.miginfocom.swing.MigLayout;
import valueobjects.Player;
import valueobjects.customCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 11.09.2017.
 */
public class CardSelectionWindow {

    private Vector<customCard> playerCardList;
    private Player player;
    private int[][] cardArray = {
            {1, 1, 1, 1},
            {2, 2, 2},
            {3, 3},
            {4, 4, 4, 4}
    };

    private Vector<Integer> deletedIdList = new Vector<>();

    private JButton card1TextButton = new JButton();
    private JButton card2TextButton = new JButton();
    private JButton card3TextButton = new JButton();
    private JButton card4TextButton = new JButton();

    JButton card1Button = new JButton("use C-3PO");
    JButton card2Button = new JButton("use Jar Jar Binks");
    JButton card3Button = new JButton("use R2-D2");
    JButton card4Button = new JButton("use Gandalf");

    public Vector<Integer> CardSelectionWindow(Vector<customCard> playerCardList, Player p) {
        this.playerCardList = playerCardList;
        this.player = p;


        cardArray = null;
        //if (playerCardList != null)
        cardArray = setCardArray(playerCardList);

        JFrame parent = new JFrame();
        parent.setTitle("Select Cards!");
        parent.setLayout(new MigLayout(
                "",
                "[][]",
                "[][]"
        ));
        // create elements

        //buttons
        JPanel buttonPanel = new JPanel(new MigLayout("", "[]", "[]"));
        Font f1 = new Font("Arial", Font.PLAIN, 30);

        card1Button.setFont(f1);
        card2Button.setFont(f1);
        card3Button.setFont(f1);
        card4Button.setFont(f1);
        card1Button.setEnabled(false);
        card2Button.setEnabled(false);
        card3Button.setEnabled(false);
        card4Button.setEnabled(false);
        buttonPanel.add(card1Button, "wrap, grow");
        buttonPanel.add(card2Button, "wrap, grow");
        buttonPanel.add(card3Button, "wrap, grow");
        buttonPanel.add(card4Button, "wrap, grow");

        //textPanel
        JPanel textArea = new JPanel(new MigLayout("", "[]", "[]"));
        card1TextButton.setFont(f1);
        card1TextButton.setEnabled(true);
        card1TextButton.setOpaque(false);
        card1TextButton.setContentAreaFilled(false);
        card1TextButton.setBorderPainted(false);
        card1TextButton.setFocusable(false);
        card1TextButton.setEnabled(false);

        card2TextButton.setFont(f1);
        card2TextButton.setEnabled(true);
        card2TextButton.setOpaque(false);
        card2TextButton.setContentAreaFilled(false);
        card2TextButton.setBorderPainted(false);
        card2TextButton.setFocusable(false);
        card2TextButton.setEnabled(false);

        card3TextButton.setFont(f1);
        card3TextButton.setEnabled(true);
        card3TextButton.setOpaque(false);
        card3TextButton.setContentAreaFilled(false);
        card3TextButton.setBorderPainted(false);
        card3TextButton.setFocusable(false);
        card3TextButton.setEnabled(false);

        card4TextButton.setFont(f1);
        card4TextButton.setEnabled(true);
        card4TextButton.setOpaque(false);
        card4TextButton.setContentAreaFilled(false);
        card4TextButton.setBorderPainted(false);
        card4TextButton.setFocusable(false);
        card4TextButton.setEnabled(false);

        textArea.add(card1TextButton, "wrap, grow");
        textArea.add(card2TextButton, "wrap, grow");
        textArea.add(card3TextButton, "wrap, grow");
        textArea.add(card4TextButton, "wrap, grow");

        setButtonText();

        // Add elements to frame
        JButton confirmButton = new JButton("confirm");
        confirmButton.setFont(f1);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);

            }
        });

        buttonPanel.add(confirmButton, "grow");


        parent.add(buttonPanel, "aligny top");
        parent.add(textArea, "aligny top, wrap");

        // End of add elements to frame
        parent.setLocationRelativeTo(null);
        parent.pack();
        parent.setVisible(true);
        parent.setResizable(false);


        card1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                removeFromCardList(1, 3);
                initButtonLogic();
                setButtonText();

            }
        });
        card2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeFromCardList(2, 3);
                initButtonLogic();
                setButtonText();

            }
        });
        card3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeFromCardList(3, 3);
                initButtonLogic();
                setButtonText();

            }
        });
        card4Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeFromCardList(4, 3);
                initButtonLogic();
                setButtonText();
            }
        });

        //logic
        initButtonLogic();
        System.out.println(cardArray[0][0]);

        return deletedIdList;
    }

    public static void main(String[] args) {
        CardSelectionWindow w = new CardSelectionWindow();
        w.CardSelectionWindow(null, null);
    }

    public int[][] setCardArray(Vector<customCard> playerCardList) {
        int[][] tcardArray = null;
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        if (playerCardList != null) {
            for (customCard c : playerCardList) {
                switch (c.getCardType()) {
                    case 1:
                        tcardArray[1][i1] = c.getCardID();
                        i1++;
                        break;
                    case 2:
                        tcardArray[2][i2] = c.getCardID();
                        i2++;
                        break;
                    case 3:
                        tcardArray[3][i3] = c.getCardID();
                        i3++;
                        break;
                    case 4:
                        tcardArray[4][i4] = c.getCardID();
                        i4++;
                        break;
                }
            }

            return tcardArray;
        } else {
            return new int[][]{
                    {2, 18, 20, 5, 10, 20,},
                    {3, 15, 12},
                    {3, 4},
                    {4, 4, 4, 4}
            };
        }

    }


    public void initButtonLogic() {
        if (cardArray[0].length >= 3) {
            card1Button.setEnabled(true);
        } else {
            card1Button.setEnabled(false);
        }
        if (cardArray[1].length >= 3) {
            card2Button.setEnabled(true);
        } else {
            card2Button.setEnabled(false);
        }
        if (cardArray[2].length >= 3) {
            card3Button.setEnabled(true);
        } else {
            card3Button.setEnabled(false);
        }
        if (cardArray[3].length >= 3) {
            card4Button.setEnabled(true);
        } else {
            card4Button.setEnabled(false);
        }

    }


    public void removeFromCardList(int index, int count) {
        index -= 1;
        ArrayList<Integer> intList = new ArrayList<>();
        for (int ix = 0; ix < cardArray[index].length; ix++) {
            intList.add(cardArray[index][ix]);
        }

        for (int i = 0; i < count; i++) {
            deletedIdList.add(intList.get(0));
            intList.remove(intList.get(0));
        }
        intList.trimToSize();

        cardArray[index] = new int[intList.size()];

        int newIndex = 0;
        for (int c : intList) {
            cardArray[index][newIndex] = c;
            newIndex++;
        }

    }

    public void setButtonText() {
        card1TextButton.setText("  " + (cardArray[0].length) + "  ");
        card2TextButton.setText("  " + (cardArray[1].length) + "  ");
        card3TextButton.setText("  " + (cardArray[2].length) + "  ");
        card4TextButton.setText("  " + (cardArray[3].length) + "  ");

    }


}
