package candice.peruserdifficulty;

/**
 * Created by Candice on 12/18/2014.
 */
public class PlayerDifficultyHelper
{
    private static double[] damage_taken;
    private static double[] damage_dealt;
    private static PlayerDifficulty max_keepinventory;

    public static void setConfig( double[] damage_taken_in, double[] damage_dealt_in, PlayerDifficulty max_keepinventory_in )
    {
        if( damage_taken_in.length != 3 || damage_dealt_in.length != 3 )
        {
            return;
        }

        damage_taken = damage_taken_in;
        damage_dealt = damage_dealt_in;
        max_keepinventory = max_keepinventory_in;
    }

    public static int difficultyToNumber( PlayerDifficulty difficulty )
    {
        switch( difficulty )
        {
            case DISABLED:
                return 0;
            case EASY:
                return 1;
            case MEDIUM:
                return 2;
            case HARD:
                return 3;
            default:
                return -1;
        }
    }

    public static PlayerDifficulty numberToDifficulty( int difficulty )
    {
        switch( difficulty )
        {
            case 0:
                return PlayerDifficulty.DISABLED;
            case 1:
                return PlayerDifficulty.EASY;
            case 2:
                return PlayerDifficulty.MEDIUM;
            case 3:
                return PlayerDifficulty.HARD;
            default:
                return null;
        }
    }

    public static double getDamageTakenMultiplier( PlayerDifficulty difficulty )
    {
        if( damage_taken == null || difficulty == PlayerDifficulty.DISABLED )
        {
            return 1.0;
        }

        double damage_multiplier = 1.0;

        switch( difficulty )
        {
            case EASY:
                damage_multiplier = damage_taken[0];
                break;
            case MEDIUM:
                damage_multiplier = damage_taken[1];
                break;
            case HARD:
                damage_multiplier = damage_taken[2];
                break;
        }

        return damage_multiplier;
    }

    public static double getDamageDealtMultiplier( PlayerDifficulty difficulty )
    {
        if( damage_dealt == null || difficulty == PlayerDifficulty.DISABLED )
        {
            return 1.0;
        }

        double damage_multiplier = 1.0;

        switch( difficulty )
        {
            case EASY:
                damage_multiplier = damage_dealt[0];
                break;
            case MEDIUM:
                damage_multiplier = damage_dealt[1];
                break;
            case HARD:
                damage_multiplier = damage_dealt[2];
                break;
        }

        return damage_multiplier;
    }

    public static boolean shouldDoKeepInventory( PlayerDifficulty difficulty )
    {
        return difficultyToNumber( difficulty ) <= difficultyToNumber( max_keepinventory );
    }
}
