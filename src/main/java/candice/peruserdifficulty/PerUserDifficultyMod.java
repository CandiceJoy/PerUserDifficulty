package candice.peruserdifficulty;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Created by Candice on 12/18/2014.
 */

@Mod(modid = PerUserDifficultyMod.MODID, version = PerUserDifficultyMod.VERSION)
public class PerUserDifficultyMod
{
    public static final String MODID = "CandisPerUserDifficulty";
    public static final String VERSION = "1.0";

    private static int time_between_difficulty_changes = 0;

    public static long getMinimumTimeBetweenDifficultyChanges()
    {
        return time_between_difficulty_changes * 60 * 1000;
    }

    @Mod.EventHandler
    public void init( FMLInitializationEvent event )
    {
        MinecraftForge.EVENT_BUS.register( new EventHandlers() );
    }

    @EventHandler
    public void serverLoad( FMLServerStartingEvent event )
    {
        event.registerServerCommand( new SetDifficultyCommand() );
    }

    @Mod.EventHandler
    public void preInit( FMLPreInitializationEvent event )
    {
        Configuration config = new Configuration( new File( event.getModConfigurationDirectory(), "CandisPerUserDifficulty.cfg" ) );
        config.load();

        //PlayerDifficultyHelper.setConfig( config );

        double[] damage_taken = new double[3];
        double[] damage_dealt = new double[3];
        PlayerDifficulty max_keepinventory = null;

        damage_taken[0] = config.get( "Damage Taken", "Easy", 0.75, "Multiplier as a decimal percentage" ).getDouble();
        damage_taken[1] = config.get( "Damage Taken", "Medium", 1.0 ).getDouble();
        damage_taken[2] = config.get( "Damage Taken", "Hard", 1.25 ).getDouble();

        damage_dealt[0] = config.get( "Damage Dealt", "Easy", 1.25, "Multiplier as a decimal percentage" ).getDouble();
        damage_dealt[1] = config.get( "Damage Dealt", "Medium", 1.0 ).getDouble();
        damage_dealt[2] = config.get( "Damage Dealt", "Hard", 0.75 ).getDouble();

        max_keepinventory = PlayerDifficultyHelper.numberToDifficulty( config.get( "Keep Inventory", "KeepInventory", 1, "Max level for KeepInventory (1=easy, 2=med, 3=hard)" ).getInt() );

        time_between_difficulty_changes = config.get( "Rate Limits", "Time Between Difficulty Changes", 60, "Time in positive integer minutes; set 0 for no limit" ).getInt();

        config.save();

        PlayerDifficultyHelper.setConfig( damage_taken, damage_dealt, max_keepinventory );
    }
}
