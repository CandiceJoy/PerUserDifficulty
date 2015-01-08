package candice.peruserdifficulty.commands;

import candice.peruserdifficulty.PerUserDifficultyMod;
import candice.peruserdifficulty.enums.PlayerDifficulty;
import candice.peruserdifficulty.helpers.NBTHelper;
import candice.peruserdifficulty.helpers.PlayerDifficultyHelper;
import candice.peruserdifficulty.helpers.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Candice on 12/18/2014.
 */
public class SetDifficultyCommand extends CommandBase
{
    @Override
    public List addTabCompletionOptions( ICommandSender icommandsender, String[] params )
    {
        ArrayList<String> tab_completion_matches = new ArrayList<String>();

        if( params.length == 1 )
        {
            String tab_completion_candidate = params[params.length - 1].toLowerCase().trim();

            CommandHelper.addToListIfMatches( "easy", tab_completion_candidate, tab_completion_matches );
            CommandHelper.addToListIfMatches( "medium", tab_completion_candidate, tab_completion_matches );
            CommandHelper.addToListIfMatches( "hard", tab_completion_candidate, tab_completion_matches );
            CommandHelper.addToListIfMatches( "disabled", tab_completion_candidate, tab_completion_matches );
        }

        return tab_completion_matches;
    }

    @Override
    public String getCommandName()
    {
        return "setdifficulty";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getCommandUsage( ICommandSender sender )
    {
        return "/setdifficulty <difficulty>";
    }

    @Override
    public boolean canCommandSenderUseCommand( ICommandSender sender )
    {
        return true;
    }

    @Override
    public void processCommand( ICommandSender sender, String[] params )
    {
        if( !( sender instanceof EntityPlayer ) )
        {
            return;
        }

        EntityPlayer player = (EntityPlayer) sender;
        ChatComponentText return_message = null;

        if( params.length == 0 )
        {
            return_message = CommandHelper.getErrorMessage( "Your current difficulty is: " + NBTHelper.getDifficultyLevel( player ) );
        }
        else if( params.length != 1 )
        {
            return_message = CommandHelper.getErrorMessage( "Correct usage is " + getCommandUsage( sender ) );
        }
        else
        {
            String difficulty_string = params[0];
            PlayerDifficulty difficulty = PlayerDifficultyHelper.stringToDifficulty( difficulty_string );

            if( difficulty == null )
            {
                return_message = CommandHelper.getErrorMessage( "Valid difficulties are disabled, easy, medium, and hard." );
            }
            else
            {
                if( difficulty == NBTHelper.getDifficultyLevel( player ) )
                {
                    return_message = CommandHelper.getErrorMessage( "You cannot set your difficulty to the same level." );
                }
                else
                {
                    Long current_time = System.currentTimeMillis();
                    Long last_change = NBTHelper.getLastChanged( player );
                    Long next_change_time = last_change + PerUserDifficultyMod.getMinimumTimeBetweenDifficultyChanges();

                    if( current_time < next_change_time )
                    {
                        return_message = CommandHelper.getErrorMessage( "You must wait " + Utils.secondsToTimeString( Math.round( (double) ( next_change_time - current_time ) / 1000.0 ) ) + " before changing your difficulty again." );
                    }
                    else
                    {
                        NBTHelper.setDifficultyLevel( player, difficulty );
                        return_message = CommandHelper.getReturnMessage( "Your difficulty is now set to " + difficulty + "." );
                    }
                }
            }
        }

        ( (EntityPlayer) sender ).addChatComponentMessage( return_message );
    }
}
