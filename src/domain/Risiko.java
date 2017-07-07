package domain;

import domain.exceptions.NoAlliedCountriesNearException;
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
    private PlayGround playGround;
    private String file = "";



    public Risiko() throws IOException {

        this.file = file;

        playerManager = new PlayerVerwaltung();
        worldManager = new WorldVerwaltung();
        missionVerwaltung = new MissionVerwaltung();
        playGround = new PlayGround();
        //worldManager.writeData ( "CountryListTest.txt" );
        //worldManager.createGameFile ( );
        //worldManager.readData ( file );
        //worldManager.writeData ( );

    }

    public void readData(String file) throws IOException {
        worldManager.readData(file);
    }

    /*public void readGameData (String file) throws IOException {
        worldManager.readGameData (file, playerManager.getPlayerList(), missionVerwaltung.getMissionList());
    }*/

    public void serializePlayers(Player p) throws IOException {
        worldManager.serializePlayers(playerManager.getPlayerList(), p);
    }

    public void serializeMissions() throws IOException {
        worldManager.serializeMissions(missionVerwaltung.getUsedMissions());
    }

    public void serializeCountries() throws IOException {
        worldManager.serializeCountries();
    }

    public List<Player> deSerializePlayers() throws IOException, ClassNotFoundException {
        return worldManager.deSerializePlayers();
    }

    public void deSerializeCountries() throws IOException, ClassNotFoundException, CountryAlreadyExistsException {
        worldManager.deSerializeCountries();
    }

    public void writeMissionsFromFile() throws IOException, ClassNotFoundException {
        Vector<Mission> v = worldManager.deSerializeMissions();
        missionVerwaltung.overwriteMissions(v);
    }

    public void createPlayer(int newPlayerID, String newPlayerName) throws PlayerAlreadyExistsException {
        playerManager.createPlayer(newPlayerID, newPlayerName);
    }

    public Vector<Card> getCardList() {
        return worldManager.getCardList();
    }

    public List<Player> getPlayerList() {
        return playerManager.getPlayerList();
    }

    public void setPlayerList(List<Player> p) {
        playerManager.setPlayerList(p);
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

    public Vector<Country> loadNeighbouringCountriesListForDistributionPhase(Country country) throws NoAlliedCountriesNearException {
        return worldManager.loadNeighbouringCountriesListForDistributionPhase(country);
    }

    public void createGameFile() {
        worldManager.createGameFile();
    }

    /*public void writeData ( ) throws IOException {
        worldManager.writeData (playerManager.getPlayerList(), missionVerwaltung.getMissionList());
    }*/


    public int returnForcesPerRoundsPerPlayer(Player player) {//, boolean cards ) {
        return worldManager.returnForcesPerRoundsPerPlayer(player); //, cards );
    }


    public void setForcesToCountry(Country country, int forces) {
        worldManager.setForcesToCountry(country, forces);
    }

    public Vector<Country> loadDistributionCountriesList(Player player) throws NoAlliedCountriesNearException {
        return worldManager.loadDistributionCountriesList(player);
    }

    public Vector<Country> loadNeighbouringCountriesListForAttackingPhase(Country country) throws NoEnemyCountriesNearException {
        return worldManager.loadNeighbouringCountryListForAttackingPhase(country);
    }

    public Vector<Country> loadAttackingCountriesList(Player player) throws NoEnemyCountriesNearException {
        return worldManager.loadAttackingCountriesList(player);
    }

    public boolean battle(Country attackingCountry, Country defendingCountry, int attackerForces, int defenderForces) {
        return playGround.battle(attackingCountry, defendingCountry, attackerForces, defenderForces);

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

    public Vector<Card> getPlayersCardList(Player player) {
        return worldManager.getPlayersCardList(player);
    }

    public boolean isCardStackFulfilled(Vector<Card> playersCards) {
        return playGround.isCardStackFulfilled(playersCards);
    }

    public Country compareHEX(String HEXvalue) {
        return worldManager.compareHEX(HEXvalue);
    }

    public Turn getTurn() {
        return playGround.getTurn();
    }

    public void nextTurn(Player p) {
        playGround.nextTurn(p);
    }

    public void nextPhase() {
        playGround.nextPhase();
    }
}