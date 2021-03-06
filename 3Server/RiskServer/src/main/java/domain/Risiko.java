package domain;


import exceptions.CountryAlreadyExistsException;
import exceptions.NoAlliedCountriesNearException;
import exceptions.NoEnemyCountriesNearException;
import exceptions.PlayerAlreadyExistsException;
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
    //private PlayGround playGround;
    private String file = "";



    public Risiko() throws IOException {

        this.file = file;

        playerManager = new PlayerVerwaltung();
        worldManager = new WorldVerwaltung();
        missionVerwaltung = new MissionVerwaltung();
        //playGround = new PlayGround();
        //worldManager.writeData ( "CountryListTest.txt" );
        //worldManager.createGameFile ( );
        //worldManager.readData ( file );
        //worldManager.writeData ( );

    }

    public void setPlayerIDs(){
        playerManager.setPlayerIDs ();
    }

    public void readData( String file) throws IOException {
        worldManager.readData(file);
    }

    /*public void readGameData (String file) throws IOException {
        worldManager.readGameData (file, playerManager.getPlayerList(), missionVerwaltung.getMissionList());
    }*/

    public void serializePlayers(Player p) throws IOException {
        playerManager.serializePlayers(p);
    }

    public void serializeMissions() throws IOException {
        missionVerwaltung.serializeMissions();
    }

    public void serializeCountries() throws IOException {
        worldManager.serializeCountries();
    }

    public Vector < Player > deSerializePlayers() throws IOException, ClassNotFoundException {
        return playerManager.deSerializePlayers();
    }

    public void deSerializeCountries() throws IOException, ClassNotFoundException, CountryAlreadyExistsException {
        worldManager.deSerializeCountries();
    }

    public void writeMissionsFromFile() throws IOException, ClassNotFoundException {
        Vector<Mission> v = missionVerwaltung.deSerializeMissions();
        missionVerwaltung.overwriteMissions(v);
    }

    public void createPlayer(int newPlayerID, String newPlayerName) throws PlayerAlreadyExistsException {
        playerManager.createPlayer(newPlayerID, newPlayerName);
    }

    public Vector<customCard> getCardList() {
        return worldManager.getCardList();
    }

    public Vector < Player > getPlayerList() {
        return playerManager.getPlayerList();
    }

    public void setPlayerList( Vector < Player > p) {
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
        //return playGround.battle(attackingCountry, defendingCountry, attackerForces, defenderForces);
        return false;
    }

    public void moveForces(Country oldCountry, Country newCountry, int forces) {
        worldManager.moveForces(oldCountry, newCountry, forces);
    }


    public boolean playerWon(Player p, WorldVerwaltung wv, MissionVerwaltung mv, PlayerVerwaltung pv) {
        return false;
        //return playGround.playerWon( p, wv, mv, pv );
    }


    public boolean missionFulfilled(Player player) {
        return missionVerwaltung.missionFulfilled (player, playerManager.getPlayerList(), worldManager.getContinentList());
    }

    public void distributeMissions() {
        missionVerwaltung.distributeMissions(playerManager.getPlayerList());
    }

    public Mission getMissionPerPlayer(Player player) {
        return missionVerwaltung.getMissionPerPlayer(player);
    }

    public Vector<customCard> getPlayersCardList(Player player) {
        return worldManager.getPlayersCardList(player);
    }

    public boolean isCardStackFulfilled(Vector<customCard> playersCards) {
        //return playGround.isCardStackFulfilled(playersCards);
        return false;
    }

    public Country compareHEX(String HEXvalue) {
        return worldManager.compareHEX(HEXvalue);
    }

    public Turn getTurn() {
        //return playGround.getTurn();
        return null;
    }

    public void nextTurn(Player p) {
        //playGround.nextTurn(p);
    }

    public void nextPhase() {
        //playGround.nextPhase();
    }

    public Player getCurrentPlayer() {
        return playerManager.getCurrentPlayer();
    }

    public void setNextPlayer() {
        playerManager.setNextPlayer();
    }

    public void startTurn(Player p) {
        //playGround.startTurn(p);
    }

    public Vector<Country> getCountryList() {
        Vector<Country> countryList = new Vector<>();


        for (Continent continent : worldManager.getContinentList()) {
            for (Country country : continent.getContinentCountries()) {
                countryList.add(country);
            }
        }
        return countryList;
    }

    public Vector<Country> loadEnemyCountriesList( Player player ){
        return worldManager.loadEnemyCountryList ( player );
    }

    public int getNumberOfCountriesOfPlayer(Player player){
        return worldManager.getNumberOfCountriesOfPlayer ( player );
    }
}