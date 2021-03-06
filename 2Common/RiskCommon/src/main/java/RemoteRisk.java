import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import events.GameEvent;
import exceptions.CountryAlreadyExistsException;
import exceptions.NoAlliedCountriesNearException;
import exceptions.NoEnemyCountriesNearException;
import exceptions.PlayerAlreadyExistsException;
import valueobjects.*;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;


/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 07.08.2017.
 */
interface RemoteRisk extends Remote {




    void notifyPlayers(GameEvent g) throws RemoteException;

    void startGame() throws RemoteException;

    void loadGame() throws IOException, ClassNotFoundException, CountryAlreadyExistsException;

    void saveGame(Player currentPlayer) throws IOException ;

    void addGameEventListener(GameEventListener listener) throws RemoteException;

    void removeGameEventListener(GameEventListener listener) throws RemoteException;

    void setPlayerIDs() throws RemoteException;

    void readData(String file) throws IOException;

    void serializePlayers(Player p) throws IOException, RemoteException;

    void serializeMissions() throws IOException, RemoteException;

    void serializeCountries() throws IOException, RemoteException;

    Vector<Player> deSerializePlayers() throws IOException, ClassNotFoundException, RemoteException;

    void deSerializeCountries() throws IOException, ClassNotFoundException, CountryAlreadyExistsException, RemoteException;

    void writeMissionsFromFile() throws IOException, ClassNotFoundException, RemoteException;

    boolean createPlayer(int newPlayerID, String newPlayerName) throws PlayerAlreadyExistsException, RemoteException;

    Vector<customCard> getCardList() throws RemoteException;

    Vector<Player> getPlayerList() throws RemoteException;

    void setPlayerList(Vector<Player> p) throws RemoteException;

    void distributeCountries() throws RemoteException;

    Vector<Country> loadOwnedCountryList(Player player) throws RemoteException;

    Vector<Country> loadOwnedCountryListWithMoreThanOneForce(Player player) throws RemoteException;

    Vector<Country> loadNeighbouringCountriesListForDistributionPhase(Country country) throws NoAlliedCountriesNearException, RemoteException;

    void createGameFile() throws RemoteException;

    void writeData() throws IOException;

    int returnForcesPerRoundsPerPlayer(Player player) throws RemoteException;

    void setForcesToCountry(Country country, int forces) throws RemoteException;

    void setOwnerToCountry(Country country, Player player) throws RemoteException;

    Country getCountryByID(int countryID) throws RemoteException;

    Vector<Country> loadDistributionCountriesList(Player player) throws NoAlliedCountriesNearException, RemoteException;

    Vector<Country> loadNeighbouringCountriesListForAttackingPhase(Country country) throws NoEnemyCountriesNearException, RemoteException;

    Vector<Country> loadAttackingCountriesList(Player player) throws NoEnemyCountriesNearException, RemoteException;

    boolean battle(Country attackingCountry, Country defendingCountry, int attackerForces) throws RemoteException;

    void moveForces(Country oldCountry, Country newCountry, int forces) throws RemoteException;

    //boolean playerWon(Player p, WorldVerwaltung wv, MissionVerwaltung mv, PlayerVerwaltung pv);

    boolean missionFulfilled(Player player) throws RemoteException;

    void distributeMissions() throws RemoteException;

    Mission getMissionPerPlayer(Player player) throws RemoteException;

    Vector<customCard> getPlayersCardList(Player player) throws RemoteException;

    boolean isCardStackFulfilled(Vector<customCard> playersCards) throws RemoteException;

    Country compareHEX(String HEXvalue) throws RemoteException;

    Turn getTurn() throws RemoteException;

    void nextTurn() throws RemoteException;

    void nextPhase() throws RemoteException;

    Player getCurrentPlayer() throws RemoteException;

    void setNextPlayer() throws RemoteException;

    void startTurn(Player p) throws RemoteException;

    Vector<Country> getCountryList() throws RemoteException;

    Vector<Country> loadEnemyCountriesList(Player player) throws RemoteException;

    int getNumberOfCountriesOfPlayer(Player player) throws RemoteException;

}

