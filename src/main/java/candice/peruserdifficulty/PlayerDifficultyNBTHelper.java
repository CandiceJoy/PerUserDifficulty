package candice.peruserdifficulty;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;

/**
 * Created by Candice on 12/18/2014.
 */
public class PlayerDifficultyNBTHelper
{
    public static final String MOD_NBT = "candi-per-user-difficulty";
    public static final String DIFFICULTY_NBT = "difficulty";
    public static final String LAST_CHANGED_NBT = "last-changed";

    public static PlayerDifficulty getDifficultyLevel( EntityPlayer player )
    {
        //int difficulty_number = player.getEntityData().getCompoundTag( player.PERSISTED_NBT_TAG ).getInteger( DIFFICULTY_NBT );
        NBTTagCompound persistent = player.getEntityData().getCompoundTag( EntityPlayer.PERSISTED_NBT_TAG );
        NBTTagCompound mod_tag = persistent.getCompoundTag( MOD_NBT );
        int difficulty_number = mod_tag.getInteger( DIFFICULTY_NBT );

        return PlayerDifficultyHelper.numberToDifficulty( difficulty_number );
    }

    public static void setDifficultyLevel( EntityPlayer player, PlayerDifficulty difficulty )
    {
        NBTTagCompound persistent = player.getEntityData().getCompoundTag( EntityPlayer.PERSISTED_NBT_TAG );
        NBTTagCompound mod_tag = persistent.getCompoundTag( MOD_NBT );
        mod_tag.setInteger( DIFFICULTY_NBT, PlayerDifficultyHelper.difficultyToNumber( difficulty ) );
        //player.getEntityData().getCompoundTag( player.PERSISTED_NBT_TAG ).setInteger( DIFFICULTY_NBT, PlayerDifficultyHelper.difficultyToNumber( difficulty ) );
        updateLastChanged( player );
    }

    public static long getLastChanged( EntityPlayer player )
    {
        NBTTagCompound persistent = player.getEntityData().getCompoundTag( EntityPlayer.PERSISTED_NBT_TAG );
        NBTTagCompound mod_tag = persistent.getCompoundTag( MOD_NBT );
        long last_changed = mod_tag.getLong( LAST_CHANGED_NBT );
        //long last_changed = player.getEntityData().getCompoundTag( player.PERSISTED_NBT_TAG ).getLong( LAST_CHANGED_NBT );

        return last_changed;
    }

    private static void updateLastChanged( EntityPlayer player )
    {
        NBTTagCompound persistent = player.getEntityData().getCompoundTag( EntityPlayer.PERSISTED_NBT_TAG );
        NBTTagCompound mod_tag = persistent.getCompoundTag( MOD_NBT );
        mod_tag.setLong( LAST_CHANGED_NBT, System.currentTimeMillis() );

        //player.getEntityData().getCompoundTag( player.PERSISTED_NBT_TAG ).setLong( LAST_CHANGED_NBT, System.currentTimeMillis() );
    }
}
