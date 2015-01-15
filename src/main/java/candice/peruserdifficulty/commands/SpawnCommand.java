package candice.peruserdifficulty.commands;

import candice.peruserdifficulty.enums.PlayerDifficulty;
import candice.peruserdifficulty.helpers.Location;
import candice.peruserdifficulty.helpers.NBTHelper;
import candice.peruserdifficulty.helpers.PlayerDifficultyHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

/**
 * Created by Candice on 1/11/2015.
 */
public class SpawnCommand extends CommandBase
{
    @Override
    public boolean canCommandSenderUseCommand( ICommandSender sender )
    {
        return true;
    }

    @Override
    public String getCommandName()
    {
        return "spawn";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getCommandUsage( ICommandSender sender )
    {
        return "/spawn";
    }

    @Override
    public void processCommand( ICommandSender sender, String[] params )
    {
        if( !( sender instanceof EntityPlayerMP ) )
        {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) sender;
        PlayerDifficulty difficulty = NBTHelper.getDifficultyLevel( player );
        ChatComponentText return_message = null;

        if( !PlayerDifficultyHelper.shouldAllowSpawnCommand( difficulty ) && difficulty != PlayerDifficulty.DISABLED )
        {
            return_message = CommandHelper.getErrorMessage( CommandHelper.NO_PERMISSION );
        }
        else if( params.length != 0 )
        {
            return_message = CommandHelper.getErrorMessage( "Correct usage is " + getCommandUsage( sender ) );
        }
        else
        {
            double x = player.worldObj.getSpawnPoint().posX;
            double y = player.worldObj.getSpawnPoint().posY;
            double z = player.worldObj.getSpawnPoint().posZ;
            Location spawn = new Location( 0, x, y, z );
            spawn.sendPlayerTo( player );
        }

        ( (EntityPlayer) sender ).addChatComponentMessage( return_message );
    }
}
