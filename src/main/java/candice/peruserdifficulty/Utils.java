package candice.peruserdifficulty;

/**
 * Created by Candice on 1/2/2015.
 */
public class Utils
{
    public static String secondsToTimeString( long seconds )
    {
        if( seconds < 0 )
        {
            return "";
        }

        long days = 0;
        long hours = 0;
        long minutes = 0;
        String output = "";

        while( seconds / 60 >= 1 )
        {
            minutes++;
            seconds -= 60;
        }

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
            output += getTimeString( "day", days );
        }

        if( hours > 0 )
        {
            output += getTimeString( "hour", hours );
        }

        if( minutes > 0 )
        {
            output += getTimeString( "minute", minutes );
        }

        if( seconds > 0 )
        {
            output += getTimeString( "second", seconds );
        }

        return output.trim();
    }

    private static String getTimeString( String unit, long value )
    {
        return value + " " + unit + ( value > 1 ? "s" : "" ) + " ";
    }
}
