package candice.peruserdifficulty.commands;

import candice.peruserdifficulty.helpers.Location;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Candice on 3/12/2015.
 */
public class TPZCommand extends CommandBase
{
    @Override
    public List addTabCompletionOptions( ICommandSender icommandsender, String[] params )
    {
        ArrayList<String> tab_completion_matches = new ArrayList<String>();

        List<EntityPlayer> players = icommandsender.getEntityWorld().playerEntities;

        for( EntityPlayer player : players )
        {
            tab_completion_matches.add( player.getDisplayName() );
        }

        return tab_completion_matches;
    }

    @Override
    public String getCommandName()
    {
        return "tpz";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandUsage( ICommandSender sender )
    {
        return "/tpz <player>";
    }

    @Override
    public boolean canCommandSenderUseCommand( ICommandSender sender )
    {
        return true;
    }

    @Override
    public void processCommand( ICommandSender sender, String[] params )
    {
        if( !( sender instanceof EntityPlayerMP ) )
        {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) sender;
        ChatComponentText return_message = null;

        if( params.length != 1 )
        {
            return_message = CommandHelper.getErrorMessage( "Correct usage is: " + getCommandUsage( sender ) );
        }
        else
        {
            List<EntityPlayer> players = player.getEntityWorld().playerEntities;
            EntityPlayer target = null;

            for( EntityPlayer candidate : players )
            {
                if( candidate.getDisplayName().toLowerCase().equals( params[0].toLowerCase() ) )
                {
                    target = candidate;
                    break;
                }
            }

            if( target == null )
            {
                return_message = CommandHelper.getErrorMessage( "Cannot find that player." );
            }
            else if( target.getDisplayName().toLowerCase().equals( player.getDisplayName().toLowerCase() ) )
            {
                return_message = CommandHelper.getErrorMessage( "You cannot teleport to yourself." );
            }
            else
            {
                Location destination = new Location( target.dimension, target.posX, target.posY, target.posZ );
                destination.sendPlayerTo( player );
            }
        }

        ( (EntityPlayer) sender ).addChatComponentMessage( return_message );
    }
}
