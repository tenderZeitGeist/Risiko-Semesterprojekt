package valueobjects;

import java.io.Serializable;

/**
 * Created by Intersection on 27.04.2017.
 */
public class Card implements Serializable {
    private int cardID;
    private int cardType;
    private String cardName;
    private Player owningPlayer;

    public Card(int cardID, int cardType, String cardName) {
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