package domain;


import valueobjects.Continent;
import valueobjects.Mission;
import valueobjects.Missions.ContinentMissions;
import valueobjects.Missions.CountryMissions;
import valueobjects.Missions.PlayerMission;
import valueobjects.Player;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

//import valueobjects.Missions.TakeOverTwoContinents;


public class MissionVerwaltung {

    private Vector<Mission> missionList = new Vector<Mission>();


    public MissionVerwaltung() {
        missionList.removeAllElements();
        missionList.add(new CountryMissions(null, "Conquer 18 Countries of your choice.", 1, 18, 0));
        missionList.add(new CountryMissions(null, "Conquer 12 Countries of your choice with at least 2 forces on each one", 2, 12, 2));
        missionList.add(new ContinentMissions(null, "Conquer Dagobah System and Republic System!", 3, new int[]{1, 4}));
        missionList.add(new ContinentMissions(null, "Conquer The Outer Rim and The Colonies!", 4, new int[]{2, 5}));
        missionList.add(new ContinentMissions(null, "Conquer Delta Quad and The Republic System!", 6, new int[]{3, 4}));
        missionList.add(new ContinentMissions(null, "Conquer The Outer Rim, Delta Quad and a third one of your choice!", 8, new int[]{3, 2, 0}));
        missionList.add(new ContinentMissions(null, "Conquer Dagobah, The Colonies and a third one of your choice!", 9, new int[]{1, 5, 0}));
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

    public void distributeMissions(List<Player> playerList) {
        Vector<Mission> tempMissionList = new Vector<>(missionList);

        Collections.shuffle(missionList);
        int counter = 0;
        for (Player currentPlayer : playerList) {
            tempMissionList.get(counter++).setPlayer(currentPlayer);
        }

    }/*
        for (int i = 0; i < playerList.size(); i++) {

            int randomMission = (int) (Math.random() * missionList.size());

            if ((missionList.get(randomMission).getPlayer() == null)) {

                Mission m = missionList.get(randomMission);
                m.setPlayer(playerList.get(i));

            } else {
                i--;
            }
        }*/


    public void overwriteMissions(Vector<Mission> missionList) {
        this.missionList = missionList;
    }

    public Vector<Mission> getMissionList() {
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
        Vector<Mission> tempMissionList = new Vector<>();
        for (Mission m : missionList)
            if (m.getPlayer() == null) {
                continue;
            } else {
                tempMissionList.add(m);
            }

        return tempMissionList;
    }

    public boolean missionFulfilled(Player player, List<Player> playerList, Vector<Continent> continentList) {

        Mission m = getMissionPerPlayer(player);

        if (m.isFulfilled(player, playerList, continentList)) {
            return true;
        }
        return false;
    }

    public void createPlayerMission(Vector<Player> playerList) {
        // TODO Create dynamic method for various player amount.
        int missionID = 7;
        for (Player p : playerList) {
            new PlayerMission(null, "Defeat " + p.getPlayerName(), missionID++, p.getPlayerID());
        }
    }


}
