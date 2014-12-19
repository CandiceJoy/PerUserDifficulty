package candice.peruserdifficulty;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Candice on 12/18/2014.
 */
public class PlayerDifficultyList
{
    private static HashMap<UUID,PlayerDifficulty> difficulty_list;

    static
    {
        difficulty_list = new HashMap<UUID,PlayerDifficulty>();
    }

    public static PlayerDifficulty getPlayerDifficulty( UUID uuid )
    {
        return difficulty_list.get( uuid );
    }

    public static void setPlayerDifficulty( UUID uuid, PlayerDifficulty difficulty )
    {
        difficulty_list.put( uuid, difficulty );
    }
}
