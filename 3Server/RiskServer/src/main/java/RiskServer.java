import domain.MissionVerwaltung;
import domain.PlayerVerwaltung;
import domain.WorldVerwaltung;
import events.GameActionEvent;
import events.GameActionEvent.GameActionEventType;
import events.GameControlEvent;
import events.GameControlEvent.GameControlEventType;
import events.GameEvent;
import exceptions.CountryAlreadyExistsException;
import exceptions.NoAlliedCountriesNearException;
import exceptions.NoEnemyCountriesNearException;
import exceptions.PlayerAlreadyExistsException;
import valueobjects.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;


/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 02.08.2017.
 */
public class RiskServer extends UnicastRemoteObject implements RemoteRisk {

    private static final long serialVersionUID = -2345681367801964822L;

    private WorldVerwaltung worldManager;
    private PlayerVerwaltung playerManager;
    private MissionVerwaltung missionVerwaltung;
    //private PlayGround playGround;
    private String file = "";

    private Vector<GameEventListener> listeners;

    private int currentPlayerID;
    private Turn currentTurn;


    public RiskServer() throws RemoteException {
        listeners = new Vector<>();
        playerManager = new PlayerVerwaltung();
        worldManager = new WorldVerwaltung();
        missionVerwaltung = new MissionVerwaltung();
    }

    public static void main(String[] args) throws IOException {
        String serverName = "RiskServer";
        RemoteRisk server = null;
        Registry registry;
        try {
            server = new RiskServer();
            registry = null;
            try {
                registry = LocateRegistry.createRegistry(1337);
                System.out.println("registry created");

            } catch (RemoteException e) {

                System.out.println("registry failed");

            }
            registry.rebind(serverName, server);
            System.out.println("server loeppt");
        } catch (RemoteException re) {
            re.printStackTrace();
        }

        try {
            server.readData("/countryList.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startGame() throws RemoteException {
        currentPlayerID = 0;
        Player activePlayer = playerManager.getPlayerList().get(currentPlayerID);
        currentTurn = new Turn(activePlayer, Turn.Phase.DISTRIBUTE);
        notifyPlayers(new GameControlEvent(currentTurn, GameControlEventType.GAME_STARTED));
        System.out.println("game started");
    }

    @Override
    public void saveGame(Player currentPlayer) throws IOException {
        serializeCountries();
        serializePlayers(currentPlayer);
        serializeMissions();
        broadCastText("The Game has been saved by "+currentPlayer.getPlayerName());
    }

    @Override
    public void loadGame() throws IOException, ClassNotFoundException, CountryAlreadyExistsException {

        int counter = 0;
        Vector<Player> loadedPlayerList = new Vector<>(deSerializePlayers());
        Vector<Player> currentPlayerList = getPlayerList();

        if (currentPlayerList.size() == loadedPlayerList.size()) {
            for (Player currentPlayer : currentPlayerList) {
                for (Player loadedPlayer : loadedPlayerList) {
                    if (currentPlayer.getPlayerName().equals(loadedPlayer.getPlayerName())) {
                        counter++;
                    }
                }
            }
        }

        if (counter == currentPlayerList.size()) {
            System.out.println("Players do match... prepare to resume the game!");
            deSerializeCountries();
            writeMissionsFromFile();
            setPlayerList(loadedPlayerList);
            notifyPlayers(new GameControlEvent(new Turn(getPlayerList().get(0), Turn.Phase.SAVE), GameControlEventType.GAME_LOADED));
        } else {
            System.out.println("The connected players do not match. " +
                    "Please retry with another instance of the game!");
        }
    }


    @Override
    public void notifyPlayers(GameEvent g) throws RemoteException {
        for (GameEventListener listener : listeners) {
            // notify every listener in a dedicated thread
            // (a notification should not block another one).
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        listener.handleGameEvent(g);
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }


    public void broadCastText(String s) throws RemoteException {
        for (GameEventListener listener : listeners) {
            // notify every listener in a dedicated thread
            // (a notification should not block another one).
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        listener.broadcast(s);
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }

    @Override
    public void addGameEventListener(GameEventListener listener) throws RemoteException {
        System.out.println("xc");
        listeners.add(listener);
    }

    @Override
    public void removeGameEventListener(GameEventListener listener) throws RemoteException {
        listeners.remove(listener);
    }


    //_____________________
    @Override
    public void setPlayerIDs() throws RemoteException {
        playerManager.setPlayerIDs();
    }

    @Override
    public void readData(String file) throws IOException {
        worldManager.readData(file);
    }


    @Override
    public void serializePlayers(Player p) throws IOException, RemoteException {
        playerManager.serializePlayers(p);
    }

    @Override
    public void serializeMissions() throws IOException, RemoteException {
        missionVerwaltung.serializeMissions();
    }

    @Override
    public void serializeCountries() throws IOException, RemoteException {
        worldManager.serializeCountries();
    }

    @Override
    public Vector<Player> deSerializePlayers() throws IOException, ClassNotFoundException, RemoteException {
        return playerManager.deSerializePlayers();
    }

    @Override
    public void deSerializeCountries() throws IOException, ClassNotFoundException, CountryAlreadyExistsException, RemoteException {
        worldManager.deSerializeCountries();
    }

    @Override
    public void writeMissionsFromFile() throws IOException, ClassNotFoundException, RemoteException {
        Vector<Mission> v = missionVerwaltung.deSerializeMissions();
        missionVerwaltung.overwriteMissions(v);
    }

    @Override
    public boolean createPlayer(int newPlayerID, String newPlayerName) throws PlayerAlreadyExistsException, RemoteException {
        boolean admin = false;

        if (playerManager.getPlayerList().size() < 1) {
            System.out.println("admin created");
            admin = true;
        }
        playerManager.createPlayer(newPlayerID, newPlayerName);

        return admin;
    }

    @Override
    public Vector<customCard> getCardList() throws RemoteException {
        return worldManager.getCardList();
    }

    @Override
    public Vector<Player> getPlayerList() throws RemoteException {
        return playerManager.getPlayerList();
    }


    @Override
    public void setPlayerList(Vector<Player> p) throws RemoteException {
        playerManager.setPlayerList(p);
    }

    @Override
    public void distributeCountries() throws RemoteException {
        worldManager.distributeCountries(playerManager.getPlayerList());
        System.out.println("countries distributed");
    }

    @Override
    public Vector<Country> loadOwnedCountryList(Player player) throws RemoteException {
        return worldManager.loadOwnedCountryList(player);
    }

    @Override
    public Vector<Country> loadOwnedCountryListWithMoreThanOneForce(Player player) throws RemoteException {
        return worldManager.loadOwnedCountryListWithMoreThanOneForce(player);
    }


    @Override
    public Vector<Country> loadNeighbouringCountriesListForDistributionPhase(Country country) throws NoAlliedCountriesNearException, RemoteException {
        return worldManager.loadNeighbouringCountriesListForDistributionPhase(country);
    }

    @Override
    public void createGameFile() throws RemoteException {
        worldManager.createGameFile();
    }

    /*public void writeData ( ) throws IOException {
        worldManager.writeData (playerManager.getPlayerList(), missionVerwaltung.getMissionList());
    }*/

    @Override
    public int returnForcesPerRoundsPerPlayer(Player player) throws RemoteException {//, boolean cards ) {
        return worldManager.returnForcesPerRoundsPerPlayer(player); //, cards );
    }

    @Override
    public void setForcesToCountry(Country country, int forces) throws RemoteException {
        worldManager.setForcesToCountry(country, forces);
    }

    @Override
    public void setOwnerToCountry(Country country, Player player) throws RemoteException {
        worldManager.setOwnerToCountry(country, player);
    }

    @Override
    public Country getCountryByID(int countryID) throws RemoteException {
        return worldManager.getCountryByID(countryID);
    }

    @Override
    public Vector<Country> loadDistributionCountriesList(Player player) throws NoAlliedCountriesNearException, RemoteException {
        return worldManager.loadDistributionCountriesList(player);
    }

    @Override
    public Vector<Country> loadNeighbouringCountriesListForAttackingPhase(Country country) throws NoEnemyCountriesNearException, RemoteException {
        return worldManager.loadNeighbouringCountryListForAttackingPhase(country);
    }

    @Override
    public Vector<Country> loadAttackingCountriesList(Player player) throws NoEnemyCountriesNearException, RemoteException {
        return worldManager.loadAttackingCountriesList(player);
    }

    @Override
    public boolean battle(Country attackingCountry, Country defendingCountry, int attackerForces) throws RemoteException {
        boolean isConquered = false;
        int defendingForces;

        if (defendingCountry.getLocalForces() < 2) {
            defendingForces = 1;
        } else {
            defendingForces = 2;
        }
        int[] forcesArray = compareDice(attackerForces, defendingForces);
        attackerForces -= forcesArray[0];
        defendingForces -= forcesArray[1];
        if (defendingForces < 1) {
            //ATTACKER WINS
            isConquered = true;
            setOwnerToCountry(defendingCountry, attackingCountry.getOwningPlayer());
            setForcesToCountry(defendingCountry, attackerForces);
            setForcesToCountry(attackingCountry, attackingCountry.getLocalForces() - (attackerForces + forcesArray[0]));

            broadCastText(defendingCountry.getOwningPlayerName() + " looses " + forcesArray[1] + ".\n"
                    + attackingCountry.getOwningPlayerName() + " looses " + forcesArray[0] + " forces.\n"
                    + attackingCountry.getOwningPlayerName() + " conquers " + defendingCountry.getCountryName());


            worldManager.distributeCard(attackingCountry.getOwningPlayer());
            GameActionEventType type = GameActionEventType.NEW_OWNER;
            notifyPlayers(new GameActionEvent(currentTurn.getPlayer(), type));
        } else {
            //NO ONE WINS
            setForcesToCountry(attackingCountry, attackingCountry.getLocalForces() - forcesArray[0]);
            setForcesToCountry(defendingCountry, defendingCountry.getLocalForces() - forcesArray[1]);

            broadCastText(defendingCountry.getOwningPlayerName() + " looses " + forcesArray[1] + " forces.\n"
                    + attackingCountry.getOwningPlayerName() + " looses " + forcesArray[0] + " forces.\n"
                    + "But " + attackingCountry.getOwningPlayerName() + " does not conquer "
                    + defendingCountry.getCountryName() + ".");

            GameActionEventType type = GameActionEventType.ATTACK;
            notifyPlayers(new GameActionEvent(currentTurn.getPlayer(), type));
        }

        if (isConquered) {
            boolean missionFullfilled = missionFulfilled(attackingCountry.getOwningPlayer());
            if (missionFullfilled) {
                notifyPlayers(new GameControlEvent(currentTurn, GameControlEventType.GAME_OVER));
            }
        }
        return isConquered;
    }

    @Override
    public void moveForces(Country oldCountry, Country newCountry, int forces) throws RemoteException {
        worldManager.moveForces(oldCountry, newCountry, forces);
    }


    @Override
    public boolean missionFulfilled(Player player) throws RemoteException {
        return missionVerwaltung.missionFulfilled(player, playerManager.getPlayerList(), worldManager.getContinentList());
    }

    @Override
    public void distributeMissions() throws RemoteException {
        missionVerwaltung.distributeMissions(playerManager.getPlayerList());
    }

    @Override
    public Mission getMissionPerPlayer(Player player) throws RemoteException {
        return missionVerwaltung.getMissionPerPlayer(player);
    }

    @Override
    public Vector<customCard> getPlayersCardList(Player player) throws RemoteException {
        return worldManager.getPlayersCardList(player);
    }

    @Override
    public boolean isCardStackFulfilled(Vector<customCard> playersCards) throws RemoteException {
        return worldManager.isCardStackFulfilled(playersCards);
    }

    @Override
    public Country compareHEX(String HEXvalue) throws RemoteException {
        return worldManager.compareHEX(HEXvalue);
    }

    @Override
    public Turn getTurn() throws RemoteException {
        //return playGround.getTurn();
        return null;
    }

    @Override
    public void nextTurn() throws RemoteException {
        Turn.Phase currentPhase = currentTurn.getPhase();
        Turn.Phase nextPhase = currentPhase.next();
        currentTurn.setPhase(nextPhase);
        if (nextPhase == Turn.Phase.DISTRIBUTE) {
            // Back to first phase? Switch players!
            Player nextPlayer = playerManager.getPlayerList().get((++currentPlayerID) % playerManager.getPlayerList().size());
            currentTurn.setPlayer(nextPlayer);
        }
        notifyPlayers(new GameControlEvent(currentTurn, GameControlEvent.GameControlEventType.NEXT_TURN));
    }

    @Override
    public void nextPhase() throws RemoteException {

    }

    @Override
    public Player getCurrentPlayer() throws RemoteException {
        return playerManager.getCurrentPlayer();
    }

    @Override
    public void setNextPlayer() throws RemoteException {
        playerManager.setNextPlayer();
    }

    @Override
    public void startTurn(Player p) throws RemoteException {
        //playGround.startTurn(p);
    }

    @Override
    public Vector<Country> getCountryList() throws RemoteException {
        return worldManager.getCountryList();
    }

    @Override
    public Vector<Country> loadEnemyCountriesList(Player player) throws RemoteException {
        return worldManager.loadEnemyCountryList(player);
    }

    @Override
    public int getNumberOfCountriesOfPlayer(Player player) throws RemoteException {
        return worldManager.getNumberOfCountriesOfPlayer(player);
    }

    public int[] compareDice(int attackerRolls, int defenderRolls) {

        int[] forcesArray = new int[]{0, 0};
        Integer[] attackerDice = new Integer[attackerRolls];
        Integer[] defenderDice = new Integer[defenderRolls];

        for (int i = 0; i < defenderDice.length; i++) {
            defenderDice[i] = (int) (Math.random() * 6) + 1;
        }


        for (int i = 0; i < attackerDice.length; i++) {
            attackerDice[i] = (int) (Math.random() * 6) + 1;
        }


        Arrays.sort(attackerDice, Collections.reverseOrder());
        Arrays.sort(defenderDice, Collections.reverseOrder());

        if (attackerDice.length < defenderDice.length) {
            for (int i = 0, j = 0; i < attackerDice.length; i++) {
                System.out.println("Attacker rolls " + attackerDice[i + 1] + " with the " + (i + 1) + " roll"
                        + " while Defender rolls a " + defenderDice[i + 1] + ".");
                if (attackerDice[i] <= defenderDice[i]) {
                    forcesArray[0]++;
                } else {
                    forcesArray[1]++;
                }
            }

        } else {
            for (int k = 0; k < defenderDice.length; k++) {
                System.out.println("Attacker rolls " + attackerDice[k] + " with the " + (k + 1) + " roll"
                        + " while Defender rolls a " + defenderDice[k] + ".");
                if (attackerDice[k] <= defenderDice[k]) {
                    forcesArray[0]++;
                } else {
                    forcesArray[1]++;
                }
            }
        }
        return forcesArray;
    }
}
