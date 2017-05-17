package domain;


import valueobjects.*;
//import valueobjects.Missions.ContinentMissions;
import valueobjects.Missions.CountryMissions;

import java.util.Vector;
import java.util.List;


public class MissionVerwaltung {

    private Vector<Mission> missionList = new Vector<Mission>();


    public MissionVerwaltung() {
        missionList.removeAllElements();
        missionList.add(new CountryMissions(null,"Conquer 24 Countries of your choice.", 1, 10, 0 ));
        missionList.add(new CountryMissions(null,"Conquer 18 Countries of your choice with at least 2 forces on each one", 2, 18, 2));
        /*missionList.add(new ContinentMissions(null,"Conquer Asia and Africa!", 3, 5,4));
        missionList.add(new ContinentMissions(null,"Conquer Asia and South-America!", 3, 5,2));
        missionList.add(new ContinentMissions(null,"Conquer North-America and Australia!", 3, 1,6));
        missionList.add(new ContinentMissions(null,"Conquer North-America and Africa!", 3, 1,4));
*/
    }

    /*
    -Europa, Australien und einen 3. Kontinent Ihrer Wahl
-Europa, Südamerika und einen 3. Kontinent Ihrer Wahl
-Europa, Afrika und einen 3. Kontinent Ihrer Wahl

- alle Länder von den roten Armeen
- alle Länder von den gelben Armeen
- alle Länder von den blauen Armeen
- alle Länder von den rosafarbenen Armeen
- alle Länder von den schwarzen Armeen
- alle Länder von den grünen Armeen
    __________________*/



    public void distributeMissions(List<Player> playerList) {

        for (int i = 0; i < playerList.size(); i++) {

            int randomMission = (int) (Math.random() * missionList.size());

            if ((missionList.get(randomMission).getPlayer() == null)) {

                Mission m = missionList.get(randomMission);
                m.setPlayer(playerList.get(i));

            } else {
                i--;
            }
        }
    }


    public Vector<Mission> getMissionList() {
        return missionList;
    }

    public Mission getMissionPerPlayer(Player player) {

        for (Mission m : missionList) {
            if (m.getPlayer().equals(player)) {
                return m;
            }
        }

        return null;
    }

    public boolean missionFullfilled(Player player, List<Player> playerList, Vector<Continent> continentList) {

        Mission m = getMissionPerPlayer(player);

        if (m.isFulfilled(player, playerList, continentList)) {
            return true;
        }
        return false;
    }


}
