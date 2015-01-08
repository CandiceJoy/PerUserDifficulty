package candice.peruserdifficulty.commands;

import candice.peruserdifficulty.enums.PlayerDifficulty;
import candice.peruserdifficulty.helpers.Location;
import candice.peruserdifficulty.helpers.NBTHelper;
import candice.peruserdifficulty.helpers.PlayerDifficultyHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Candice on 1/7/2015.
 */
public class HomeCommand extends CommandBase
{
    @Override
    public List addTabCompletionOptions( ICommandSender icommandsender, String[] params )
    {
        ArrayList<String> tab_completion_matches = new ArrayList<String>();

        if( params.length == 1 )
        {
            String tab_completion_candidate = params[params.length - 1].toLowerCase().trim();

            CommandHelper.addToListIfMatches( "set", tab_completion_candidate, tab_completion_matches );

            return tab_completion_matches;
        }

        return tab_completion_matches;
    }

    @Override
    public boolean canCommandSenderUseCommand( ICommandSender sender )
    {
        return true;
    }

    @Override
    public String getCommandName()
    {
        return "home";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getCommandUsage( ICommandSender sender )
    {
        return "/home <set>";
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

        if( !PlayerDifficultyHelper.shouldAllowHomeCommand( difficulty ) && difficulty != PlayerDifficulty.DISABLED )
        {
            return_message = CommandHelper.getErrorMessage( CommandHelper.NO_PERMISSION );
        }
        else if( params.length == 0 )
        {
            int dimension = player.dimension;
            Location location = NBTHelper.getHomeLocation( player, dimension );

            if( location != null )
            {
                double x = location.getX();
                double y = location.getY();
                double z = location.getZ();

                location.sendPlayerTo( player );
            }
            else
            {
                return_message = CommandHelper.getErrorMessage( "Your home has not been set in this dimension." );
            }
        }
        else if( params.length == 1 )
        {
            if( params[0].toLowerCase().trim().equals( "set" ) )
            {
                int dimension = player.dimension;
                double x = player.posX;
                double y = player.posY;
                double z = player.posZ;

                NBTHelper.setHomeLocation( player, new Location( dimension, x, y, z ) );
                return_message = CommandHelper.getReturnMessage( "Home location set." );
            }
            else
            {
                return_message = CommandHelper.getErrorMessage( "Correct usage is " + getCommandUsage( sender ) );
            }
        }
        else
        {
            return_message = CommandHelper.getErrorMessage( "Correct usage is " + getCommandUsage( sender ) );
        }

        ( (EntityPlayer) sender ).addChatComponentMessage( return_message );
    }
}
