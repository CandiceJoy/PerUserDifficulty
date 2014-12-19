package candice.peruserdifficulty;

import net.minecraftforge.event.world.NoteBlockEvent;

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
}
