package domain;

import domain.Persistence.FilePersistenceManager;
import domain.Persistence.PersistenceManager;
import valueobjects.Card;
import valueobjects.Country;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class PlayGround {

    private PersistenceManager pm = new FilePersistenceManager ( );

    public PlayGround ( ) {
    }

    // Logical part for attacking/defending and generate random values


    public int[] compareDice ( int attackerRolls, int defenderRolls ) {

        int[] forcesArray = new int[] { 0 , 0 };
        Integer[] attackerDice = new Integer[ attackerRolls ];
        Integer[] defenderDice = new Integer[ defenderRolls ];

        for ( int i = 0 ; i < defenderDice.length ; i++ ) {
            defenderDice[ i ] = rollDice ( );
        }

        for ( int i = 0 ; i < attackerDice.length ; i++ ) {
            attackerDice[ i ] = rollDice ( );
        }

        Arrays.sort ( attackerDice, Collections.reverseOrder ( ) );
        Arrays.sort ( defenderDice, Collections.reverseOrder ( ) );

        if ( attackerDice.length < defenderDice.length ) {
            for ( int i = 0 ; i < attackerDice.length ; i++ ) {
                System.out.println ( "Attacker rolls " + attackerDice[ i ] + " with the " + ( i + 1 ) + " roll"
                        + " while Defender rolls a " + defenderDice[ i ] + "." );
                if ( attackerDice[ i ] <= defenderDice[ i ] ) {
                    forcesArray[ 0 ]++;
                } else {
                    forcesArray[ 1 ]++;
                }
            }

        } else {
            for ( int i = 0 ; i < defenderDice.length ; i++ ) {
                System.out.println ( "Attacker rolls " + attackerDice[ i ] + " with the " + ( i + 1 ) + " roll"
                        + " while Defender rolls a " + defenderDice[ i ] + "." );
                if ( attackerDice[ i ] <= defenderDice[ i ] ) {
                    forcesArray[ 0 ]++;
                } else {
                    forcesArray[ 1 ]++;
                }
            }
        }
        return forcesArray;
    }


    public Integer rollDice ( ) {
        return ( int ) ( Math.random ( ) * 6 + 1 );
    }

    public void battle ( Country attackingCountry, Country defendingCountry, int attackerRolls, int defenderRolls ) {
// KLasse BattleResult (C1, C2, W1, W2, Winner, ...)
        //TODO attacker can use more then 3 forces to attack, but only 3 dices(maybe!)
        /*if ( ! ( attackerRolls < attackingCountry.getLocalForces ( ) && attackerRolls < 4 && attackerRolls > 0 ) ) {
            System.out.println ( "- Attacker may use only up to 3 forces, and keep at least 1 on the country he is attacking from" );
            return false;
        }


        if ( ! ( defenderRolls >= 1 && defenderRolls <= 2 ) ) {
            System.out.println ( "- Defender may use up to 2 forces" );
            return false;
        }*/

        int[] forcesLosses = compareDice ( attackerRolls, defenderRolls );

        attackingCountry.setLocalForces ( attackingCountry.getLocalForces ( ) - forcesLosses[ 0 ] );
        System.out.println ( "Attacking country loses " + forcesLosses[ 0 ] + " and has "
                + attackingCountry.getLocalForces ( ) + " forces remaining." );

        defendingCountry.setLocalForces ( defendingCountry.getLocalForces ( ) - forcesLosses[ 1 ] );
        System.out.println ( "Defending country loses " + forcesLosses[ 1 ] + " and has "
                + defendingCountry.getLocalForces ( ) + " forces remaining." );

       /*
        int defenderLosses = defenderRolls;

        if (attackerRolls < defenderRolls) {
            defenderLosses = attackerRolls;
        }
        if (attackerWins) {
            //attacker wins
            defendingCountry.setLocalForces(defendingCountry.getLocalForces() - defenderLosses);
            System.out.println(attacker.getPlayerName() + " wins!");
        } else if (!attackerWins) {
            //defender Wins

            attackingCountry.setLocalForces(attackingCountry.getLocalForces() - defenderRolls);
            System.out.println(defender.getPlayerName() + " wins!");
        }
*/
        if ( defendingCountry.getLocalForces ( ) < 1 ) {
            //defending Country is conquered
            System.out.println ( "The defending Country " + defendingCountry.getCountryName ( ) + " has lost all its forces." );
            System.out.println ( attackingCountry.getOwningPlayer ( ).getPlayerName ( ) + " is the new owner." );
            defendingCountry.setOwningPlayer ( attackingCountry.getOwningPlayer ( ) );
            defendingCountry.setLocalForces ( attackerRolls - forcesLosses[ 0 ] );
            attackingCountry.setLocalForces ( attackingCountry.getLocalForces ( ) - attackerRolls - forcesLosses[ 0 ] );
            // conquerCountry ( attackingCountry, defendingCountry, attackerRolls );

        }

        //return true;
    }

    public boolean isCardStackFulfilled ( Vector < Card > playerCards ) {
        boolean isFulfilled = false;
        int infantry = 0;
        int cavalry = 0;
        int artillery = 0;
        int joker = 0;

        if ( playerCards.size ( ) < 3 ) {
            for ( Card currentCard : playerCards ) {
                switch ( currentCard.getCardType ( ) ) {
                    case 1:
                        infantry++;
                        break;
                    case 2:
                        cavalry++;
                        break;
                    case 3:
                        artillery++;
                        break;
                    case 4:
                        joker++;
                        break;
                }
                if ( ( infantry > 2 || cavalry > 2 || artillery > 2 )
                        || ( infantry >= 1 && cavalry >= 1 && artillery >= 1 )
                        || ( ( joker >= 1 ) && ( infantry > 1 || cavalry > 1 || artillery > 1 ) )
                        || ( ( joker > 0 ) && ( infantry > 1 || cavalry > 1 || artillery > 1 ) ) ) {
                    isFulfilled = true;
                }
            }
        } else {
            isFulfilled = false;
        }
        return isFulfilled;
    }

}