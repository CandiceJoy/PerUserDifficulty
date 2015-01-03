package candice.peruserdifficulty.commands;

/**
 * Created by Candice on 1/2/2015.
 */

import candice.peruserdifficulty.NBTHelper;
import candice.peruserdifficulty.PlayerDifficulty;
import candice.peruserdifficulty.PlayerDifficultyHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class AdminSetDifficultyCommand extends CommandBase
{
    @Override
    public List addTabCompletionOptions( ICommandSender icommandsender, String[] params )
    {
        ArrayList<String> tab_completion_matches = new ArrayList<String>();
        String tab_completion_candidate = params[params.length - 1].toLowerCase().trim();

        if( params.length == 1 )
        {
            for( String username : MinecraftServer.getServer().getAllUsernames() )
            {
                CommandHelper.addToListIfMatches( username.toLowerCase().trim(), tab_completion_candidate, tab_completion_matches );
            }
        }
        else if( params.length == 2 )
        {
            CommandHelper.addToListIfMatches( "easy", tab_completion_candidate, tab_completion_matches );
            CommandHelper.addToListIfMatches( "medium", tab_completion_candidate, tab_completion_matches );
            CommandHelper.addToListIfMatches( "hard", tab_completion_candidate, tab_completion_matches );
            CommandHelper.addToListIfMatches( "disabled", tab_completion_candidate, tab_completion_matches );
        }
        else
        {
            return tab_completion_matches;
        }

        return tab_completion_matches;
    }

    @Override
    public String getCommandName()
    {
        return "adminsetdifficulty";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandUsage( ICommandSender sender )
    {
        return "/adminsetdifficulty <username> <difficulty>";
    }

    @Override
    public void processCommand( ICommandSender sender, String[] params )
    {
        if( !( sender instanceof EntityPlayer ) )
        {
            return;
        }

        EntityPlayer requesting_player = (EntityPlayer) sender;
        ChatComponentText return_message = null;

        if( params.length != 2 )
        {
            return_message = CommandHelper.getErrorMessage( "Correct usage is " + getCommandUsage( sender ) );
        }
        else
        {
            EntityPlayer target_player = getPlayer( sender, params[0] );

            if( target_player == null )
            {
                return_message = CommandHelper.getErrorMessage( "That player cannot be found." );
            }
            else
            {
                String difficulty_string = params[1];
                PlayerDifficulty difficulty = PlayerDifficultyHelper.stringToDifficulty( difficulty_string );

                if( difficulty == null )
                {
                    return_message = CommandHelper.getErrorMessage( "Valid difficulties are disabled, easy, medium, and hard." );
                }
                else
                {
                    if( difficulty == NBTHelper.getDifficultyLevel( target_player ) )
                    {
                        return_message = CommandHelper.getErrorMessage( "You cannot set the difficulty for " + target_player.getDisplayName() + " to the same level." );
                    }
                    else
                    {
                        if( requesting_player.getUniqueID().equals( target_player.getUniqueID() ) )
                        {
                            return_message = CommandHelper.getErrorMessage( "You cannot set your own difficulty through this command." );
                        }
                        else
                        {
                            NBTHelper.setDifficultyLevel( target_player, difficulty );
                            return_message = CommandHelper.getReturnMessage( "The difficulty level for " + target_player.getDisplayName() + " is now set to " + difficulty + "." );
                        }
                    }
                }
            }
        }

        ( (EntityPlayer) sender ).addChatComponentMessage( return_message );
    }
}
