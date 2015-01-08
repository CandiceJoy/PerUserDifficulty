package candice.peruserdifficulty.commands;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;

/**
 * Created by Candice on 1/2/2015.
 */
public class CommandHelper
{
    public static final String NO_PERMISSION = "Your difficulty level does not permit the use of this command.";

    private static ChatComponentText getMessage( EnumChatFormatting color, String message )
    {
        for( String word : message.split( " " ) )
        {
            if( !word.trim().equals( "" ) )
            {
                message = message.replaceAll( word, color + word );
            }
        }

        return new ChatComponentText( message );
    }

    public static ChatComponentText getErrorMessage( String message )
    {
        return getMessage( EnumChatFormatting.RED, message );
    }

    public static ChatComponentText getReturnMessage( String message )
    {
        return getMessage( EnumChatFormatting.GREEN, message );
    }

    public static void addToListIfMatches( String full_word, String tab_completion_candidate, ArrayList<String> tab_completion_matches )
    {
        if( full_word.indexOf( tab_completion_candidate ) == 0 )
        {
            tab_completion_matches.add( full_word );
        }
    }
}
