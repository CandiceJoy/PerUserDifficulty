package candice.peruserdifficulty;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Candice on 12/18/2014.
 */

public class PlayerDifficultyNBTHelper
{
    public static final String MOD_NBT = "CandisPerUserDifficulty";
    public static final String DIFFICULTY_NBT = "difficulty";
    public static final String LAST_CHANGED_NBT = "last-changed";

    public static PlayerDifficulty getDifficultyLevel( EntityPlayer player )
    {
        return PlayerDifficultyHelper.numberToDifficulty( getModNBT( player ).getInteger( DIFFICULTY_NBT ) );
    }

    public static void setDifficultyLevel( EntityPlayer player, PlayerDifficulty difficulty )
    {
        getModNBT( player ).setInteger( DIFFICULTY_NBT, PlayerDifficultyHelper.difficultyToNumber( difficulty ) );
        updateLastChanged( player );
    }

    public static long getLastChanged( EntityPlayer player )
    {
        return getModNBT( player ).getLong( LAST_CHANGED_NBT );
    }

    private static void updateLastChanged( EntityPlayer player )
    {
        getModNBT( player ).setLong( LAST_CHANGED_NBT, System.currentTimeMillis() );
    }

    private static NBTTagCompound getModNBT( EntityPlayer player )
    {
        NBTTagCompound root_tag = player.getEntityData();
        NBTTagCompound persistent_tag = null;
        NBTTagCompound mod_tag = null;

        if( root_tag.hasKey( EntityPlayer.PERSISTED_NBT_TAG ) )
        {
            persistent_tag = root_tag.getCompoundTag( EntityPlayer.PERSISTED_NBT_TAG );
        }
        else
        {
            persistent_tag = new NBTTagCompound();
            root_tag.setTag( EntityPlayer.PERSISTED_NBT_TAG, persistent_tag );
        }

        if( persistent_tag.hasKey( MOD_NBT ) )
        {
            mod_tag = persistent_tag.getCompoundTag( MOD_NBT );
        }
        else
        {
            mod_tag = new NBTTagCompound();
            persistent_tag.setTag( MOD_NBT, mod_tag );
        }

        return mod_tag;
    }
}
