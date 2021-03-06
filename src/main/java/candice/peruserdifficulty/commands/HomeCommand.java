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
        return "/home <set|dimension>";
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

        if( !PlayerDifficultyHelper.shouldAllowHomeCommand( difficulty ) && difficulty != PlayerDifficulty.DISABLED )
        {
            return_message = CommandHelper.getErrorMessage( CommandHelper.NO_PERMISSION );
        }
        else if( params.length == 0 )
        {
            int dimension = player.dimension;
            Location home = NBTHelper.getHomeLocation( player, dimension );
            Location back = new Location( player );

            if( home != null )
            {
                double x = home.getX();
                double y = home.getY();
                double z = home.getZ();

                NBTHelper.setBackLocation( player, back );
                boolean worked = home.sendPlayerTo( player );

                if( !worked )
                {
                    return_message = CommandHelper.getErrorMessage( "Your home location is blocked." );
                }
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

                Location home = new Location( dimension, x, y, z );

                NBTHelper.setHomeLocation( player, home );
                return_message = CommandHelper.getReturnMessage( "Home location set." );
            }
            else
            {
                int dimension = 0;
                boolean set = false;

                try
                {
                    dimension = Integer.parseInt( params[0] );
                    set = true;
                }catch( NumberFormatException e )
                {
                    set = false;
                }

                if( set )
                {
                    Location home = NBTHelper.getHomeLocation( player, dimension );

                    if( home != null )
                    {
                        boolean worked = home.sendPlayerTo( player );

                        if( !worked )
                        {
                            return_message = CommandHelper.getErrorMessage( "Your home location is blocked." );
                        }
                    }
                    else
                    {
                        return_message = CommandHelper.getErrorMessage( "You do not have a home in that dimension." );
                    }
                }
                else
                {
                    return_message = CommandHelper.getErrorMessage( "Correct usage is " + getCommandUsage( sender ) );
                }
            }
        }
        else
        {
            return_message = CommandHelper.getErrorMessage( "Correct usage is " + getCommandUsage( sender ) );
        }

        ( (EntityPlayer) sender ).addChatComponentMessage( return_message );
    }
}
