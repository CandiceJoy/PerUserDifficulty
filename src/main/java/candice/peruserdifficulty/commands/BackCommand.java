package candice.peruserdifficulty.commands;

import candice.peruserdifficulty.enums.PlayerDifficulty;
import candice.peruserdifficulty.helpers.Location;
import candice.peruserdifficulty.helpers.NBTHelper;
import candice.peruserdifficulty.helpers.PlayerDifficultyHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

/**
 * Created by Candice on 1/8/2015.
 */
public class BackCommand extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "back";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getCommandUsage( ICommandSender sender )
    {
        return "/back";
    }

    @Override
    public void processCommand( ICommandSender sender, String[] params )
    {
        if( !( sender instanceof EntityPlayer ) )
        {
            return;
        }

        EntityPlayer player = (EntityPlayer) sender;
        PlayerDifficulty difficulty = NBTHelper.getDifficultyLevel( player );
        ChatComponentText return_message = null;

        if( !PlayerDifficultyHelper.shouldAllowBackCommand( difficulty ) )
        {
            return_message = CommandHelper.getErrorMessage( CommandHelper.NO_PERMISSION );
        }
        else if( params.length == 0 )
        {
            Location location = NBTHelper.getBackLocation( player );

            if( location != null )
            {
                location.sendPlayerTo( player );
                NBTHelper.eraseBackLocation( player );
            }
            else
            {
                return_message = CommandHelper.getErrorMessage( "You do not have a location to go back to." );
            }
        }
        else
        {
            return_message = CommandHelper.getErrorMessage( "Correct usage is " + getCommandUsage( sender ) );
        }

        ( (EntityPlayer) sender ).addChatComponentMessage( return_message );
    }
}
