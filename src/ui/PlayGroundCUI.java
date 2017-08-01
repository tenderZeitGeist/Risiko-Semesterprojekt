package ui;

import domain.Risiko;
import domain.WorldVerwaltung;
import domain.exceptions.*;


import valueobjects.Card;
import valueobjects.Country;
import valueobjects.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;


/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 30.03.2017.
 */
public class PlayGroundCUI {
    private BufferedReader in;
    private WorldVerwaltung worldManager;

    private Player nextPlayer;


    private Risiko risiko;
    private List<Player> tempPlayerList;


    private int playerInitialTurn = 0;
    private int cardExchangeArmies = 4;
    private int roundNumber = 1;
    private int maxRoundNumber = 100;


    public PlayGroundCUI() throws IOException {

        risiko = new Risiko();
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void main(String[] args) throws IOException, CountryAlreadyExistsException, ClassNotFoundException {
        PlayGroundCUI pCUI = new PlayGroundCUI();
        IO io = new IO();
        pCUI.run();


    }

    public String readInput() {
        String inputSTRING = "";
        int inputINT;
        try {
            inputSTRING = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputSTRING;
    }

    public void run() throws IOException, CountryAlreadyExistsException, ClassNotFoundException {
        String input = "";
        do {
            printMenu();
            System.out.print("##>");
            input = readInput();
            processInput(input);

        } while (!input.equals("q"));

    }

    public void processInput(String input) throws IOException, ClassNotFoundException, CountryAlreadyExistsException {

        switch (input) {
            case "n":       //start new game
                //TODO start_new_game() should be called here
                System.out.println("new game started!");
                risiko.readData("Risiko-Semesterprojekt/countryList.txt");
                startGameCUI();

                break;
            case "s":       //check scores
                break;
            case "j":       //join game
                //TODO join game over the netwerk
                System.out.println("joining game...");
                break;
            case "k":       //dummy
                break;
            case "l":       //load game
                System.out.println("load game");
                //risiko.readGameData("runningGame.txt");
                Vector < Player > playerList = risiko.deSerializePlayers();
                risiko.setPlayerList(playerList);
                risiko.writeMissionsFromFile();
                risiko.deSerializeCountries();
                round(playerList);
                break;
            case "q":       //quit da shit
                break;
            default:
                break;
        }
    }


    public void printMenu() {
        System.out.println("");
        System.out.println("<------ GAME MENU ------>");
        System.out.println("- what do you want to do?");
        System.out.println("- start a new game    (enter 'n')");
        System.out.println("- join live game      (enter 'j')");
        System.out.println("- load game           (enter 'l')");
        System.out.println("- check last scores   (enter 's')");
        System.out.println("-");
        System.out.println("- to quit enter 'q'");

    }


    public void startGameCUI() throws IOException {
        // int playerCount = 1;
        int playerCount;
        boolean bool = true;


        System.out.println("");
        System.out.println("<------- NEW GAME ------>");
        System.out.println("- 2 to 6 players can play at once");
        System.out.println("- How many players want to play?");
        System.out.print("##>");

        //TODO change input to throw correct error when String is entered

        while (bool)
            try {
                playerCount = new Integer(readInput());


                if (!(playerCount < 2 || playerCount > 6)) {
                    System.out.println("- " + playerCount + " players joined the game");

                    int aktiveSpieler = 0;
                    do {
                        System.out.println("- What is the name of player " + (aktiveSpieler + 1) + "?");
                        System.out.print("##>");
                        String playerNameTemp = readInput();
                        try {
                            risiko.createPlayer(aktiveSpieler + 1, playerNameTemp);

                            aktiveSpieler++;
                        } catch (PlayerAlreadyExistsException e) {
                            System.out.println(e.getMessage());
                            System.out.println("Bitte wählen Sie einen anderen Namen.");
                        }
                        System.out.println("- your name is " + playerNameTemp);
                        System.out.println("");
                    } while (aktiveSpieler < playerCount);
                } else {
                    System.out.println("- Please enter a number from 2 to 6!");
                    startGameCUI();

                }
                bool = false;

            } catch (NumberFormatException e) {
                System.out.println("- Please enter a number from 2 to 6!");
                startGameCUI();
            }

        risiko.distributeCountries();

        System.out.println("");

        System.out.println("____________________________S__T__A__R__T__________________________________");
        System.out.println("");

        List<Player> playerList = new Vector<Player>(risiko.getPlayerList());

        risiko.distributeMissions();


        for (int p = 0; p < playerList.size(); p++) {


            System.out.println("Its " + playerList.get(p).getPlayerName() + "'s turn, please distribute your forces!");
            distribureForcesMenuInitial(playerList.get(p), risiko.loadOwnedCountryList(playerList.get(p)));


            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("<----------------------->");
            System.out.println("");
            System.out.println("");


        }

        round(risiko.getPlayerList());

    }


    public void round(List<Player> playerList) {


        System.out.println("Round: " + roundNumber);


        for (int i = 0; i < playerList.size(); i++) {
            //if (!risiko.missionFulfilled(playerList.get(i))) { //set this to true to fake a win!
            turn(playerList.get(i));

            /*} else {
                System.out.println("");
                System.out.println("PLAYER " + playerList.get(i).getPlayerName() + " WON!!!");
                System.out.println("");

                roundNumber = maxRoundNumber;
                break;*/
        }


        roundNumber++;

        if (roundNumber <= maxRoundNumber) {
            round(playerList);
        } else {
            endGameBIGBOSS();
        }


    }


    public void turn(Player currentPlayer) {


        String currentPlayerName = currentPlayer.getPlayerName();
        Vector<Country> ownedCountriesList;
        ownedCountriesList = risiko.loadOwnedCountryList(currentPlayer);


        System.out.println("");
        System.out.println("<----------------------> " + currentPlayerName);


        distributeForcesMenu(currentPlayer, ownedCountriesList);


        try {
            attackEnemyMenu(currentPlayer);
        } catch (CancelAttackException e) {
            System.out.println(e.getMessage());
        }
        try {
            moveForcesEndOfRound(currentPlayer);
        } catch (CancelDistributeForcesEndOfRound | NoAlliedCountriesNearException ex) {
            System.out.println(ex.getMessage());
        }


        /*if ( risiko.missionFulfilled ( currentPlayer ) ) {
            endGameBIGBOSS ( );
        }*/

    }


    public void distributeForcesMenu(Player currentPlayer, Vector<Country> ownedCountriesList) {
        //boolean cards = risiko.isCardStackFulfilled(worldManager.getPlayersCardList(currentPlayer));
        int initForces = risiko.returnForcesPerRoundsPerPlayer(currentPlayer); //, cards);
        int forcesLeft = initForces;
        int selectedCountryIDTemp;
        int selectedForcesCountTemp = 0;
        int temp = 0;

        /*if ( cards ) {
            Vector < Card > currentCardList = worldManager.getPlayersCardList ( currentPlayer ), tempCardList = new Vector <> ( );
            int selectedCardID;
            boolean isFulfilled = false, checkInfantry = false, checkCavalry = false, checkArtillery = false, checkJoker = false;


            printPlayersCardList ( currentPlayer );
            System.out.println ( "" );
            System.out.println ( "Please select up to three cards or 99 to skip this process." );
            cardLoop:
            while ( true ) {
                do {
                    System.out.println ( "#>" );
                    selectedCardID = ( Integer.parseInt ( readInput ( ) ) ) - 1;
                    if ( tempCardList.contains ( currentCardList.get ( selectedCardID ) ) ) {
                        System.out.println ( "You have already selected this card. Please choose another one." );
                        continue;
                    } else {
                        tempCardList.add ( currentCardList.get ( selectedCardID ) );
                        System.out.println ( "You have selected " + currentCardList.get ( selectedCardID ).getCardName ( ) );
                    }
                } while ( tempCardList.size ( ) < 2 );

                if ( risiko.isCardStackFulfilled ( tempCardList ) ) {
                    System.out.println ( "Input correct!" );
                    if ( cardExchangeArmies < 12 ) {
                        cardExchangeArmies = + 2;
                        forcesLeft += cardExchangeArmies;
                    } else if ( cardExchangeArmies < 15 ) {
                        cardExchangeArmies += 3;
                        forcesLeft += cardExchangeArmies;
                    } else {
                        cardExchangeArmies += 5;
                        forcesLeft += cardExchangeArmies;
                    }

                    Vector < Card > cardList = risiko.getCardList ();
                    for ( Card c : tempCardList ) {
                        cardList.remove ( c );
                    }

                    break cardLoop;
                } else {
                    System.out.println ( "Input incorrect! Please choose your cards again" );
                    tempCardList.removeAllElements ( );
                    tempCardList.trimToSize ( );
                    continue;
                }

            }*/


        System.out.println("");
        System.out.println("Your mission for this game:");

        System.out.println(risiko.getMissionPerPlayer(currentPlayer).getDescription());

        System.out.println("");


        System.out.println("");
        System.out.println("<---- SET FORCE MENU --->");


        while (!(forcesLeft <= 0)) {

            System.out.println("");
            System.out.println("You have " + forcesLeft + " forces to set. ");
            System.out.println("Which country do you want to set forces on?");
            System.out.println("");
            Country selectedCountryTemp;

            printOwnedCountriesList(currentPlayer, ownedCountriesList);
            System.out.println("");
            System.out.println("please enter the number of the country you want to select");
            System.out.println("");
            System.out.print("##>");


            try {
                selectedCountryIDTemp = new Integer(readInput());
                selectedCountryIDTemp -= 1;
                selectedCountryTemp = ownedCountriesList.get(selectedCountryIDTemp);
                System.out.println("");
                System.out.println("You have " + forcesLeft + " forces to set. ");
                System.out.println("You selected " + selectedCountryTemp.getCountryName() + ". How many Forces do you want to set?");
                System.out.println("");
                System.out.print("##>");
                selectedForcesCountTemp = new Integer(readInput());


                if (selectedForcesCountTemp > forcesLeft) {
                    System.out.println("");
                    System.out.println("<------- WARNING ------->");
                    System.out.println("Please enter a valid amount!");
                    System.out.println("");
                    continue;

                } else {
                    risiko.setForcesToCountry(selectedCountryTemp, selectedForcesCountTemp);


                    //TODO change input to throw correct error when String is entered
                    System.out.println("You set " + selectedForcesCountTemp + " forces on country: " + selectedCountryTemp.getCountryName());

                    forcesLeft -= selectedForcesCountTemp;
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("");
                System.out.println("Please enter the correct number!");
            }


        }
        //MOAR COMMANDS AFTER FORCE VERTEILUNG
        //
    }


    public void distribureForcesMenuInitial(Player currentPlayer, Vector<Country> ownedCountriesList) {

        int initForces = 25;
        int forcesLeft = initForces;
        int selectedCountryIDTemp;
        int selectedForcesCountTemp = 0;


        System.out.println("");
        System.out.println("Your mission for this game:");

        System.out.println(risiko.getMissionPerPlayer(currentPlayer).getDescription());

        System.out.println("");


        System.out.println("");
        System.out.println("<---- SET FORCE MENU --->");


        while (!(forcesLeft <= 0)) {

            System.out.println("");
            System.out.println("You have " + forcesLeft + " forces to set. ");
            System.out.println("Which country do you want to set forces on?");
            System.out.println("");
            Country selectedCountryTemp;

            printOwnedCountriesList(currentPlayer, ownedCountriesList);
            System.out.println("");
            System.out.println("please enter the number of the country you want to select");
            System.out.println("");
            System.out.print("##>");


            try {
                selectedCountryIDTemp = new Integer(readInput());
                selectedCountryIDTemp -= 1;
                selectedCountryTemp = ownedCountriesList.get(selectedCountryIDTemp);

                System.out.println("");
                System.out.println("You have " + forcesLeft + " forces to set. ");
                System.out.println("You selected " + selectedCountryTemp.getCountryName() + ". How many Forces do you want to set?");
                System.out.println("");
                System.out.print("##>");
                selectedForcesCountTemp = new Integer(readInput());


                if (selectedForcesCountTemp > forcesLeft) {
                    System.out.println("");
                    System.out.println("<------- WARNING ------->");
                    System.out.println("Please enter a valid amount!");
                    System.out.println("");
                    continue;

                } else {
                    risiko.setForcesToCountry(selectedCountryTemp, selectedForcesCountTemp);


                    //TODO change input to throw correct error when String is entered
                    System.out.println("You set " + selectedForcesCountTemp + " forces on country: " + selectedCountryTemp.getCountryName());

                    forcesLeft -= selectedForcesCountTemp;
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("");
                System.out.println("Please enter the correct number!");
            }
        }
        //MOAR COMMANDS AFTER FORCE VERTEILUNG
        //
    }


    public void attackEnemyMenu(Player currentPlayer) throws CancelAttackException {
        int attackingForces = 0;
        int selectedCountryIDTemp;
        Country selectedAttackerCountryTemp = null;
        Country selectedDefenderCountryTemp = null;
        int selectedDefenderIDTemp;
        int distributeForces = 0;
        String inputGameSave;


        try {

            do {

                boolean attackCountryChosen = false;
                boolean bool1 = true;
                boolean bool2 = true;
                boolean bool3 = true;
                boolean bool4 = true;

                System.out.println("");
                System.out.println("<----- ATTACK MENU ----->");
                System.out.println("");

                System.out.println("Select country to attack from:");
                System.out.println("");

                printAttackingCountriesListList(currentPlayer);

                while (!attackCountryChosen) {
                    try {

                        System.out.println("");
                        System.out.println("Enter 99 to skip the attack");
                        System.out.println("");
                        System.out.println("Please enter the number of the country you want to select.");
                        System.out.print("##>");

                        selectedCountryIDTemp = new Integer(readInput());

                        if (selectedCountryIDTemp == 99) {
                            throw new CancelAttackException();
                        }
                        selectedCountryIDTemp -= 1;

                        selectedAttackerCountryTemp = risiko.loadAttackingCountriesList(currentPlayer).get(selectedCountryIDTemp);

                        System.out.println("");
                        System.out.println(selectedAttackerCountryTemp.getCountryName() + " will attack!");
                        System.out.print("");

                        attackCountryChosen = true;

                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Please enter the correct number 11111!");
                    }
                }

                while (bool1) {
                    try {

                        System.out.println("Which country do you want to attack?");
                        System.out.println("");

                        //TODO: return neighbouring countries the right way!!!
                        printNeighbouringCountriesListForAttackingPhase(selectedAttackerCountryTemp);
                        Vector<Country> tempCountriesList = risiko.loadNeighbouringCountriesListForAttackingPhase(selectedAttackerCountryTemp);
                        System.out.println("Please enter the number of the country you want to select.");
                        System.out.print("##>");


                        selectedDefenderIDTemp = new Integer(readInput());
                        selectedDefenderCountryTemp = tempCountriesList.get(selectedDefenderIDTemp - 1);


                        System.out.println("");
                        System.out.println("The attacking Country is " + selectedAttackerCountryTemp.getCountryName() + " it has " + selectedAttackerCountryTemp.getLocalForces() + " forces.");


                        bool1 = false;

                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Please enter the correct number222222!");
                        System.out.println("");
                    }
                }

                while (bool2) {
                    try {
                        System.out.println("How many do you want to use for the attack?");
                        System.out.println("");
                        System.out.print("##>");
                        attackingForces = new Integer(readInput());

                        if (attackingForces > 3) {
                            System.out.println("You can only attack with 3 forces at once.");
//                            attackEnemyMenu(currentPlayer);
                            break;
                        }
                        if (selectedAttackerCountryTemp.getLocalForces() - attackingForces <= 0) {
                            System.out.println("You can only attack with 2 forces.");
//                            attackEnemyMenu(currentPlayer);
                            break;
                        }

                        int defendingForces = selectedDefenderCountryTemp.getLocalForces();

                        if (defendingForces < 2) {
                            defendingForces = 1;
                        } else {
                            defendingForces = 2;
                        }

                        bool2 = false;

                        if (risiko.battle(selectedAttackerCountryTemp, selectedDefenderCountryTemp, attackingForces, defendingForces)) {
                            if (risiko.missionFulfilled(currentPlayer)) {
                                endGameBIGBOSS();
                            }
                            while (bool3) {
                                try {
                                    while (bool4) {
                                        System.out.println("");
                                        System.out.println("How many forces do you want to distribute to the conquered country on top of the " +
                                                "forces you used for the attack?");
                                        System.out.println("You have " + (selectedAttackerCountryTemp.getLocalForces() - 1) + " forces to distribute.");
                                        System.out.println("");
                                        System.out.print("##>");
                                        distributeForces = new Integer(readInput());

                                        if (distributeForces < selectedAttackerCountryTemp.getLocalForces() + 1) {
                                            risiko.setForcesToCountry(selectedDefenderCountryTemp, distributeForces);
                                            risiko.setForcesToCountry(selectedAttackerCountryTemp, -distributeForces);
                                            System.out.println("You set " + distributeForces + " on" + selectedAttackerCountryTemp.getCountryName() + ".");
                                            bool4 = false;

                                        } else {
                                            System.out.println("Please enter the correct number! Keep in mind you" +
                                                    " have to have at least 1 force left on each country!");
                                        }
                                    }

                                    bool3 = false;

                                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                                    System.out.println("Please enter the correct number444444!");
                                    System.out.println(e.getMessage());
                                    System.out.println("");
                                }
                            }
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Please enter the correct number333333!");
                        System.out.println(e.getMessage());
                        System.out.println("");
                    }
                }
//                attackEnemyMenu(currentPlayer);
            } while (true);

        } catch (NoEnemyCountriesNearException e) {
            System.out.println("There are currently no countries available to attack.");
        }
    }


    public void moveForcesEndOfRound(Player currentPlayer) throws CancelDistributeForcesEndOfRound, NoAlliedCountriesNearException {
        int countriesWithMoreThanOneForce = risiko.loadDistributionCountriesList(currentPlayer).size();
        int selectedCountryIDTempFrom;
        int selectedCountryIDTempTo;
        int forcesToDistribute = 0;
        int selectedForcesCountTemp;
        String inputGameSave;
        Vector<Country> distributionCountriesList = risiko.loadDistributionCountriesList(currentPlayer);


        Country selectedCountryTempFrom;
        Country selectedCountryTempTo;
        try {
            while (!(countriesWithMoreThanOneForce <= 0)) {

                System.out.println("");
                System.out.println("Please choose a country to redistribute forces from.");
                System.out.println("");

                printDistributionCountriesListList(currentPlayer);

                System.out.println("");
                System.out.println("Enter 99 to end reditsribution of forces");

                try {

                    System.out.print("##>");
                    selectedCountryIDTempFrom = new Integer(readInput());

                    if (selectedCountryIDTempFrom == 99) {

                        System.out.println("Do you want to save the game? y/n");

                        try {
                            System.out.print("##>");
                            inputGameSave = readInput();
                            if (inputGameSave.equals("y")) {
                                //risiko.writeData();

                                risiko.serializePlayers(currentPlayer);
                                risiko.serializeCountries();
                                risiko.serializeMissions();
                                return;

                                // maybe "exitGame function" here?

                            } else if (inputGameSave.equals("n")) {
                                return;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    selectedCountryIDTempFrom -= 1;
                    selectedCountryTempFrom = distributionCountriesList.get(selectedCountryIDTempFrom);
                    forcesToDistribute = distributionCountriesList.get(selectedCountryIDTempFrom).getLocalForces() - 1;

                    System.out.println("You selected " + selectedCountryTempFrom.getCountryName());
                    System.out.println("You have " + forcesToDistribute + " forces to distribute. ");
                    System.out.println("Please enter how many forces do you want to distribute.");
                    System.out.println("");
                    System.out.print("##>");
                    selectedForcesCountTemp = new Integer(readInput());

                    if (selectedForcesCountTemp > forcesToDistribute) {
                        System.out.println("");
                        System.out.println("<------- WARNING ------->");
                        System.out.println("Please enter a valid amount!");
                        System.out.println("");

                    } else {
                        System.out.println("To which country do you want to set forces on?");

                        printNeighbouringCountriesListForDistributionPhase(selectedCountryTempFrom);
                        System.out.print("##>");

                        selectedCountryIDTempTo = new Integer(readInput());
                        selectedCountryIDTempTo--;
                        Vector<Country> neighbouringCountriesList = risiko.loadNeighbouringCountriesListForDistributionPhase(selectedCountryTempFrom);


                        selectedCountryTempTo = neighbouringCountriesList.get(selectedCountryIDTempTo);

                        risiko.moveForces(selectedCountryTempFrom, selectedCountryTempTo, selectedForcesCountTemp);

                        System.out.println("You set " + selectedForcesCountTemp + " forces on country: "
                                + neighbouringCountriesList.get(selectedCountryIDTempTo).getCountryName());

                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("");
                    System.out.println("Please enter the correct number!");
                    moveForcesEndOfRound(currentPlayer);
                }
            }
        } catch (NoAlliedCountriesNearException e) {
            System.out.println("There are currently no countries available to distribute forces to.");
        }
    }


//______________________________________________________________________________________

    public void printDistributionCountriesListList(Player currentPlayer) throws NoAlliedCountriesNearException {
        Vector<Country> distributionCountriesList = risiko.loadDistributionCountriesList(currentPlayer);
        int index = 1;

        System.out.format("%2s%21s%7s", "Number: ", "Country: ", "Forces: ");
        System.out.println("");
        System.out.println("----------------------------------------");
        for (int i = 0; i < distributionCountriesList.size(); i++) {

            System.out.format("%2d%25s%7d", index, distributionCountriesList.get(i).getCountryName(), distributionCountriesList.get(i).getLocalForces());
            System.out.println("");
            index++;
        }
    }

    public void printOwnedCountriesList(Player player, Vector<Country> ownedCountriesList) {

        int index = 1;

        System.out.format("%2s%21s%7s", "Number: ", "Country: ", "Forces: ");
        System.out.println("");
        System.out.println("----------------------------------------");
        for (int i = 0; i < ownedCountriesList.size(); i++) {

            System.out.format("%2d%25s%7d", index, ownedCountriesList.get(i).getCountryName(), ownedCountriesList.get(i).getLocalForces());
            System.out.println("");
            index++;
        }
    }

    public void printNeighbouringCountriesListForDistributionPhase(Country country) throws NoAlliedCountriesNearException {
        Vector<Country> neighbouringCountriesListForDistributionPhase = risiko.loadNeighbouringCountriesListForDistributionPhase(country);

        int index = 1;
        System.out.format("%2s%21s%7s", "Number: ", "Country: ", "Forces: ");
        System.out.println("");
        System.out.println("----------------------------------------");

        for (int i = 0; i < neighbouringCountriesListForDistributionPhase.size(); i++) {

            System.out.format("%2d%25s%7d", index, neighbouringCountriesListForDistributionPhase.get(index - 1).getCountryName(), neighbouringCountriesListForDistributionPhase.get(index - 1).getLocalForces());
            System.out.println("");
            index++;

        }
        System.out.println("----------------------------------------");
    }

    public void printNeighbouringCountriesListForAttackingPhase(Country country) throws NoEnemyCountriesNearException {
        Vector<Country> neighbouringCountriesList = risiko.loadNeighbouringCountriesListForAttackingPhase(country);

        int index = 1;
        System.out.format("%2s%21s%7s", "Number: ", "Country: ", "Forces: ");
        System.out.println("");
        System.out.println("----------------------------------------");

        for (int i = 0; i < neighbouringCountriesList.size(); i++) {

            System.out.format("%2d%25s%7d", index, neighbouringCountriesList.get(index - 1).getCountryName(), neighbouringCountriesList.get(index - 1).getLocalForces());
            System.out.println("");
            index++;

        }
        System.out.println("----------------------------------------");
    }

    public void printAttackingCountriesListList(Player player) throws NoEnemyCountriesNearException {
        Vector<Country> attackingCountriesList = risiko.loadAttackingCountriesList(player);
        int index = 1;

        System.out.format("%2s%21s%7s", "Number: ", "Country: ", "Forces: ");
        System.out.println("");
        System.out.println("----------------------------------------");

        for (int i = 0; i < attackingCountriesList.size(); i++) {
            System.out.format("%2d%25s%7d", index, attackingCountriesList.get(i).getCountryName(), attackingCountriesList.get(i).getLocalForces());
            System.out.println("");
            index++;
        }
        System.out.println("----------------------------------------");
    }

    public void printPlayersCardList(Player player) {
        Vector<Card> playerCards = risiko.getPlayersCardList(player);
        int index = 1;

        System.out.format("%23s", "Card:");
        System.out.println("");
        System.out.println("----------------------------------------");

        for (Card c : playerCards) {
            System.out.printf("%2d%21s", index++, c.getCardName());
        }
        System.out.println("----------------------------------------");
    }


    public void endGameBIGBOSS() {
        System.out.println("");
        System.out.println("  ______ _   _ _____     _____          __  __ ______ ");
        System.out.println(" |  ____| \\ | |  __ \\   / ____|   /\\   |  \\/  |  ____|");
        System.out.println(" | |__  |  \\| | |  | | | |  __   /  \\  | \\  / | |__   ");
        System.out.println(" |  __| | . ` | |  | | | | |_ | / /\\ \\ | |\\/| |  __|  ");
        System.out.println(" | |____| |\\  | |__| | | |__| |/ ____ \\| |  | | |____ ");
        System.out.println(" |______|_| \\_|_____/   \\_____/_/    \\_\\_|  |_|______|");
        System.out.println("");

    }
}
