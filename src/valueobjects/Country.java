package valueobjects;

import java.io.Serializable;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 23.03.2017.
 */
public class Country implements Serializable{
    private int localForces;
    private String countryName;
    private Player owningPlayer;
    private String countryNameShort;
    private int countryID;
    private int continentID;
    private int[] neighbouringCountriesByID;


    public Country(String countryName, int countryID, int localForces, Player owningPlayer, int continentID, int[] neighbouringCountriesByID) {
        this.localForces = localForces;
        this.countryName = countryName;
        this.owningPlayer = owningPlayer;
        this.countryID = countryID;
        this.continentID = continentID;
        this.neighbouringCountriesByID = neighbouringCountriesByID;

    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getLocalForces() {
        return localForces;
    }

    public void setLocalForces(int localForces) {
        this.localForces = localForces;
    }

    public Player getOwningPlayer() {
        return owningPlayer;
    }

    public void setOwningPlayer(Player owningPlayer) {
        this.owningPlayer = owningPlayer;
    }

    public String getCountryNameShort() {
        return countryNameShort;
    }

    public void setCountryNameShort(String countryNameShort) {
        this.countryNameShort = countryNameShort;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public int getContinentID() {
        return continentID;
    }

    public void setContinentID(int continentID) {
        this.continentID = continentID;
    }

    public int[] getNeighbouringCountries() {
        return neighbouringCountriesByID;
    }

    public String getOwningPlayerName(){
        return owningPlayer.getPlayerName ( );
    }

}
