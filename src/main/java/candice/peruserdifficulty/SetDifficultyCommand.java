package candice.peruserdifficulty;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by Candice on 12/18/2014.
 */
public class SetDifficultyCommand extends CommandBase
{
    private static ChatComponentText getMessage( EnumChatFormatting color, String message )
    {
        return new ChatComponentText( color + message );
    }

    private static ChatComponentText getErrorMessage( String message )
    {
        return getMessage( EnumChatFormatting.RED, message );
    }

    private static ChatComponentText getReturnMessage( String message )
    {
        return getMessage( EnumChatFormatting.GREEN, message );
    }

    private static ChatComponentText setDifficulty( EntityPlayer player, PlayerDifficulty difficulty )
    {
        NBTHelper.setDifficultyLevel( player, difficulty );
        return getReturnMessage( "Difficulty set to " + difficulty + "." );
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
            return_message = getErrorMessage( "Your current difficulty is: " + NBTHelper.getDifficultyLevel( player ) );
        }
        else if( params.length != 1 )
        {
            return_message = getErrorMessage( "Invalid number of parameters." );
        }
        else
        {
            String difficulty_string = params[0];

            if( difficulty_string.length() != 1 )
            {
                PlayerDifficulty difficulty = null;
                String standardized_difficulty_string = difficulty_string.toLowerCase().trim();

                if( standardized_difficulty_string.equals( "disabled" ) )
                {
                    difficulty = PlayerDifficulty.DISABLED;
                }
                else if( standardized_difficulty_string.equals( "easy" ) )
                {
                    difficulty = PlayerDifficulty.EASY;
                }
                else if( standardized_difficulty_string.equals( "medium" ) || standardized_difficulty_string.equals( "med" ) )
                {
                    difficulty = PlayerDifficulty.MEDIUM;
                }
                else if( standardized_difficulty_string.equals( "hard" ) )
                {
                    difficulty = PlayerDifficulty.HARD;
                }

                if( difficulty == null )
                {
                    return_message = getErrorMessage( "Valid difficulties are disabled, easy, medium, and hard." );
                }
                else
                {
                    return_message = setDifficulty( player, difficulty );
                }
            }
            else
            {
                int difficulty_number = -1;

                try
                {
                    difficulty_number = Integer.parseInt( difficulty_string );
                }catch( NumberFormatException e )
                {
                    return_message = getErrorMessage( "Difficulty not a number." );
                }

                if( difficulty_number != -1 )
                {
                    if( difficulty_number < 0 || difficulty_number > 3 )
                    {
                        return_message = getErrorMessage( "Valid difficulties are 0-3." );
                    }
                    else
                    {
                        PlayerDifficulty difficulty = PlayerDifficultyHelper.numberToDifficulty( difficulty_number );

                        if( difficulty == null )
                        {
                            return_message = getErrorMessage( "Unknown error converting command params to difficulty.  Please contact the mod author." );
                        }
                        else
                        {
                            Long last_change = NBTHelper.getLastChanged( player );

                            if( System.currentTimeMillis() < last_change + PerUserDifficultyMod.getMinimumTimeBetweenDifficultyChanges() )
                            {
                                return_message = getErrorMessage( "You must wait longer before changing your difficulty again." );
                            }
                            else
                            {
                                return_message = setDifficulty( player, difficulty );
                            }
                        }
                    }
                }
            }
        }

        ( (EntityPlayer) sender ).addChatComponentMessage( return_message );
    }
}
