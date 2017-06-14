package valueobjects;

import valueobjects.Country;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Created by Intersection on 30.03.2017.
 */
public class Continent implements Serializable {

    private String name;
    private int value;
    private Vector<Country> continentCountries;
    private int continentID;

    public Continent(String name, int value, int continentID, Vector<Country> continentCountries) {
        this.name = name;
        this.value = value;
//        this.continentCountries = continentCountries;
        this.continentCountries = continentCountries;
        this.continentID = continentID;
        //this.continentCountries = continentCountries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getContinentID() {
        return continentID;
    }

    public void setContinentID(int continentID) {
        this.continentID = continentID;
    }

    public Vector<Country> getContinentCountries() {
        return continentCountries;
    }

    public void setContinentCountries(Vector<Country> continentCountries) {
        this.continentCountries = continentCountries;
    }
}