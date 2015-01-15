package candice.peruserdifficulty.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Candice on 1/8/2015.
 */
public class Location
{
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

    public void sendPlayerTo( EntityPlayer player )
    {
        if( !player.worldObj.isRemote )
        {
            if( player.getEntityWorld().provider.dimensionId != dimension )
            {
                player.travelToDimension( dimension );
            }

            player.setPositionAndUpdate( x, y, z );
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
