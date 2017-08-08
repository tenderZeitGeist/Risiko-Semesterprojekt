import exceptions.CountryAlreadyExistsException;
import exceptions.NoAlliedCountriesNearException;
import exceptions.NoEnemyCountriesNearException;
import exceptions.PlayerAlreadyExistsException;
import valueobjects.Country;
import valueobjects.Mission;
import valueobjects.Player;
import valueobjects.Turn;

import javax.smartcardio.Card;
import java.io.IOException;
import java.rmi.Remote;
import java.util.Vector;


/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 07.08.2017.
 */
interface RemoteRisk extends Remote {

    void setPlayerIDs();

    void readData(String file) throws IOException;

    void serializePlayers(Player p) throws IOException;

    void serializeMissions() throws IOException;

    void serializeCountries() throws IOException;

    Vector<Player> deSerializePlayers() throws IOException, ClassNotFoundException;

    void deSerializeCountries() throws IOException, ClassNotFoundException, CountryAlreadyExistsException;

    void writeMissionsFromFile() throws IOException, ClassNotFoundException;

    void createPlayer(int newPlayerID, String newPlayerName) throws PlayerAlreadyExistsException;

    Vector<Card> getCardList();

    Vector<Player> getPlayerList();

    void setPlayerList(Vector<Player> p);

    void distributeCountries();

    Vector<Country> loadOwnedCountryList(Player player);

    Vector<Country> loadOwnedCountryListWithMoreThanOneForce(Player player);

    Vector<Country> loadNeighbouringCountriesListForDistributionPhase(Country country) throws NoAlliedCountriesNearException;

    void createGameFile();

    int returnForcesPerRoundsPerPlayer(Player player);

    void setForcesToCountry(Country country, int forces);

    Vector<Country> loadDistributionCountriesList(Player player) throws NoAlliedCountriesNearException;

    Vector<Country> loadNeighbouringCountriesListForAttackingPhase(Country country) throws NoEnemyCountriesNearException;

    Vector<Country> loadAttackingCountriesList(Player player) throws NoEnemyCountriesNearException;

    boolean battle(Country attackingCountry, Country defendingCountry, int attackerForces, int defenderForces);

    void moveForces(Country oldCountry, Country newCountry, int forces);

    //boolean playerWon(Player p, WorldVerwaltung wv, MissionVerwaltung mv, PlayerVerwaltung pv);

    boolean missionFulfilled(Player player);

    void distributeMissions();

    Mission getMissionPerPlayer(Player player);

    Vector<Card> getPlayersCardList(Player player);

    boolean isCardStackFulfilled(Vector<Card> playersCards);

    Country compareHEX(String HEXvalue);

    Turn getTurn();

    void nextTurn(Player p);

    void nextPhase();

    Player getCurrentPlayer();

    void setNextPlayer();

    void startTurn(Player p);

    Vector<Country> getCountryList();

    Vector<Country> loadEnemyCountriesList(Player player);

    int getNumberOfCountriesOfPlayer(Player player);

}

