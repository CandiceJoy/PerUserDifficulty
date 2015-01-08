package candice.peruserdifficulty.helpers;

import candice.peruserdifficulty.enums.PlayerDifficulty;

import java.util.Arrays;

/**
 * Created by Candice on 12/18/2014.
 */
public class PlayerDifficultyHelper
{
    private static double[] damage_taken;
    private static double[] damage_dealt;
    private static double[] food;
    private static double[] saturation;
    private static PlayerDifficulty max_keepinventory;
    private static PlayerDifficulty max_home_command;
    private static PlayerDifficulty max_back_command;

    public static void setConfig( double[] damage_taken_in, double[] damage_dealt_in, double[] food_in, double[] saturation_in, PlayerDifficulty max_keepinventory_in, PlayerDifficulty max_home_command_in, PlayerDifficulty max_back_command_in )
    {
        if( damage_taken_in.length != 3 || damage_dealt_in.length != 3 || food_in.length != 3 || saturation_in.length != 3 )
        {
            return;
        }

        damage_taken = damage_taken_in;
        damage_dealt = damage_dealt_in;
        food = food_in;
        saturation = saturation_in;
        max_keepinventory = max_keepinventory_in;
        max_home_command = max_home_command_in;
        max_back_command = max_back_command_in;
    }

    public static PlayerDifficulty stringToDifficulty( String difficulty_string )
    {
        PlayerDifficulty difficulty = null;
        difficulty_string = difficulty_string.toLowerCase().trim();

        if( difficulty_string.equals( "disabled" ) )
        {
            difficulty = PlayerDifficulty.DISABLED;
        }
        else if( difficulty_string.equals( "easy" ) )
        {
            difficulty = PlayerDifficulty.EASY;
        }
        else if( difficulty_string.equals( "medium" ) || difficulty_string.equals( "med" ) )
        {
            difficulty = PlayerDifficulty.MEDIUM;
        }
        else if( difficulty_string.equals( "hard" ) )
        {
            difficulty = PlayerDifficulty.HARD;
        }

        return difficulty;
    }

    public static int difficultyToNumber( PlayerDifficulty difficulty )
    {
        return Arrays.binarySearch( PlayerDifficulty.values(), difficulty );
    }

    public static PlayerDifficulty numberToDifficulty( int difficulty )
    {
        if( difficulty < 0 || difficulty >= PlayerDifficulty.values().length )
        {
            return null;
        }
        else
        {
            return PlayerDifficulty.values()[difficulty];
        }
    }

    public static double getDamageTakenMultiplier( PlayerDifficulty difficulty )
    {
        if( damage_taken == null || difficulty == PlayerDifficulty.DISABLED )
        {
            return 1.0;
        }

        return damage_taken[difficultyToNumber( difficulty ) - 1];
    }

    public static double getDamageDealtMultiplier( PlayerDifficulty difficulty )
    {
        if( damage_dealt == null || difficulty == PlayerDifficulty.DISABLED )
        {
            return 1.0;
        }

        return damage_dealt[difficultyToNumber( difficulty ) - 1];
    }

    public static double getFoodMultiplier( PlayerDifficulty difficulty )
    {
        if( food == null || difficulty == PlayerDifficulty.DISABLED )
        {
            return 1.0;
        }

        return food[difficultyToNumber( difficulty ) - 1];
    }

    public static double getSaturationMultiplier( PlayerDifficulty difficulty )
    {
        if( saturation == null || difficulty == PlayerDifficulty.DISABLED )
        {
            return 1.0;
        }

        return saturation[difficultyToNumber( difficulty ) - 1];
    }

    public static boolean shouldDoKeepInventory( PlayerDifficulty difficulty )
    {
        return difficultyToNumber( difficulty ) <= difficultyToNumber( max_keepinventory );
    }

    public static boolean shouldAllowHomeCommand( PlayerDifficulty difficulty )
    {
        return difficultyToNumber( difficulty ) <= difficultyToNumber( max_home_command );
    }

    public static boolean shouldAllowBackCommand( PlayerDifficulty difficulty )
    {
        return difficultyToNumber( difficulty ) <= difficultyToNumber( max_back_command );
    }
}
