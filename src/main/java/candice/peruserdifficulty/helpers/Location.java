package candice.peruserdifficulty.helpers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by Candice on 1/8/2015.
 */
public class Location
{
    public static final int SEARCH_RADIUS = 1;

    private int dimension;
    private double x;
    private double y;
    private double z;

    public Location( int dimension_in, double x_in, double y_in, double z_in )
    {
        dimension = dimension_in;
        x = x_in;
        y = y_in;
        z = z_in;
    }

    public Location( EntityPlayer player )
    {
        dimension = player.getEntityWorld().provider.dimensionId;
        x = player.posX;
        y = player.posY;
        z = player.posZ;
    }

    public static Location readFromNBT( NBTTagCompound compound )
    {
        int dimension_temp = compound.getInteger( NBTHelper.DIMENSION_NBT );
        double x_temp = compound.getDouble( NBTHelper.X_NBT );
        double y_temp = compound.getDouble( NBTHelper.Y_NBT );
        double z_temp = compound.getDouble( NBTHelper.Z_NBT );

        return new Location( dimension_temp, x_temp, y_temp, z_temp );
    }

    public int getDimension()
    {
        return dimension;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }

    public boolean sendPlayerTo( EntityPlayer player )
    {
        if( player.worldObj.isRemote )
        {
            return false;
        }

        int old_dim = player.dimension;
        double old_x = player.posX;
        double old_y = player.posY;
        double old_z = player.posZ;

        if( player.getEntityWorld().provider.dimensionId != dimension )
        {
            player.travelToDimension( dimension );
        }

        int x_as_int = (int) Math.round( x );
        int y_as_int = (int) Math.round( y );
        int z_as_int = (int) Math.round( z );
        boolean proper_location_found = false;

        if( !isValidTeleportLocation( player.worldObj, x_as_int, y_as_int, z_as_int ) )
        {
            for( int a = x_as_int - SEARCH_RADIUS; a <= x_as_int + SEARCH_RADIUS && !proper_location_found; a++ )
            {
                for( int b = y_as_int - SEARCH_RADIUS; b <= y_as_int + SEARCH_RADIUS && !proper_location_found; b++ )
                {
                    for( int c = z_as_int - SEARCH_RADIUS; c <= z_as_int + SEARCH_RADIUS && !proper_location_found; c++ )
                    {
                        if( isValidTeleportLocation( player.worldObj, a, b, c ) )
                        {
                            x_as_int = a;
                            y_as_int = b;
                            z_as_int = c;
                            proper_location_found = true;
                        }
                    }
                }
            }
        }
        else
        {
            proper_location_found = true;
        }

        if( !proper_location_found )
        {
            player.travelToDimension( old_dim );
            player.setPositionAndUpdate( old_x, old_y, old_z );

            return false;
        }

        player.setPositionAndUpdate( x_as_int, y_as_int, z_as_int );
        return true;
    }

    private boolean isValidTeleportLocation( World world, int x_in, int y_in, int z_in )
    {
        if( hasRoomForPlayer( world, x_in, y_in, z_in ) && isLocationSafe( world, x_in, y_in, z_in ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean hasRoomForPlayer( World world, int x_in, int y_in, int z_in )
    {
        if( world.isAirBlock( x_in, y_in, z_in ) && world.isAirBlock( x_in, y_in + 1, z_in ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isLocationSafe( World world, int x_in, int y_in, int z_in )
    {
        Block bottom_block = world.getBlock( x_in, y_in - 1, z_in );

        if( bottom_block instanceof BlockCactus || bottom_block instanceof BlockAir || bottom_block.getMaterial() == Material.lava )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void writeToNBT( NBTTagCompound compound )
    {
        compound.setInteger( NBTHelper.DIMENSION_NBT, dimension );
        compound.setDouble( NBTHelper.X_NBT, x );
        compound.setDouble( NBTHelper.Y_NBT, y );
        compound.setDouble( NBTHelper.Z_NBT, z );
    }
}
