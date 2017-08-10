
import domain.MissionVerwaltung;
import domain.PlayerVerwaltung;
import domain.WorldVerwaltung;
import events.GameControlEvent;
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
import java.util.List;
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

    private List<GameEventListener> listeners;

    private int currentPlayerID;
    private Turn currentTurn;


    public RiskServer() throws RemoteException {
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
        notifyAll();
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

    @Override
    public void addGameEventListener(GameEventListener listener) throws RemoteException {
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
        worldManager.serializePlayers(playerManager.getPlayerList(), p);
    }

    @Override
    public void serializeMissions() throws IOException, RemoteException {
        worldManager.serializeMissions(missionVerwaltung.getUsedMissions());
    }

    @Override
    public void serializeCountries() throws IOException, RemoteException {
        worldManager.serializeCountries();
    }

    @Override
    public Vector<Player> deSerializePlayers() throws IOException, ClassNotFoundException, RemoteException {
        return worldManager.deSerializePlayers();
    }

    @Override
    public void deSerializeCountries() throws IOException, ClassNotFoundException, CountryAlreadyExistsException, RemoteException {
        worldManager.deSerializeCountries();
    }

    @Override
    public void writeMissionsFromFile() throws IOException, ClassNotFoundException, RemoteException {
        Vector<Mission> v = worldManager.deSerializeMissions();
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
    public boolean battle(Country attackingCountry, Country defendingCountry, int attackerForces, int defenderForces) throws RemoteException {
        //return playGround.battle(attackingCountry, defendingCountry, attackerForces, defenderForces);
        return false;
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
        //return playGround.isCardStackFulfilled(playersCards);
        return false;
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
    public void nextTurn(Player p) throws RemoteException {
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
        //playGround.nextPhase();
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

}
