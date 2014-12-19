package candice.peruserdifficulty;

/**
 * Created by Candice on 12/18/2014.
 */
public class PlayerDifficultyHelper
{
    public static int difficultyToNumber( PlayerDifficulty difficulty )
    {
        switch( difficulty )
        {
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
        double damage_multiplier = 1.0;

        switch( difficulty )
        {
            case EASY:
                damage_multiplier = 0.5;
                break;
            case MEDIUM:
                damage_multiplier = 1.0;
                break;
            case HARD:
                damage_multiplier = 2.0;
                break;
        }

        return damage_multiplier;
    }

    public static double getDamageDealtMultiplier( PlayerDifficulty difficulty )
    {
        double damage_multiplier = 1.0;

        switch( difficulty )
        {
            case EASY:
                damage_multiplier = 2.0;
                break;
            case MEDIUM:
                damage_multiplier = 1.0;
                break;
            case HARD:
                damage_multiplier = 0.5;
                break;
        }

        return damage_multiplier;
    }
}
