package candice.peruserdifficulty.helpers;

import candice.peruserdifficulty.enums.PlayerDifficulty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;

/**
 * Created by Candice on 12/18/2014.
 */

public class NBTHelper
{
    public static final String MOD_NBT = "CandisPerUserDifficulty";
    public static final String DIFFICULTY_NBT = "difficulty";
    public static final String LAST_CHANGED_NBT = "last-changed";
    public static final String SAVED_INVENTORY_NBT = "saved-inventory";
    public static final String HOME_LOCATION_NBT = "home-location";
    public static final String BACK_LOCATION_NBT = "back-location";

    public static final String DIMENSION_NBT = "dim";
    public static final String X_NBT = "x";
    public static final String Y_NBT = "y";
    public static final String Z_NBT = "z";

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

    public static void saveInventory( EntityPlayer player, ArrayList<ItemStack> inventory )
    {
        NBTTagList list = new NBTTagList();

        for( ItemStack stack : inventory )
        {
            NBTTagCompound compound = new NBTTagCompound();
            stack.writeToNBT( compound );
            list.appendTag( compound );
        }

        getModNBT( player ).setTag( SAVED_INVENTORY_NBT, list );
    }

    public static ArrayList<ItemStack> getSavedInventory( EntityPlayer player )
    {
        ArrayList<ItemStack> inventory = new ArrayList<ItemStack>();
        NBTTagList list = getModNBT( player ).getTagList( SAVED_INVENTORY_NBT, Constants.NBT.TAG_COMPOUND );

        for( int x = 0; x < list.tagCount(); x++ )
        {
            NBTTagCompound compound = list.getCompoundTagAt( x );
            inventory.add( ItemStack.loadItemStackFromNBT( compound ) );
        }

        return inventory;
    }

    public static void eraseSavedInventory( EntityPlayer player )
    {
        getModNBT( player ).removeTag( SAVED_INVENTORY_NBT );
    }

    public static void setBackLocation( EntityPlayer player, Location location )
    {
        NBTTagCompound compound = null;

        if( getModNBT( player ).hasKey( BACK_LOCATION_NBT ) )
        {
            compound = getModNBT( player ).getCompoundTag( BACK_LOCATION_NBT );
        }
        else
        {
            compound = new NBTTagCompound();
            getModNBT( player ).setTag( BACK_LOCATION_NBT, compound );
        }

        location.writeToNBT( compound );
    }

    public static Location getBackLocation( EntityPlayer player )
    {
        NBTTagCompound compound = null;

        if( getModNBT( player ).hasKey( BACK_LOCATION_NBT ) )
        {
            compound = getModNBT( player ).getCompoundTag( BACK_LOCATION_NBT );
        }
        else
        {
            return null;
        }

        return Location.readFromNBT( compound );
    }

    public static void eraseBackLocation( EntityPlayer player )
    {
        getModNBT( player ).removeTag( BACK_LOCATION_NBT );
    }

    public static void setHomeLocation( EntityPlayer player, Location location )
    {
        NBTTagList list = null;

        if( getModNBT( player ).hasKey( HOME_LOCATION_NBT ) )
        {
            list = getModNBT( player ).getTagList( HOME_LOCATION_NBT, Constants.NBT.TAG_COMPOUND );
        }
        else
        {
            list = new NBTTagList();
            getModNBT( player ).setTag( HOME_LOCATION_NBT, list );
        }

        boolean home_has_been_set = false;

        for( int i = 0; i < list.tagCount(); i++ )
        {
            NBTTagCompound compound = list.getCompoundTagAt( i );
            int current_dimension = compound.getInteger( DIMENSION_NBT );

            if( current_dimension == location.getDimension() )
            {
                location.writeToNBT( compound );

                home_has_been_set = true;
                break;
            }
        }

        if( !home_has_been_set )
        {
            NBTTagCompound compound = new NBTTagCompound();
            location.writeToNBT( compound );
            list.appendTag( compound );
        }
    }

    public static Location getHomeLocation( EntityPlayer player, int dimension )
    {
        Location home_location = null;
        NBTTagList list = null;

        if( getModNBT( player ).hasKey( HOME_LOCATION_NBT ) )
        {
            list = getModNBT( player ).getTagList( HOME_LOCATION_NBT, Constants.NBT.TAG_COMPOUND );
        }
        else
        {
            return null;
        }

        for( int i = 0; i < list.tagCount(); i++ )
        {
            NBTTagCompound compound = list.getCompoundTagAt( i );
            int current_dimension = compound.getInteger( DIMENSION_NBT );

            if( current_dimension == dimension )
            {
                home_location = Location.readFromNBT( compound );
                break;
            }
        }

        return home_location;
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
