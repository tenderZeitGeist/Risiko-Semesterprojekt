package domain;


import valueobjects.Continent;
import valueobjects.Country;
import valueobjects.Player;
import valueobjects.Card;
import domain.exceptions.NoEnemyCountriesNearException;
import domain.exceptions.CountryAlreadyExistsException;
import domain.exceptions.NoAlliedCountriesNearException;
import domain.Persistence.*;

import java.util.*;
import java.io.IOException;


/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 30.03.2017.
 */

public class WorldVerwaltung {

    public int countryCount = 42;
    private PersistenceManager pm = new FilePersistenceManager();
    private Vector<Country> countryList = new Vector<Country>();
    private Vector<Continent> continentList = new Vector<Continent>();
    private Vector<Integer> randomList = new Vector<Integer>();

    private Vector<Country> ownedCountriesList = new Vector<>();
    private Vector<Country> neighbouringCountriesList = new Vector<>();
    private Vector<Country> attackingCountriesList = new Vector<>();
    private Vector<Card> cardList = new Vector<>();


    //TODO this replaces the text file for now!
    private Vector<Country> countryListEurope = new Vector<Country>(); // EUROPE
    private Vector<Country> countryListAsia = new Vector<Country>(); // ASIA
    private Vector<Country> countryListSAmerica = new Vector<Country>(); // SOUTH-AMERICA
    private Vector<Country> countryListNAmerica = new Vector<Country>(); // NORTH-AMERICA
    private Vector<Country> countryListAustralia = new Vector<Country>(); // AUSTRALIA
    private Vector<Country> countryListAfrica = new Vector<Country>(); // AFRICA


    //---------------------------------------------------------------------------------

    /**
     * Reads the Data from a .txt file in to load countries
     *
     * @param file
     * @throws IOException
     */
    public void readData(String file) throws IOException {
        // PersistenzManager für Lesevorgänge öffnen
        pm.openForReading(file);

        Country oneCountry;
        do {
            // Objekt einlesen
            oneCountry = pm.loadCountry();
            if (oneCountry != null) {
                // Buch in Liste einfügen
                try {
                    addCountry(oneCountry);
                } catch (CountryAlreadyExistsException e1) {
                    // Kann hier eigentlich nicht auftreten,
                    // daher auch keine Fehlerbehandlung...
                }
            }
        } while (oneCountry != null);

        // Persistenz-Schnittstelle wieder schließen
        pm.close();
    }

    /**
     * Adds the amount of countries from the .txt into the a countryList
     *
     * @param country
     * @throws CountryAlreadyExistsException
     */
    // TODO Switch case for adding the countries into the correct countrylists
    public void addCountry(Country country) throws CountryAlreadyExistsException {
        // Adds countries to continent countries lists

        switch (country.getContinentID()) {
            case 1:
                countryListNAmerica.add(country);
                break;
            case 2:
                countryListSAmerica.add(country);
                break;
            case 3:
                countryListEurope.add(country);
                break;
            case 4:
                countryListAfrica.add(country);
                break;
            case 5:
                countryListAsia.add(country);
                break;
            case 6:
                countryListAustralia.add(country);
                break;
            default:
                break;
        }

                  /*if ( ! countryList.contains ( country ) )
            countryList.add ( country );
        else
            throw new CountryAlreadyExistsException ( country, " - in 'einfuegen()'" );
    }*/
    }

    public void addCountriesListsToContinentList() {

        continentList.add(new Continent("North America", 5, 1, countryListNAmerica));  //Id 0
        continentList.add(new Continent("South America", 2, 2, countryListSAmerica));  //Id 1
        continentList.add(new Continent("Europe", 5, 3, countryListEurope));           //Id 2
        continentList.add(new Continent("Africa", 3, 4, countryListAfrica));           //Id 3
        continentList.add(new Continent("Asia", 7, 5, countryListAsia));               //Id 4
        continentList.add(new Continent("Australia", 2, 6, countryListAustralia));     //Id 5
    }

    public void getCountryList() {
        for (Country c : countryList) {
            System.out.println(c.getCountryName());
        }
    }

    /**
     * Saves the date into a .txt file
     *
     * @param file
     * @throws IOException
     */
    public void writeData(String file) throws IOException {
        // PersistenzManager für Schreibvorgänge öffnen
        pm.openForWriting(file);


        for (Continent continent : continentList) {
            for (Country country : continent.getContinentCountries()) {
                pm.saveCountry(country);
            }

            pm.close();
        }
    }

    /**
     * Logical part of WorldVerwaltung to retrieve and set information of continents/countries
     */

    public void resetOwnedCountriesList() {
        ownedCountriesList.removeAllElements();
    }

    public void resetAttackingCountriesList() {
        attackingCountriesList.removeAllElements();
    }

    public void resetNeighbouringCountriesList() {
        neighbouringCountriesList.removeAllElements();
    }

    public Country selectNeighbouringCountriesListByNumber(int selectedCountryNumber) {
        return neighbouringCountriesList.get(selectedCountryNumber - 1);
    }

    public Country selectAttackingCountriesListByNumber(int selectedCountryNumber) {
        return attackingCountriesList.get(selectedCountryNumber - 1);
    }

    /**
     * @param attackingCountry
     * @param conqueredCountry
     * @param newForces
     */


    public void conquerCountry(Country attackingCountry, Country conqueredCountry, int newForces) {

        conqueredCountry.setOwningPlayer(attackingCountry.getOwningPlayer());
        moveForces(attackingCountry, conqueredCountry, newForces);

        loadOwnedCountryList(attackingCountry.getOwningPlayer());
    }

    public void moveForces(Country oldCountry, Country newCountry, int forces) {
        newCountry.setLocalForces(forces + newCountry.getLocalForces());
        oldCountry.setLocalForces(oldCountry.getLocalForces() - forces);
    }

    public Country getCountryByID(int countryID) {
        Country c = null;

        for (Continent continent : continentList) {
            for (Country country : continent.getContinentCountries()) {
                if (country.getCountryID() == countryID) {
                    c = country;
                }
            }
        }
        return c;
    }

    public Vector<Continent> getContinentList() {
        return continentList;
    }

    /*public Vector < Country > getCountryList ( ) {
        return countryList;
    }*/

    public void setForcesToCountry(Country country, int forces) {
        int n = country.getLocalForces();
        country.setLocalForces(n + forces);
    }

    public int returnForcesPerRoundsPerPlayer(Player player) {
        int forcesCount = 0;

        if (getNumberOfCountriesOfPlayer(player) < 9) {
            forcesCount += 3;
        } else {
            forcesCount = getNumberOfCountriesOfPlayer(player) / 3;
        }

        //Check if continent is completely occupied and add forces accordingly
        if (isContinentOccupied(player, 1)) {
            forcesCount += continentList.get(0).getValue();
        }
        if (isContinentOccupied(player, 2)) {
            forcesCount += continentList.get(1).getValue();
        }
        if (isContinentOccupied(player, 3)) {
            forcesCount += continentList.get(2).getValue();
        }
        if (isContinentOccupied(player, 4)) {
            forcesCount += continentList.get(3).getValue();
        }
        if (isContinentOccupied(player, 5)) {
            forcesCount += continentList.get(4).getValue();
        }
        if (isContinentOccupied(player, 6)) {
            forcesCount += continentList.get(5).getValue();
        }
        return forcesCount;
    }

    /**
     * Part of the code, mainly for iteration of various queries
     */

    public void distributeCountries(List<Player> playerList) throws ArithmeticException {
        //TODO proper distribution with randomisation etc

        addCountriesListsToContinentList();

        int index = 0;
        Vector<Country> tempCountryList;

        for (Continent continent : continentList) {
            tempCountryList = new Vector<>(continent.getContinentCountries());
            while (!tempCountryList.isEmpty()) {
                if (!(index < playerList.size())) {
                    index = 0;
                }
                int rollCountry = (int) (Math.random() * tempCountryList.size());
                tempCountryList.get(rollCountry).setOwningPlayer(playerList.get(index++));
                tempCountryList.remove(rollCountry);
            }
        }
    }


    public int getNumberOfCountriesOfPlayer(Player player) {
        //checks every Country in every Continent for its owner
        int numberOfCountries = 0;

        for (Continent continent : continentList) {
            for (Country country : continent.getContinentCountries()) {
                if (country.getOwningPlayer().equals(player)) {
                    numberOfCountries++;
                }
            }
        }
        return numberOfCountries;
    }

    //OWNED
    public Vector<Country> loadOwnedCountryList(Player player) {

        resetOwnedCountriesList();
        for (Continent continent : continentList) {
            for (Country country : continent.getContinentCountries()) {
                if (country.getOwningPlayer().equals(player)) {
                    ownedCountriesList.add(country);
                }
            }
        }
        return ownedCountriesList;
    }

    public Vector<Country> loadOwnedCountryListWithMoreThanOneForce(Player player) {
        resetOwnedCountriesList();

        /*Vector<Country> currentCountries = loadOwnedCountryList(player);
        for (Country country : currentCountries) {
            if (!(country.getLocalForces() >= 2))
                currentCountries.remove(country);
        }
        return currentCountries;*/

        for ( int i = 0 ; i < continentList.size ( ) ; i++ ) {
            for ( int j = 0 ; j < continentList.get ( i ).getContinentCountries ( ).size ( ) ; j++ ) {
                if ( continentList.get ( i ).getContinentCountries ( ).get ( j ).getOwningPlayer ( ).equals ( player )
                        && continentList.get ( i ).getContinentCountries ( ).get ( j ).getLocalForces ( ) > 1 ) {

                    ownedCountriesList.add ( continentList.get ( i ).getContinentCountries ( ).get ( j ) );


                }
            }
        }
        return ownedCountriesList;
    }

    public Vector<Country> loadNeighbouringCountriesListForDistributionPhase(Country country) throws NoAlliedCountriesNearException {
        resetNeighbouringCountriesList();

        int[] neighbouringCountriesListIDs = country.getNeighbouringCountries();
        for (int n : neighbouringCountriesListIDs) {
            if (getCountryByID(n).getOwningPlayer().equals(country.getOwningPlayer())) {
                neighbouringCountriesList.add(getCountryByID(n));
            }
        }
        if (neighbouringCountriesList.isEmpty()) {
            throw new NoAlliedCountriesNearException();
        }
        return neighbouringCountriesList;
    }


    //NEIGHBOUR
    public Vector<Country> loadNeighbouringCountryListForAttackingPhase(Country country) throws NoEnemyCountriesNearException {
        resetNeighbouringCountriesList();

        int[] neighbouringCountriesListIDs = country.getNeighbouringCountries();
        /*for (int n : neighbouringCountriesListIDs) {
            if (!(getCountryByID(n).getOwningPlayer()).equals(country.getOwningPlayer())) {
                neighbouringCountriesList.add(getCountryByID(n));
            }
        }*/
        for (int i = 0; i < neighbouringCountriesListIDs.length; i++) {

            if (!getCountryByID(neighbouringCountriesListIDs[i]).getOwningPlayer().equals(country.getOwningPlayer())) {

                neighbouringCountriesList.add(getCountryByID(neighbouringCountriesListIDs[i]));

            }
            if (neighbouringCountriesList.size() == 0) {
                throw new NoEnemyCountriesNearException();
            }
           // return neighbouringCountriesList;

        }
        return neighbouringCountriesList;

    }

    //ATTACKING
    //1
    public Vector<Country> loadAttackingCountriesList(Player player) throws NoEnemyCountriesNearException {
        loadOwnedCountryList(player);
        resetAttackingCountriesList();


        for (int i = 0; i < ownedCountriesList.size(); i++) {

            try {
                loadNeighbouringCountryListForAttackingPhase(ownedCountriesList.get(i));
            } catch (NoEnemyCountriesNearException e) {


            }
            if (ownedCountriesList.get(i).getLocalForces() > 1 && (neighbouringCountriesList.size() > 0)) {
                attackingCountriesList.add(ownedCountriesList.get(i));
            }
        }

        if (attackingCountriesList.size() == 0) {
            throw new NoEnemyCountriesNearException();

        }
        return attackingCountriesList;


    }

    // Checks if the continent ist occupied by the player
    public boolean isContinentOccupied(Player player, int continentNumber) {
        int countryIndex = 0;
        Continent continent = getContinentByID(continentNumber);
        for (Country country : continent.getContinentCountries()) {
            if (country.getOwningPlayer().equals(player))
                countryIndex++;
        }
        // If the countryIndex has the same value as the amount of countries of this continent
        // the continent is completely dominated by the handed over player
        if (countryIndex == continent.getContinentCountries().size()) {
            return true;

        } else {
            return false;
        }
/*
        int continentsSize = continentList.get ( continentNumber ).getContinentCountries ( ).size ( );
        int index = 0;
        int isOccupied = 0;

        for ( int i = 0 ; i < continentsSize ; i++ ) {
            if ( continentList.get ( continentNumber ).getContinentCountries ( ).get ( i ).getOwningPlayer ( ).equals ( player ) ) {
                index++;
            }
        }
*/
    }

    // Iterates the continentList and compares value n with the continentID of each continent
    public Continent getContinentByID(int n) {
        Continent continent = null;
        for (Continent c : continentList) {
            if (c.getContinentID() == n) {
                continent = c;
            }
        }
        return continent;
    }


    //_____________________________________________________________________________________________________________


    //_____________________________________________________________________________________________________________


    public void createGameFile() {
        //TODO ersetzt die text Datei,
        //TODO THIS IS NOT THE FINAL VERSION, PLEASE USE METHOD ABOVE!!

        // Adds NA countries into a list
        /*countryListNAmerica.add(new Country("Alaska", 1, 1, null, 1, new int[]{2, 6, 32}));
        countryListNAmerica.add(new Country("Alberta", 2, 1, null, 1, new int[]{1, 6, 7, 9}));
        countryListNAmerica.add(new Country("Central America", 3, 1, null, 1, new int[]{4, 9, 13}));
        countryListNAmerica.add(new Country("Eastern United States", 4, 1, null, 1, new int[]{3, 7, 8, 9}));
        countryListNAmerica.add(new Country("Greenland", 5, 1, null, 1, new int[]{6, 7, 8, 15}));
        countryListNAmerica.add(new Country("Northwest Territory", 6, 1, null, 1, new int[]{1, 2, 5, 7}));
        countryListNAmerica.add(new Country("Ontario", 7, 1, null, 1, new int[]{2, 6, 4, 5, 7, 9}));
        countryListNAmerica.add(new Country("Quebec", 8, 1, null, 1, new int[]{4, 5, 7}));
        countryListNAmerica.add(new Country("Western United States", 9, 1, null, 1, new int[]{2, 3, 4, 7}));

        // Adds SA countries into a list
        countryListSAmerica.add(new Country("Argentina", 10, 1, null, 2, new int[]{11, 12}));
        countryListSAmerica.add(new Country("Brazil", 11, 1, null, 2, new int[]{10, 12, 13, 25}));
        countryListSAmerica.add(new Country("Peru", 12, 1, null, 2, new int[]{10, 11, 13}));
        countryListSAmerica.add(new Country("Venezuela", 13, 1, null, 2, new int[]{3, 12, 11}));

        // Adds EU countries into a list
        countryListEurope.add(new Country("Great Britain", 14, 1, null, 3, new int[]{15, 17, 16}));
        countryListEurope.add(new Country("Iceland", 15, 1, null, 3, new int[]{14, 16}));
        countryListEurope.add(new Country("Northern Europe", 16, 1, null, 3, new int[]{14, 17, 18, 19, 20}));
        countryListEurope.add(new Country("Scandinavia", 17, 1, null, 3, new int[]{14, 15, 16, 19}));
        countryListEurope.add(new Country("Southern Europe", 18, 1, null, 3, new int[]{16, 19, 20, 23}));
        countryListEurope.add(new Country("Ukraine", 19, 1, null, 3, new int[]{16, 17, 18, 27, 33, 37}));
        countryListEurope.add(new Country("Western Europe", 20, 1, null, 3, new int[]{16, 18, 25}));

        // Adds Africa countries into a list
        countryListAfrica.add(new Country("Congo", 21, 1, null, 4, new int[]{22, 23, 25, 26}));
        countryListAfrica.add(new Country("East Africa", 22, 1, null, 4, new int[]{21, 23, 24, 25, 26, 33}));
        countryListAfrica.add(new Country("Egypt", 23, 1, null, 4, new int[]{18, 22, 25, 33}));
        countryListAfrica.add(new Country("Madagascar", 24, 1, null, 4, new int[]{22, 26}));
        countryListAfrica.add(new Country("North Africa", 25, 1, null, 4, new int[]{20, 21, 22, 23}));
        countryListAfrica.add(new Country("South Africa", 26, 1, null, 4, new int[]{21, 22, 24}));

        // Adds Asia countries into a list
        countryListAsia.add(new Country("Afghanistan", 27, 1, null, 5, new int[]{19, 28, 29, 33, 37}));
        countryListAsia.add(new Country("China", 28, 1, null, 5, new int[]{27, 29, 34, 35, 36, 37}));
        countryListAsia.add(new Country("India", 29, 1, null, 5, new int[]{27, 28, 33, 35}));
        countryListAsia.add(new Country("Irkutsk", 30, 1, null, 5, new int[]{32, 34, 36, 38}));
        countryListAsia.add(new Country("Japan", 31, 1, null, 5, new int[]{32, 34}));
        countryListAsia.add(new Country("Kamchatka", 32, 1, null, 5, new int[]{30, 31, 34, 38}));
        countryListAsia.add(new Country("Middle East", 33, 1, null, 5, new int[]{18, 19, 22, 23, 27, 29}));
        countryListAsia.add(new Country("Mongolia", 34, 1, null, 5, new int[]{28, 30, 31, 32, 36}));
        countryListAsia.add(new Country("Siam", 35, 1, null, 5, new int[]{28, 29, 40}));
        countryListAsia.add(new Country("Siberia", 36, 1, null, 5, new int[]{30, 34, 37, 38}));
        countryListAsia.add(new Country("Ural", 37, 1, null, 5, new int[]{19, 27, 28, 36}));
        countryListAsia.add(new Country("Yakutsk", 38, 1, null, 5, new int[]{30, 32, 36}));

        // Adds AUS countries into a list
        countryListAustralia.add(new Country("Eastern Australia", 39, 1, null, 6, new int[]{41, 42}));
        countryListAustralia.add(new Country("Indonesia", 40, 1, null, 6, new int[]{35, 41, 42}));
        countryListAustralia.add(new Country("New Guinea", 41, 1, null, 6, new int[]{40, 42}));
        countryListAustralia.add(new Country("Western Australia", 42, 1, null, 6, new int[]{39, 41}));

        // Adds all countryLists into a whole continentList
        continentList.add(new Continent("North America", 5, 1, countryListNAmerica));  //Id 0
        continentList.add(new Continent("South America", 2, 2, countryListSAmerica));  //Id 1
        continentList.add(new Continent("Europe", 5, 3, countryListEurope));           //Id 2
        continentList.add(new Continent("Africa", 3, 4, countryListAfrica));           //Id 3
        continentList.add(new Continent("Asia", 7, 5, countryListAsia));               //Id 4
        continentList.add(new Continent("Australia", 2, 6, countryListAustralia));*/     //Id 5

        // Creates and adds als cards into the cardList Vector

        for (int i = 0, index = 1; i < continentList.size(); i++) {
            for (int j = 0; j < continentList.get(i).getContinentCountries().size(); j++) {
                if (index < 15) {
                    cardList.add(new Card(1, index, getCountryByID(index).getCountryName()));
                    index++;
                } else if (index < 29) {
                    cardList.add(new Card(2, index, getCountryByID(index).getCountryName()));
                    index++;
                } else if (index < 43) {
                    cardList.add(new Card(3, index, getCountryByID(index).getCountryName()));
                    index++;
                } else {
                    cardList.add(new Card(4, index, "Joker"));
                    index++;
                }
            }
        }
    }

    //TODO PUT THE LOGIC SHIT INTO THE PLAYGROUND CLASS!!!!!!!
    //--------------------------------------------------------


    public int[] compareDice(int attackerRolls, int defenderRolls) {

        int[] forcesArray = new int[]{0, 0};
        Integer[] attackerDice = new Integer[attackerRolls];
        Integer[] defenderDice = new Integer[defenderRolls];

        for (int i = 0; i < defenderDice.length; i++) {
            defenderDice[i] = rollDice();
        }


        for (int i = 0; i < attackerDice.length; i++) {
            attackerDice[i] = rollDice();
        }

        Arrays.sort(attackerDice, Collections.reverseOrder());
        Arrays.sort(defenderDice, Collections.reverseOrder());

        if (attackerDice.length < defenderDice.length) {
            for (int i = 0; i < attackerDice.length; i++) {
                System.out.println("Attacker rolls " + attackerDice[i + 1] + " with the " + i + " roll"
                        + " while Defender rolls a " + defenderDice[i + 1] + ".");
                if (attackerDice[i] <= defenderDice[i]) {
                    forcesArray[0]++;
                } else {
                    forcesArray[1]++;
                }
            }

        } else {
            for (int k = 0; k < defenderDice.length; k++) {
                System.out.println("Attacker rolls " + attackerDice[k + 1] + " with the " + k + " roll"
                        + " while Defender rolls a " + defenderDice[k + 1] + ".");
                if (attackerDice[k] <= defenderDice[k]) {
                    forcesArray[0]++;
                } else {
                    forcesArray[1]++;
                }
            }
        }
        return forcesArray;
    }


    public int rollDice() {
        return (int) (Math.random() * 6 + 1);
    }

    public void battle(Country attackingCountry, Country defendingCountry, int attackerRolls, int defenderRolls) {
// KLasse BattleResult (C1, C2, W1, W2, Winner, ...)
        //TODO attacker can use more then 3 forces to attack, but only 3 dices(maybe!)
        /*if ( ! ( attackerRolls < attackingCountry.getLocalForces ( ) && attackerRolls < 4 && attackerRolls > 0 ) ) {
            System.out.println ( "- Attacker may use only up to 3 forces, and keep at least 1 on the country he is attacking from" );
            return false;
        }


        if ( ! ( defenderRolls >= 1 && defenderRolls <= 2 ) ) {
            System.out.println ( "- Defender may use up to 2 forces" );
            return false;
        }*/

        int[] forcesLosses = compareDice(attackerRolls, defenderRolls);

        attackingCountry.setLocalForces(attackingCountry.getLocalForces() - forcesLosses[0]);
        System.out.println("Attacking country loses " + forcesLosses[0] + " and has "
                + attackingCountry.getLocalForces() + " forces remaining.");

        defendingCountry.setLocalForces(defendingCountry.getLocalForces() - forcesLosses[1]);
        System.out.println("Defending country loses " + forcesLosses[1] + " and has "
                + defendingCountry.getLocalForces() + " forces remaining.");

       /*
        int defenderLosses = defenderRolls;

        if (attackerRolls < defenderRolls) {
            defenderLosses = attackerRolls;
        }
        if (attackerWins) {
            //attacker wins
            defendingCountry.setLocalForces(defendingCountry.getLocalForces() - defenderLosses);
            System.out.println(attacker.getPlayerName() + " wins!");
        } else if (!attackerWins) {
            //defender Wins

            attackingCountry.setLocalForces(attackingCountry.getLocalForces() - defenderRolls);
            System.out.println(defender.getPlayerName() + " wins!");
        }
*/
        if (defendingCountry.getLocalForces() < 1) {
            //defending Country is conquered
            System.out.println("The defending Country " + defendingCountry.getCountryName() + " has lost all its forces.");
            System.out.println(attackingCountry.getOwningPlayer().getPlayerName() + " is the new owner.");

            conquerCountry(attackingCountry, defendingCountry, attackerRolls);

        }

        //return true;
    }

    public boolean playerWon(Player p, boolean b) {
        //TODO: implement this to also ask if player achieved his goal
        //TODO split Method to fill hte ownedCountrieslist and seperate the printing from the filling

/*        for ( int i = 0 ; i < continentList.size ( ) ; i++ ) {

            for ( int j = 0 ; j < continentList.get ( i ).getContinentCountries ( ).size ( ) ; j++ ) {

                if ( continentList.get ( i ).getContinentCountries ( ).get ( j ).getOwningPlayer ( ).equals ( p ) ) {
                    ownedCountriesList.add ( continentList.get ( i ).getContinentCountries ( ).get ( j ) );
                }

            }

        }*/
        int n = getNumberOfCountriesOfPlayer(p);

        if (n == 42 || b) {
            return true;
        }
        return false;

    }
}


/*  Buffered Reader line einlesen und Strings in Integer Array umwandeln/hinzufügen <-- wichtig für Nachbarländer
    String[] integersInString = br.readLine().split(" ");
    int a[] = new int[integersInString.length];
    for (int i = 0; i < integersInString.length; i++) {
    a[i] = Integer.parseInt(integersInString[i]);*/
