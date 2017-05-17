package domain;

import domain.Persistence.PersistenceManager;
import domain.exceptions.NoEnemyCountriesNearException;
import domain.exceptions.PlayerAlreadyExistsException;
import domain.exceptions.CountryAlreadyExistsException;

import valueobjects.*;

import java.io.IOException;
import java.util.*;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 06.04.2017.
 */
public class Risiko {

    private WorldVerwaltung worldManager;
    private PlayerVerwaltung playerManager;
    private MissionVerwaltung missionVerwaltung;

    private String file = "";


    public Risiko(String file) throws IOException {

        this.file = file;

        playerManager = new PlayerVerwaltung();
        worldManager = new WorldVerwaltung();
        missionVerwaltung = new MissionVerwaltung();

        worldManager.readData(file + ".txt");
    }

    public void createPlayer(int newPlayerID, String newPlayerName, boolean newTurn) throws PlayerAlreadyExistsException {
        playerManager.createPlayer(newPlayerID, newPlayerName, newTurn);
    }

    public List<Player> getPlayerList() {
        return playerManager.getPlayerList();
    }


    public void distributeCountries() {
        worldManager.distributeCountries(playerManager.getPlayerList());
    }

    public Vector<Country> loadOwnedCountryList(Player player) {
        return worldManager.loadOwnedCountryList(player);
    }

    public Vector<Country> loadOwnedCountryListWithMoreThanOneForce(Player player) {
        return worldManager.loadOwnedCountryListWithMoreThanOneForce(player);
    }

    public Vector<Country> loadNeighbouringCountriesListForDistributionPhase(Country country) {
        return worldManager.loadNeighbouringCountriesListForDistributionPhase(country);
    }

    public void createGameFile() {
        worldManager.createGameFile();
    }

    public void writeData( String file ) throws IOException{
        worldManager.writeData( file );
    }


    public int returnForcesPerRoundsPerPlayer(Player player) {
        return worldManager.returnForcesPerRoundsPerPlayer(player);
    }


    public void setForcesToCountry(Country country, int forces) {
        worldManager.setForcesToCountry(country, forces);
    }


    public Country selectNeighbouringCountriesListByNumber(int i) {
        return worldManager.selectNeighbouringCountriesListByNumber(i);
    }

    public Vector<Country> loadNeighbouringCountriesList(Country country) throws NoEnemyCountriesNearException {
        return worldManager.loadNeighbouringCountryListForAttackingPhase(country);
    }

    public Vector<Country> loadAttackingCountriesList(Player player) throws NoEnemyCountriesNearException {
        return worldManager.loadAttackingCountriesList(player);
    }

    public void battle(Country attackingCountry, Country defendingCountry, int attackerForces, int defenderForces) {
        worldManager.battle(attackingCountry, defendingCountry, attackerForces, defenderForces);

    }

    public void moveForces(Country oldCountry, Country newCountry, int forces) {
        worldManager.moveForces(oldCountry, newCountry, forces);
    }


    public boolean playerWon(Player p, boolean b) {
        return worldManager.playerWon(p, b);
    }


    public boolean missionFulfilled(Player player) {
        return missionVerwaltung.missionFullfilled(player, playerManager.getPlayerList(), worldManager.getContinentList());
    }

    public void distributeMissions() {
        missionVerwaltung.distributeMissions(playerManager.getPlayerList());
    }

    public Mission getMissionPerPlayer(Player player) {
        return missionVerwaltung.getMissionPerPlayer(player);
    }

}