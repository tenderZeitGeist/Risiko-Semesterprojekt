package domain;


import valueobjects.*;
//import valueobjects.Missions.TakeOverTwoContinents;
import valueobjects.Missions.*;

import java.util.Vector;
import java.util.List;


public class MissionVerwaltung {

    private Vector < Mission > missionList = new Vector < Mission > ( );


    public MissionVerwaltung ( ) {
        missionList.removeAllElements ( );
        missionList.add ( new CountryMissions ( null, "Conquer 24 Countries of your choice.", 1, 24, 0 ) );
        missionList.add ( new CountryMissions ( null, "Conquer 18 Countries of your choice with at least 2 forces on each one", 2, 18, 2 ) );
        missionList.add ( new ContinentMissions ( null, "Conquer Asia and Africa!", 3, new int[] { 4 , 5 } ) );
        missionList.add ( new ContinentMissions ( null, "Conquer Asia and South-America!", 4, new int[] { 2 , 5 } ) );
        missionList.add ( new ContinentMissions ( null, "Conquer North-America and Australia!", 5, new int[] { 1 , 7 } ) );
        missionList.add ( new ContinentMissions ( null, "Conquer North-America and Africa!", 6, new int[] { 1 , 4 } ) );
        missionList.add ( new ContinentMissions ( null, "Conquer Europe, Australia and a third one of your choice!", 7, new int[] { 3 , 6 , 0 } ) );
        missionList.add ( new ContinentMissions ( null, "Conquer Europe, South-America and a third one of your choice!", 8, new int[] { 3 , 2 , 0 } ) );
        missionList.add ( new ContinentMissions ( null, "Conquer Europe, Africa and a third one of your choice!", 9, new int[] { 3 , 4 , 0 } ) );
/*      missionList.add(new KillPlayerMissions(null, "Capture all countries of player 1", 10, 1));
        missionList.add(new KillPlayerMissions(null, "Capture all countries of player 2", 11, 2));
        missionList.add(new KillPlayerMissions(null, "Capture all countries of player 3", 12, 3));
        missionList.add(new KillPlayerMissions(null, "Capture all countries of player 4", 13, 4));
        missionList.add(new KillPlayerMissions(null, "Capture all countries of player 5", 14, 5));
        missionList.add(new KillPlayerMissions(null, "Capture all countries of player 6", 15, 6));*/
    }

    /*

- alle Länder von den roten Armeen
- alle Länder von den gelben Armeen
- alle Länder von den blauen Armeen
- alle Länder von den rosafarbenen Armeen
- alle Länder von den schwarzen Armeen
- alle Länder von den grünen Armeen
    __________________*/

    public void distributeMissions ( List < Player > playerList ) {

        for ( int i = 0 ; i < playerList.size ( ) ; i++ ) {

            int randomMission = ( int ) ( Math.random ( ) * missionList.size ( ) );

            if ( ( missionList.get ( randomMission ).getPlayer ( ) == null ) ) {

                Mission m = missionList.get ( randomMission );
                m.setPlayer ( playerList.get ( i ) );

            } else {
                i--;
            }
        }
    }

    public void overwriteMissions(Vector<Mission> missionList) {
        this.missionList = missionList;
    }

    public Vector < Mission > getMissionList ( ) {
        return missionList;
    }

    public Mission getMissionPerPlayer(Player player) {

        for (int i = 0; i < missionList.size(); i++) {
            if ((missionList.get(i).getPlayer() == null || !missionList.get(i).getPlayer().equals(player))) {
                continue;
            } else if (missionList.get(i).getPlayer().equals(player)) {
                return missionList.get(i);
            }
        }
        return null;
    }

    public Vector<Mission> getUsedMissions() {
        Vector <Mission> tempMissionList = new Vector<>();
        for (Mission m : missionList)
            if (m.getPlayer() == null) {
                continue;
            } else {
                tempMissionList.add(m);
            }

        return tempMissionList;
    }

    public boolean missionFullfilled ( Player player, List < Player > playerList, Vector < Continent > continentList ) {

        Mission m = getMissionPerPlayer ( player );

        if ( m.isFulfilled ( player, playerList, continentList ) ) {
            return true;
        }
        return false;
    }

    public void createPlayerMission ( Vector < Player > playerList ) {
        // TODO Create dynamic method for various player amount.
        int missionID = 7;
        for ( Player p : playerList ) {
            new PlayerMission ( null, "Defeat " + p.getPlayerName ( ), missionID++, p.getPlayerID ( ) );
        }
    }

}
