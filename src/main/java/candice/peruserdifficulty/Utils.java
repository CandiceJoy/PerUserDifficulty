package candice.peruserdifficulty;

/**
 * Created by Candice on 1/2/2015.
 */
public class Utils
{
    public static String minutesToTimeString( int minutes )
    {
        if( minutes < 0 )
        {
            return "";
        }

        int days = 0;
        int hours = 0;
        String output = "";

        while( minutes / 60 >= 1 )
        {
            hours++;
            minutes -= 60;
        }

        while( hours / 24 >= 1 )
        {
            days++;
            hours -= 24;
        }

        if( days > 0 )
        {
            output += days + " day" + ( days > 1 ? "s" : "" ) + " ";
        }

        if( hours > 0 )
        {
            output += hours + " hour" + ( hours > 1 ? "s" : "" ) + " ";
        }

        if( minutes > 0 )
        {
            output += minutes + " minute" + ( minutes > 1 ? "s" : "" ) + " ";
        }

        return output;
    }
}
