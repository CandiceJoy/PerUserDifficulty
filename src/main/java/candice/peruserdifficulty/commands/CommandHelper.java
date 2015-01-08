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
        ArrayList<Integer> word_beginning_indexes = new ArrayList<Integer>();

        char current = ' ';
        char previous = ' ';

        for( int x = 0; x < message.length(); x++ )
        {
            previous = current;
            current = message.charAt( x );

            if( previous == ' ' && current != ' ' )
            {
                word_beginning_indexes.add( x );
            }
        }

        StringBuffer buffer = new StringBuffer( message );

        for( int x = word_beginning_indexes.size() - 1; x >= 0; x-- )
        {
            buffer.insert( word_beginning_indexes.get( x ), color );
        }

        return new ChatComponentText( buffer.toString() );
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
