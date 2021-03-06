package valueobjects;

import java.io.Serializable;
import java.rmi.Remote;

/**
 * Created by Intersection on 27.04.2017.
 */
public class customCard implements Serializable, Remote {

    private static final long serialVersionUID = -875464645568L;

    private int cardID;
    private int cardType;
    private String cardName;
    private Player owningPlayer;

    public customCard(int cardID, int cardType, String cardName) {
        this.cardID = cardID;
        this.cardType = cardType;
        this.cardName = cardName;
        this.owningPlayer = null;
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Player getOwningPlayer() {
        return owningPlayer;
    }

    public void setOwningPlayer(Player owningPlayer) {
        this.owningPlayer = owningPlayer;
    }
}