package domain;

import domain.Persistence.FilePersistenceManager;
import domain.Persistence.PersistenceManager;
import exceptions.CountryAlreadyExistsException;
import exceptions.NoAlliedCountriesNearException;
import exceptions.NoEnemyCountriesNearException;
import valueobjects.Continent;
import valueobjects.Country;
import valueobjects.Player;
import valueobjects.customCard;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;


/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 30.03.2017.
 */

public class WorldVerwaltung {

    public int countryCount = 42;
    private PersistenceManager pm = new FilePersistenceManager();
    private Vector<Continent> continentList = new Vector<Continent>();
    private Vector<customCard> cardList = new Vector<>();

    private Vector<Country> ownedCountriesList = new Vector<>();
    private Vector<Country> neighbouringCountriesList = new Vector<>();
    private Vector<Country> attackingCountriesList = new Vector<>();


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
            // Reads in every country, line by line, from the given file
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
        // Adding the read in countries into the continentList
        addCountriesListsToContinentList();

        customCard oneCard;
        do {
            oneCard = pm.loadCard();
            if (oneCard != null) {
                cardList.add(oneCard);
            }

        } while (oneCard != null);
        pm.close();

        // Persistenz-Schnittstelle wieder schließen
        //Closes the interface to persistence
        pm.close();
    }

    public void serializeCountries() throws IOException {

        try (FileOutputStream fos = new FileOutputStream("countries.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Continent c : continentList) {
                for (Country con : c.getContinentCountries())
                    oos.writeObject(con);
            }
        }
    }

    public void deSerializeCountries() throws IOException, ClassNotFoundException, CountryAlreadyExistsException {

        eraseAllCountriesAndContinents();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("countries.ser"))) {
            while (true) {
                Country c = (Country) ois.readObject();
                //Following line stays the same
                addCountry(c);
            }

        } catch (EOFException e) {
            System.out.println(e.getMessage());
            //Following line stays the same
        }
        addCountriesListsToContinentList();
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

    public void eraseAllCountriesAndContinents() {
        for (Continent currentContinent : continentList) {
            currentContinent.getContinentCountries().removeAllElements();
            currentContinent.getContinentCountries().trimToSize();
        }
        continentList.removeAllElements();
        continentList.trimToSize();
    }

    public void checkCountriesForPlayer(Vector<Player> playerList) throws IllegalStateException {

        for (Player p : playerList) {
            for (Continent currentContinent : continentList) {
                for (Country currentCountry : currentContinent.getContinentCountries())
                    if (p.getPlayerName().equals(currentCountry.getOwningPlayerName())) {
                        currentCountry.setOwningPlayer(p);
                    } else {
                        throw new IllegalStateException();
                    }
            }
        }
    }

    public void addCountriesListsToContinentList() {

        continentList.add(new Continent("Left Top", 1, 1, countryListNAmerica));  //Id 0
        continentList.add(new Continent("Left Bottom", 1, 2, countryListSAmerica));  //Id 1
        continentList.add(new Continent("Right Top", 1, 3, countryListEurope));           //Id 2
        continentList.add(new Continent("Right Bottom", 1, 4, countryListAfrica));           //Id 3
        continentList.add(new Continent("Center", 1, 5, countryListAsia));               //Id 4
        continentList.add(new Continent("Australia", 1, 6, countryListAustralia));     //Id 5
    }


    public void resetOwnedCountriesList() {
        ownedCountriesList.removeAllElements();
    }

    public void resetAttackingCountriesList() {
        attackingCountriesList.removeAllElements();
    }

    public void resetNeighbouringCountriesList() {
        neighbouringCountriesList.removeAllElements();
    }

    /*public Country selectNeighbouringCountriesListByNumber ( int selectedCountryNumber ) {
        return neighbouringCountriesList.get ( selectedCountryNumber - 1 );
    }

    public Country selectAttackingCountriesListByNumber ( int selectedCountryNumber ) {
        return attackingCountriesList.get ( selectedCountryNumber - 1 );
    }*/


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
                    return c = country;
                }
            }
        }
        return c;
    }

    public Vector<Continent> getContinentList() {
        return continentList;
    }

    public void setForcesToCountry(Country country, int forces) {
        for (Continent currentContinent : continentList) {
            for (Country currentCountry : currentContinent.getContinentCountries()) {
                if (country.getCountryID() == currentCountry.getCountryID()) {
                    currentCountry.setLocalForces(forces);
                }
            }
        }
    }

    public void setOwnerToCountry(Country country, Player player) {
        for (Continent currentContinent : continentList) {
            for (Country currentCountry : currentContinent.getContinentCountries()) {
                if (country.getCountryID() == currentCountry.getCountryID()) {
                    currentCountry.setOwningPlayer(player);
                }
            }
        }
    }


    public int returnForcesPerRoundsPerPlayer(Player player) { //, boolean cards ) {
        int forcesCount = 0;

        if (getNumberOfCountriesOfPlayer(player) < 9) {
            forcesCount += 3;
            //getPlayersCardList()
        } else {
            forcesCount = getNumberOfCountriesOfPlayer(player) / 3;
        }

        //Check if continent is completely occupied and add forces accordingly
        if (isContinentOccupied(player, 1)) {
            System.out.println("player owns " + continentList.get(0).getName());
            forcesCount += continentList.get(0).getValue();
        }
        if (isContinentOccupied(player, 2)) {
            System.out.println("player owns " + continentList.get(1).getName());
            forcesCount += continentList.get(1).getValue();
        }
        if (isContinentOccupied(player, 3)) {
            System.out.println("player owns " + continentList.get(2).getName());
            forcesCount += continentList.get(2).getValue();
        }
        if (isContinentOccupied(player, 4)) {
            System.out.println("player owns " + continentList.get(3).getName());
            forcesCount += continentList.get(3).getValue();
        }
        if (isContinentOccupied(player, 5)) {
            System.out.println("player owns " + continentList.get(4).getName());
            forcesCount += continentList.get(4).getValue();
        }
        if (isContinentOccupied(player, 6)) {
            System.out.println("player owns " + continentList.get(5).getName());
            forcesCount += continentList.get(5).getValue();
        }

        if(isCardStackFulfilled(getPlayersCardList(player))){
            forcesCount += 3;
        }

        /*if (isCardStackFulfilled(getPlayersCardList(player))) {
            Vector<customCard> currentCardList = worldManager.getPlayersCardList(currentPlayer), tempCardList = new Vector<>();
            int selectedCardID;
            boolean isFulfilled = false, checkInfantry = false, checkCavalry = false, checkArtillery = false, checkJoker = false;


            printPlayersCardList(currentPlayer);
            System.out.println("");
            System.out.println("Please select up to three cards or 99 to skip this process.");
            cardLoop:
            while (true) {
                do {
                    System.out.println("#>");
                    selectedCardID = (Integer.parseInt(readInput())) - 1;
                    if (tempCardList.contains(currentCardList.get(selectedCardID))) {
                        System.out.println("You have already selected this card. Please choose another one.");
                        continue;
                    } else {
                        tempCardList.add(currentCardList.get(selectedCardID));
                        System.out.println("You have selected " + currentCardList.get(selectedCardID).getCardName());
                    }
                } while (tempCardList.size() < 2);

                if (risiko.isCardStackFulfilled(tempCardList)) {
                    System.out.println("Input correct!");
                    if (cardExchangeArmies < 12) {
                        cardExchangeArmies = +2;
                        forcesLeft += cardExchangeArmies;
                    } else if (cardExchangeArmies < 15) {
                        cardExchangeArmies += 3;
                        forcesLeft += cardExchangeArmies;
                    } else {
                        cardExchangeArmies += 5;
                        forcesLeft += cardExchangeArmies;
                    }

                    Vector<Card> cardList = risiko.getCardList();
                    for (Card c : tempCardList) {
                        cardList.remove(c);
                    }

                    break cardLoop;
                } else {
                    System.out.println("Input incorrect! Please choose your cards again");
                    tempCardList.removeAllElements();
                    tempCardList.trimToSize();
                    continue;
                }

            }*/
            return forcesCount;
        }


    public void distributeCountries(List<Player> playerList) throws ArithmeticException {
        int counter = 0;
        for (Continent continent : continentList) {
            Vector<Country> tempCountryList = new Vector<>(continent.getContinentCountries());
            Collections.shuffle(tempCountryList);

            for (Country country : tempCountryList) {
                country.setOwningPlayer(playerList.get(counter++ % playerList.size()));
            }
        }
    }

    public void distributeCard(Player player) throws IllegalStateException {
        Vector<customCard> emptyCardList = new Vector<>(cardList);
        Random rng = new Random();
        for (customCard currentCard : cardList) {
            if (currentCard.getOwningPlayer() == null) {
                emptyCardList.add(currentCard);
            }
        }
        if (emptyCardList.isEmpty()) {
            throw new IllegalStateException();
        } else {
            int draw = rng.nextInt(emptyCardList.size() - 1);
            emptyCardList.get(draw).setOwningPlayer(player);
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
        Vector<Country> ownedCountriesList = new Vector<Country>();

        for (Continent continent : continentList) {
            for (Country country : continent.getContinentCountries()) {
                if (country.getOwningPlayer().equals(player)) {
                    ownedCountriesList.add(country);
                }
            }
        }
        return ownedCountriesList;
    }

    public Vector<Country> loadEnemyCountryList(Player player) {
        Vector<Country> enemyCountriesList = new Vector<>();
        for (Continent continent : continentList) {
            for (Country currentCountry : continent.getContinentCountries()) {
                if (!currentCountry.getOwningPlayer().equals(player)) {
                    enemyCountriesList.add(currentCountry);
                }
            }
        }
        return enemyCountriesList;
    }

    public Vector<Country> loadDistributionCountriesList(Player player) throws NoAlliedCountriesNearException {

        Vector<Country> ownedCountriesList = loadOwnedCountryList(player);
        Vector<Country> distributionCountriesList = new Vector<>();
        Vector<Country> neighbouringCountriesList = new Vector<>();

        for (Country c : ownedCountriesList) {
            try {
                neighbouringCountriesList = loadNeighbouringCountriesListForDistributionPhase(c);
            } catch (NoAlliedCountriesNearException e) {

            }
            if (c.getLocalForces() > 1 && !(neighbouringCountriesList.isEmpty())) {
                distributionCountriesList.add(c);
            }
        }
        if (distributionCountriesList.isEmpty()) {
            throw new NoAlliedCountriesNearException();
        }
        return distributionCountriesList;


    }


    public Vector<Country> loadOwnedCountryListWithMoreThanOneForce(Player player) {
        // ConcurrentModificationException?
        Vector<Country> ownedCountriesList = loadOwnedCountryList(player);
        ownedCountriesList.removeIf(country -> country.getLocalForces() < 2);
        ownedCountriesList.trimToSize();

        return ownedCountriesList;
    }

    //ATTACKING
    //1
    public Vector<Country> loadAttackingCountriesList(Player player) throws NoEnemyCountriesNearException {

        Vector<Country> ownedCountriesList = loadOwnedCountryList(player);
        Vector<Country> attackingCountriesList = new Vector<>();
        Vector<Country> neighbouringCountriesList = new Vector<>();

        for (Country c : ownedCountriesList) {
            try {
                neighbouringCountriesList = loadNeighbouringCountryListForAttackingPhase(c);
            } catch (NoEnemyCountriesNearException e) {

            }
            if (c.getLocalForces() > 1 && !(neighbouringCountriesList.isEmpty())) {
                attackingCountriesList.add(c);
            }
        }
        if (attackingCountriesList.isEmpty()) {
            throw new NoEnemyCountriesNearException();
        }
        return attackingCountriesList;


    }


    public Vector<Country> loadNeighbouringCountriesListForDistributionPhase(Country country) throws
            NoAlliedCountriesNearException {

        Vector<Country> neighbouringCountriesList = new Vector<>();

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
    public Vector<Country> loadNeighbouringCountryListForAttackingPhase(Country country) throws
            NoEnemyCountriesNearException {

        Vector<Country> neighbouringCountriesList = new Vector<>();

        int[] neighbouringCountriesListIDs = country.getNeighbouringCountries();
        for (int n : neighbouringCountriesListIDs) {
            if (!(getCountryByID(n).getOwningPlayer()).equals(country.getOwningPlayer())) {
                neighbouringCountriesList.add(getCountryByID(n));
            }
        }
        if (neighbouringCountriesList.isEmpty()) {
            throw new NoEnemyCountriesNearException();
        }
        return neighbouringCountriesList;

    }

    public Vector<customCard> getCardList() {
        return cardList;
    }


    // Checks if the continent ist occupied by the player

    public boolean isContinentOccupied(Player player, int continentNumber) {
        Continent continent = getContinentByID(continentNumber);
        int index = 0;
        for (Country country : continent.getContinentCountries()) {
            index++;
            if (!country.getOwningPlayer().equals(player))
                return false;
        }
        if (index != 0) {
            return true;
        }
        return false;
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

    public Vector<customCard> getPlayersCardList(Player player) {
        Vector<customCard> playersCardList = new Vector<>();
        for (customCard c : cardList) {
            if (c.getOwningPlayer().equals(player)) {
                playersCardList.add(c);
            }
        }
        return playersCardList;
    }

    /*public int getForcesByCards(Player p) {
        Vector<customCard> playersCardList = getPlayersCardList(p);
        int cardIndex = 0, cardIndex1 = 0, cardIndex2 = 0, cardIndex3 = 0, cardIndex4 = 0;
        int totalForces = 0;

        for (customCard card : playersCardList) {
            switch (card.getCardType()) {
                case 1:
                    cardIndex1++;
                    break;
                case 2:
                    cardIndex2++;
                    break;
                case 3:
                    cardIndex3++;
                    break;
                case 4:
                    cardIndex4++;
                    break;
            }
        }

        if (cardIndex1 >= 3) {
            totalForces += 1;
            country -> country.getCountryName().equals(c2.getCountryName())

            playersCardList.removeIf(customCard -> )
        }
        totalForces += 1;
        if (cardIndex2 >= 3)
            totalForces += 1;
        if (cardIndex3 >= 3)
            totalForces += 1;
        if (cardIndex4 >= 3)
            totalForces += 1;


    }*/

    //_____________________________________________________________________________________________________________


    //_____________________________________________________________________________________________________________


    public void createGameFile() {
        //TODO ersetzt die text Datei,
        //TODO THIS IS NOT THE FINAL VERSION, PLEASE USE METHOD ABOVE!!

        for (int i = 0, index = 1; i < continentList.size(); i++) {
            for (int j = 0; j < continentList.get(i).getContinentCountries().size(); j++) {
                if (index < 9) {
                    cardList.add(new customCard(1, index, getCountryByID(index).getCountryName()));
                    index++;
                } else if (index < 20) {
                    cardList.add(new customCard(2, index, getCountryByID(index).getCountryName()));
                    index++;
                } else if (index < 29) {
                    cardList.add(new customCard(3, index, getCountryByID(index).getCountryName()));
                    index++;
                } else {
                    cardList.add(new customCard(4, index, "Joker"));
                    index++;
                }
            }
        }
    }

    public boolean annihilation(Player p) {

        for (Continent currentContinent : continentList) {
            for (Country currentCountry : currentContinent.getContinentCountries()) {
                if (!p.equals(currentCountry.getOwningPlayer())) {
                    return false;
                }
            }
        }
        return true;
    }


    public Country compareHEX(String HEXvalue) {
        for (Continent currentContinent : continentList) {
            for (Country currentCountry : currentContinent.getContinentCountries())
                if (currentCountry.getHEX().equals(HEXvalue)) {
                    return currentCountry;
                }
        }
        return null;
    }


    public Vector<Country> getCountryList() {
        Vector<Country> countryList = new Vector<>();
        for (Continent continent : getContinentList()) {
            for (Country country : continent.getContinentCountries()) {
                countryList.add(country);
            }
        }
        return countryList;
    }


    public boolean isCardStackFulfilled(Vector<customCard> playerCards) {
        boolean isFulfilled = false, checkInfantry = false, checkCavalry = false, checkArtillery = false, checkJoker = false;
        int infantry = 0, cavalry = 0, artillery = 0, joker = 0;

        if (playerCards.size() < 3) {
            for (customCard currentCard : playerCards) {
                switch (currentCard.getCardType()) {
                    case 1:
                        infantry++;
                        break;
                    case 2:
                        cavalry++;
                        break;
                    case 3:
                        artillery++;
                        break;
                    case 4:
                        joker++;
                        break;
                }
                if (infantry > 0) {
                    checkInfantry = true;
                }
                if (cavalry > 0) {
                    checkCavalry = true;
                }
                if (artillery > 0) {
                    checkArtillery = true;
                }
                if (joker > 0) {
                    checkJoker = true;
                }
                if ((checkInfantry && checkCavalry && checkArtillery)
                        || (checkJoker && checkInfantry && checkCavalry)
                        || (checkJoker && checkCavalry && checkArtillery)
                        || (checkJoker && checkInfantry && checkArtillery)
                        || (checkJoker && (infantry > 1 || cavalry > 1 || artillery > 1))
                        || (joker > 1 && (checkInfantry || checkCavalry || checkArtillery))) {

                    return isFulfilled = true;
                } else {
                    return isFulfilled;
                }

            }
        } else {
            isFulfilled = false;
        }
        return isFulfilled;
    }

}