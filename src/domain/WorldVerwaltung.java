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
import java.util.stream.IntStream;


/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 30.03.2017.
 */

public class WorldVerwaltung {

    public int countryCount = 42;
    private PersistenceManager pm = new FilePersistenceManager ( );
    private Vector < Country > countryList = new Vector < Country > ( );
    private Vector < Continent > continentList = new Vector < Continent > ( );
    private Vector < Card > cardList = new Vector <> ( );

     private Vector < Country > ownedCountriesList = new Vector <> ( );
     private Vector < Country > neighbouringCountriesList = new Vector <> ( );
     private Vector < Country > attackingCountriesList = new Vector <> ( );


    //TODO this replaces the text file for now!
    private Vector < Country > countryListEurope = new Vector < Country > ( ); // EUROPE
    private Vector < Country > countryListAsia = new Vector < Country > ( ); // ASIA
    private Vector < Country > countryListSAmerica = new Vector < Country > ( ); // SOUTH-AMERICA
    private Vector < Country > countryListNAmerica = new Vector < Country > ( ); // NORTH-AMERICA
    private Vector < Country > countryListAustralia = new Vector < Country > ( ); // AUSTRALIA
    private Vector < Country > countryListAfrica = new Vector < Country > ( ); // AFRICA


    //---------------------------------------------------------------------------------

    /**
     * Reads the Data from a .txt file in to load countries
     *
     * @param file
     * @throws IOException
     */
    public void readData ( String file ) throws IOException {
        // PersistenzManager für Lesevorgänge öffnen
        pm.openForReading ( file );

        Country oneCountry;
        do {
            // Reads in every country, line by line, from the given file
            oneCountry = pm.loadCountry ( );
            if ( oneCountry != null ) {
                // Buch in Liste einfügen
                try {
                    addCountry ( oneCountry );
                } catch ( CountryAlreadyExistsException e1 ) {
                    // Kann hier eigentlich nicht auftreten,
                    // daher auch keine Fehlerbehandlung...
                }
            }
        } while ( oneCountry != null );
        // Adding the read in countries into the continentList
        addCountriesListsToContinentList ( );
        // Persistenz-Schnittstelle wieder schließen
        //Closes the interface to persistence
        pm.close ( );
    }

    /**
     * Adds the amount of countries from the .txt into the a countryList
     *
     * @param country
     * @throws CountryAlreadyExistsException
     */
    // TODO Switch case for adding the countries into the correct countrylists
    public void addCountry ( Country country ) throws CountryAlreadyExistsException {
        // Adds countries to continent countries lists

        switch ( country.getContinentID ( ) ) {
            case 1:
                countryListNAmerica.add ( country );
                break;
            case 2:
                countryListSAmerica.add ( country );
                break;
            case 3:
                countryListEurope.add ( country );
                break;
            case 4:
                countryListAfrica.add ( country );
                break;
            case 5:
                countryListAsia.add ( country );
                break;
            case 6:
                countryListAustralia.add ( country );
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

    public void addCountriesListsToContinentList ( ) {

        continentList.add ( new Continent ( "North America", 5, 1, countryListNAmerica ) );  //Id 0
        continentList.add ( new Continent ( "South America", 2, 2, countryListSAmerica ) );  //Id 1
        continentList.add ( new Continent ( "Europe", 5, 3, countryListEurope ) );           //Id 2
        continentList.add ( new Continent ( "Africa", 3, 4, countryListAfrica ) );           //Id 3
        continentList.add ( new Continent ( "Asia", 7, 5, countryListAsia ) );               //Id 4
        continentList.add ( new Continent ( "Australia", 2, 6, countryListAustralia ) );     //Id 5
    }

    public void getCountryListNames ( ) {
        for ( Country c : countryList ) {
            System.out.println ( c.getCountryName ( ) );
        }
    }

    /**
     * Saves the date into a .txt file
     *
     * @param file
     * @throws IOException
     */
    public void writeData ( String file ) throws IOException {
        // PersistenzManager für Schreibvorgänge öffnen
        pm.openForWriting ( file );

        for ( Continent continent : continentList ) {
            for ( Country country : continent.getContinentCountries ( ) ) {
                pm.saveCountry ( country );
            }
        }
        pm.close ( );
    }

    /**
     * Logical part of WorldVerwaltung to retrieve and set information of continents/countries
     */

    public void resetOwnedCountriesList ( ) {
        ownedCountriesList.removeAllElements ( );
    }

    /*public void resetAttackingCountriesList ( ) {
        attackingCountriesList.removeAllElements ( );
    }*/

    public void resetNeighbouringCountriesList ( ) {
        neighbouringCountriesList.removeAllElements ( );
    }
/*
    public Country selectNeighbouringCountriesListByNumber ( int selectedCountryNumber ) {
        return neighbouringCountriesList.get ( selectedCountryNumber - 1 );
    }

    public Country selectAttackingCountriesListByNumber ( int selectedCountryNumber ) {
        return attackingCountriesList.get ( selectedCountryNumber - 1 );
    }
*/

    /**
     * @param attackingCountry
     * @param conqueredCountry
     * @param newForces
     */


    public void conquerCountry ( Country attackingCountry, Country conqueredCountry, int newForces ) {

        conqueredCountry.setOwningPlayer ( attackingCountry.getOwningPlayer ( ) );
        moveForces ( attackingCountry, conqueredCountry, newForces );

        loadOwnedCountryList ( attackingCountry.getOwningPlayer ( ) );
    }

    public void moveForces ( Country oldCountry, Country newCountry, int forces ) {
        newCountry.setLocalForces ( forces + newCountry.getLocalForces ( ) );
        oldCountry.setLocalForces ( oldCountry.getLocalForces ( ) - forces );
    }

    public Country getCountryByID ( int countryID ) {
        Country c = null;

        for ( Continent continent : continentList ) {
            for ( Country country : continent.getContinentCountries ( ) ) {
                if ( country.getCountryID ( ) == countryID ) {
                    c = country;
                }
            }
        }
        return c;
    }

    public Vector < Continent > getContinentList ( ) {
        return continentList;
    }

    public Vector < Country > getCountryList ( ) {
        return countryList;
    }

    public void setForcesToCountry ( Country country, int forces ) {
        int n = country.getLocalForces ( );
        country.setLocalForces ( n + forces );
    }

    public int returnForcesPerRoundsPerPlayer ( Player player ) {
        int forcesCount = 0;

        if ( getNumberOfCountriesOfPlayer ( player ) < 9 ) {
            forcesCount += 3;
        } else {
            forcesCount = getNumberOfCountriesOfPlayer ( player ) / 3;
        }

        //Check if continent is completely occupied and add forces accordingly
        if ( isContinentOccupied ( player, 1 ) ) {
            forcesCount += continentList.get ( 0 ).getValue ( );
        }
        if ( isContinentOccupied ( player, 2 ) ) {
            forcesCount += continentList.get ( 1 ).getValue ( );
        }
        if ( isContinentOccupied ( player, 3 ) ) {
            forcesCount += continentList.get ( 2 ).getValue ( );
        }
        if ( isContinentOccupied ( player, 4 ) ) {
            forcesCount += continentList.get ( 3 ).getValue ( );
        }
        if ( isContinentOccupied ( player, 5 ) ) {
            forcesCount += continentList.get ( 4 ).getValue ( );
        }
        if ( isContinentOccupied ( player, 6 ) ) {
            forcesCount += continentList.get ( 5 ).getValue ( );
        }
        return forcesCount;
    }

    /**
     * Part of the code, mainly for iteration of various queries
     */

/*    public void distributeCountries ( List < Player > playerList ) throws ArithmeticException {
        //TODO proper distribution with randomisation etc
        int playerIndex = 0;
        Vector < Country > tempCountryList;

        for ( Continent continent : continentList ) {
            tempCountryList = new Vector <> ( continent.getContinentCountries ( ) );
            while ( ! tempCountryList.isEmpty ( ) ) {
                int rollCountry = ( int ) ( Math.random ( ) * tempCountryList.size ( ) );
                tempCountryList.get ( rollCountry ).setOwningPlayer ( playerList.get ( playerIndex++ ) );
                tempCountryList.remove ( rollCountry );
                playerIndex = playerIndex % playerList.size ( );
            }
        }
    }*/
    public void distributeCountries ( List < Player > playerList ) throws ArithmeticException {
        //Thanks to nox
        int counter = 0;
        for ( Continent continent : continentList ) {
            Vector < Country > tempCountryList = new Vector <> ( continent.getContinentCountries ( ) );
            Collections.shuffle ( tempCountryList );

            for ( Country country : tempCountryList ) {
                country.setOwningPlayer ( playerList.get ( counter++ % playerList.size ( ) ) );
            }
        }
    }

    public void distributeCard ( Player player ) throws IllegalStateException {
        Vector < Card > emptyCardList = new Vector <> ( cardList );
        Random rng = new Random ( );
        for ( Card currentCard : cardList ) {
            if ( currentCard.getOwningPlayer ( ) == null ) {
                emptyCardList.add ( currentCard );
            }
        }
        if ( emptyCardList.isEmpty ( ) ) {
            throw new IllegalStateException ( );
        } else {
            int draw = rng.nextInt ( emptyCardList.size ( ) - 1 );
            emptyCardList.get ( draw ).setOwningPlayer ( player );
        }

    }


    public int getNumberOfCountriesOfPlayer ( Player player ) {
        //checks every Country in every Continent for its owner
        int numberOfCountries = 0;

        for ( Continent continent : continentList ) {
            for ( Country country : continent.getContinentCountries ( ) ) {
                if ( country.getOwningPlayer ( ).equals ( player ) ) {
                    numberOfCountries++;
                }
            }
        }
        return numberOfCountries;
    }

    //OWNED
    public Vector < Country > loadOwnedCountryList ( Player player ) {

        resetOwnedCountriesList();

        for ( Continent continent : continentList ) {
            for ( Country country : continent.getContinentCountries ( ) ) {
                if ( country.getOwningPlayer ( ).equals ( player ) ) {
                    ownedCountriesList.add ( country );
                }
            }
        }
        return ownedCountriesList;
    }

    public Vector < Country > loadDistributionCountriesList ( Player player ) throws NoAlliedCountriesNearException {
        // ConcurrentModificationException?
        /*Vector < Country > ownedCountriesList = loadOwnedCountryList ( player );
        ownedCountriesList.removeIf ( country -> country.getLocalForces ( ) < 2 );
        ownedCountriesList.trimToSize ();

        return ownedCountriesList;*/
        Vector < Country > ownedCountriesList = loadOwnedCountryList ( player );
        Vector < Country > distributionCountriesList = new Vector <> ( );
        Vector < Country > neighbouringCountriesList = new Vector <> ( );

        for ( Country c : ownedCountriesList ) {
            try {
                neighbouringCountriesList = loadNeighbouringCountriesListForDistributionPhase ( c );
            } catch ( NoAlliedCountriesNearException e ) {

            }
            if ( c.getLocalForces ( ) > 1 && ! ( neighbouringCountriesList.isEmpty ( ) ) ) {
                distributionCountriesList.add ( c );
            }
        }
        if ( distributionCountriesList.isEmpty ( ) ) {
            throw new NoAlliedCountriesNearException ( );
        }
        return distributionCountriesList;


    }

    //ATTACKING
    //1
    public Vector < Country > loadAttackingCountriesList ( Player player ) throws NoEnemyCountriesNearException {

        Vector < Country > ownedCountriesList = loadOwnedCountryList ( player );
        Vector < Country > attackingCountriesList = new Vector <> ( );
        Vector < Country > neighbouringCountriesList = new Vector <> ( );

        for ( Country c : ownedCountriesList ) {
            try {
                neighbouringCountriesList = loadNeighbouringCountryListForAttackingPhase ( c );
            } catch ( NoEnemyCountriesNearException e ) {

            }
            if ( c.getLocalForces ( ) > 1 && ! ( neighbouringCountriesList.isEmpty ( ) ) ) {
                attackingCountriesList.add ( c );
            }
        }
        if ( attackingCountriesList.isEmpty ( ) ) {
            throw new NoEnemyCountriesNearException ( );
        }
        return attackingCountriesList;


    }


    public Vector < Country > loadNeighbouringCountriesListForDistributionPhase ( Country country ) throws
            NoAlliedCountriesNearException {

        resetNeighbouringCountriesList();

        int[] neighbouringCountriesListIDs = country.getNeighbouringCountries ( );
        for ( int n : neighbouringCountriesListIDs ) {
            if ( getCountryByID ( n ).getOwningPlayer ( ).equals ( country.getOwningPlayer ( ) ) ) {
                neighbouringCountriesList.add ( getCountryByID ( n ) );
            }
        }
        if ( neighbouringCountriesList.isEmpty ( ) ) {
            throw new NoAlliedCountriesNearException ( );
        }
        return neighbouringCountriesList;
    }

    //NEIGHBOUR
    public Vector < Country > loadNeighbouringCountryListForAttackingPhase ( Country country ) throws
            NoEnemyCountriesNearException {

        resetNeighbouringCountriesList();

        int[] neighbouringCountriesListIDs = country.getNeighbouringCountries ( );
        for ( int n : neighbouringCountriesListIDs ) {
            if ( ! ( getCountryByID ( n ).getOwningPlayer ( ) ).equals ( country.getOwningPlayer ( ) ) ) {
                neighbouringCountriesList.add ( getCountryByID ( n ) );
            }
        }
        if ( neighbouringCountriesList.isEmpty ( ) ) {
            throw new NoEnemyCountriesNearException ( );
        }
        return neighbouringCountriesList;

    }


    // Checks if the continent ist occupied by the player

    public boolean isContinentOccupied ( Player player, int continentNumber ) {
        Continent continent = getContinentByID ( continentNumber );
        for ( Country country : continent.getContinentCountries ( ) ) {
            if ( ! country.getOwningPlayer ( ).equals ( player ) )
                return false;
        }
        return true;
    }

    // Iterates the continentList and compares value n with the continentID of each continent
    public Continent getContinentByID ( int n ) {
        Continent continent = null;
        for ( Continent c : continentList ) {
            if ( c.getContinentID ( ) == n ) {
                continent = c;
            }
        }
        return continent;
    }

    public Vector < Card > getPlayersCardList ( Player player ) {
        Vector < Card > playersCardList = new Vector <> ( );
        for ( Card c : cardList ) {
            if ( c.getOwningPlayer ( ).equals ( player ) ) {
                playersCardList.add ( c );
            }
        }
        return playersCardList;
    }

    //_____________________________________________________________________________________________________________


    //_____________________________________________________________________________________________________________


    public void createGameFile ( ) {
        //TODO ersetzt die text Datei,
        //TODO THIS IS NOT THE FINAL VERSION, PLEASE USE METHOD ABOVE!!

        // Adds NA countries into a list
        countryListNAmerica.add ( new Country ( "Alaska", 1, 1, null, 1, new int[] { 2 , 6 , 32 } ) );
        countryListNAmerica.add ( new Country ( "Alberta", 2, 1, null, 1, new int[] { 1 , 6 , 7 , 9 } ) );
        countryListNAmerica.add ( new Country ( "Central America", 3, 1, null, 1, new int[] { 4 , 9 , 13 } ) );
        countryListNAmerica.add ( new Country ( "Eastern United States", 4, 1, null, 1, new int[] { 3 , 7 , 8 , 9 } ) );
        countryListNAmerica.add ( new Country ( "Greenland", 5, 1, null, 1, new int[] { 6 , 7 , 8 , 15 } ) );
        countryListNAmerica.add ( new Country ( "Northwest Territory", 6, 1, null, 1, new int[] { 1 , 2 , 5 , 7 } ) );
        countryListNAmerica.add ( new Country ( "Ontario", 7, 1, null, 1, new int[] { 2 , 6 , 4 , 5 , 7 , 9 } ) );
        countryListNAmerica.add ( new Country ( "Quebec", 8, 1, null, 1, new int[] { 4 , 5 , 7 } ) );
        countryListNAmerica.add ( new Country ( "Western United States", 9, 1, null, 1, new int[] { 2 , 3 , 4 , 7 } ) );

        // Adds SA countries into a list
        countryListSAmerica.add ( new Country ( "Argentina", 10, 1, null, 2, new int[] { 11 , 12 } ) );
        countryListSAmerica.add ( new Country ( "Brazil", 11, 1, null, 2, new int[] { 10 , 12 , 13 , 25 } ) );
        countryListSAmerica.add ( new Country ( "Peru", 12, 1, null, 2, new int[] { 10 , 11 , 13 } ) );
        countryListSAmerica.add ( new Country ( "Venezuela", 13, 1, null, 2, new int[] { 3 , 12 , 11 } ) );

        // Adds EU countries into a list
        countryListEurope.add ( new Country ( "Great Britain", 14, 1, null, 3, new int[] { 15 , 17 , 16 } ) );
        countryListEurope.add ( new Country ( "Iceland", 15, 1, null, 3, new int[] { 14 , 16 , 17} ) );
        countryListEurope.add ( new Country ( "Northern Europe", 16, 1, null, 3, new int[] { 14 , 17 , 18 , 19 , 20 } ) );
        countryListEurope.add ( new Country ( "Scandinavia", 17, 1, null, 3, new int[] { 14 , 15 , 16 , 19 } ) );
        countryListEurope.add ( new Country ( "Southern Europe", 18, 1, null, 3, new int[] { 16 , 19 , 20 , 23 } ) );
        countryListEurope.add ( new Country ( "Ukraine", 19, 1, null, 3, new int[] { 16 , 17 , 18 , 27 , 33 , 37 } ) );
        countryListEurope.add ( new Country ( "Western Europe", 20, 1, null, 3, new int[] { 16 , 18 , 25 } ) );

        // Adds Africa countries into a list
        countryListAfrica.add ( new Country ( "Congo", 21, 1, null, 4, new int[] { 22 , 23 , 25 , 26 } ) );
        countryListAfrica.add ( new Country ( "East Africa", 22, 1, null, 4, new int[] { 21 , 23 , 24 , 25 , 26 , 33 } ) );
        countryListAfrica.add ( new Country ( "Egypt", 23, 1, null, 4, new int[] { 18 , 22 , 25 , 33 } ) );
        countryListAfrica.add ( new Country ( "Madagascar", 24, 1, null, 4, new int[] { 22 , 26 } ) );
        countryListAfrica.add ( new Country ( "North Africa", 25, 1, null, 4, new int[] { 20 , 21 , 22 , 23 } ) );
        countryListAfrica.add ( new Country ( "South Africa", 26, 1, null, 4, new int[] { 21 , 22 , 24 } ) );

        // Adds Asia countries into a list
        countryListAsia.add ( new Country ( "Afghanistan", 27, 1, null, 5, new int[] { 19 , 28 , 29 , 33 , 37 } ) );
        countryListAsia.add ( new Country ( "China", 28, 1, null, 5, new int[] { 27 , 29 , 34 , 35 , 36 , 37 } ) );
        countryListAsia.add ( new Country ( "India", 29, 1, null, 5, new int[] { 27 , 28 , 33 , 35 } ) );
        countryListAsia.add ( new Country ( "Irkutsk", 30, 1, null, 5, new int[] { 32 , 34 , 36 , 38 } ) );
        countryListAsia.add ( new Country ( "Japan", 31, 1, null, 5, new int[] { 32 , 34 } ) );
        countryListAsia.add ( new Country ( "Kamchatka", 32, 1, null, 5, new int[] { 30 , 31 , 34 , 38 } ) );
        countryListAsia.add ( new Country ( "Middle East", 33, 1, null, 5, new int[] { 18 , 19 , 22 , 23 , 27 , 29 } ) );
        countryListAsia.add ( new Country ( "Mongolia", 34, 1, null, 5, new int[] { 28 , 30 , 31 , 32 , 36 } ) );
        countryListAsia.add ( new Country ( "Siam", 35, 1, null, 5, new int[] { 28 , 29 , 40 } ) );
        countryListAsia.add ( new Country ( "Siberia", 36, 1, null, 5, new int[] { 30 , 34 , 37 , 38 } ) );
        countryListAsia.add ( new Country ( "Ural", 37, 1, null, 5, new int[] { 19 , 27 , 28 , 36 } ) );
        countryListAsia.add ( new Country ( "Yakutsk", 38, 1, null, 5, new int[] { 30 , 32 , 36 } ) );

        // Adds AUS countries into a list
        countryListAustralia.add ( new Country ( "Eastern Australia", 39, 1, null, 6, new int[] { 41 , 42 } ) );
        countryListAustralia.add ( new Country ( "Indonesia", 40, 1, null, 6, new int[] { 35 , 41 , 42 } ) );
        countryListAustralia.add ( new Country ( "New Guinea", 41, 1, null, 6, new int[] { 40 , 42 } ) );
        countryListAustralia.add ( new Country ( "Western Australia", 42, 1, null, 6, new int[] { 39 , 40, 41 } ) );

        // Adds all countryLists into a whole continentList
        continentList.add ( new Continent ( "North America", 5, 1, countryListNAmerica ) );  //Id 0
        continentList.add ( new Continent ( "South America", 2, 2, countryListSAmerica ) );  //Id 1
        continentList.add ( new Continent ( "Europe", 5, 3, countryListEurope ) );           //Id 2
        continentList.add ( new Continent ( "Africa", 3, 4, countryListAfrica ) );           //Id 3
        continentList.add ( new Continent ( "Asia", 7, 5, countryListAsia ) );               //Id 4
        continentList.add ( new Continent ( "Australia", 2, 6, countryListAustralia ) );     //Id 5

        // Creates and adds als cards into the cardList Vector

        for ( int i = 0, index = 1 ; i < continentList.size ( ) ; i++ ) {
            for ( int j = 0 ; j < continentList.get ( i ).getContinentCountries ( ).size ( ) ; j++ ) {
                if ( index < 15 ) {
                    cardList.add ( new Card ( 1, index, getCountryByID ( index ).getCountryName ( ) ) );
                    index++;
                } else if ( index < 29 ) {
                    cardList.add ( new Card ( 2, index, getCountryByID ( index ).getCountryName ( ) ) );
                    index++;
                } else if ( index < 43 ) {
                    cardList.add ( new Card ( 3, index, getCountryByID ( index ).getCountryName ( ) ) );
                    index++;
                } else {
                    cardList.add ( new Card ( 4, index, "Joker" ) );
                    index++;
                }
            }
        }
    }


    public boolean playerWon ( Player p, boolean b ) {
        //TODO: implement this to also ask if player achieved his goal
        //TODO split Method to fill hte ownedCountrieslist and seperate the printing from the filling

/*        for ( int i = 0 ; i < continentList.size ( ) ; i++ ) {

            for ( int j = 0 ; j < continentList.get ( i ).getContinentCountries ( ).size ( ) ; j++ ) {

                if ( continentList.get ( i ).getContinentCountries ( ).get ( j ).getOwningPlayer ( ).equals ( p ) ) {
                    ownedCountriesList.add ( continentList.get ( i ).getContinentCountries ( ).get ( j ) );
                }

            }

        }*/
        int n = getNumberOfCountriesOfPlayer ( p );

        if ( n == 42 || b ) {
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
