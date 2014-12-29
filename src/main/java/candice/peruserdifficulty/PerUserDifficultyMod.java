package candice.peruserdifficulty;

import cpw.mods.fml.common.FMLCommonHandler;
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
    public static final String VERSION = "0.3";

    private static int time_between_difficulty_changes = 0;

    public static long getMinimumTimeBetweenDifficultyChanges()
    {
        return time_between_difficulty_changes * 60 * 1000;
    }

    @Mod.EventHandler
    public void init( FMLInitializationEvent event )
    {
        MinecraftForge.EVENT_BUS.register( new EventHandlers() );
        FMLCommonHandler.instance().bus().register( new EventHandlersCommon() );
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

        double[] damage_taken = new double[3];
        double[] damage_dealt = new double[3];
        double[] food = new double[3];
        double[] saturation = new double[3];
        PlayerDifficulty max_keepinventory = null;

        damage_taken[0] = config.get( "Damage Taken", "Easy", 0.75, "Multiplier as a decimal percentage" ).getDouble();
        damage_taken[1] = config.get( "Damage Taken", "Medium", 1.0 ).getDouble();
        damage_taken[2] = config.get( "Damage Taken", "Hard", 1.25 ).getDouble();

        damage_dealt[0] = config.get( "Damage Dealt", "Easy", 1.25, "Multiplier as a decimal percentage" ).getDouble();
        damage_dealt[1] = config.get( "Damage Dealt", "Medium", 1.0 ).getDouble();
        damage_dealt[2] = config.get( "Damage Dealt", "Hard", 0.75 ).getDouble();

        food[0] = config.get( "Food", "Easy", 0.5, "Bonus as a decimal percentage" ).getDouble();
        food[1] = config.get( "Food", "Medium", 0.25 ).getDouble();
        food[2] = config.get( "Food", "Hard", 0.0 ).getDouble();

        saturation[0] = config.get( "Saturation", "Easy", 0.5, "Bonus as a decimal percentage" ).getDouble();
        saturation[1] = config.get( "Saturation", "Medium", 0.25 ).getDouble();
        saturation[2] = config.get( "Saturation", "Hard", 0.0 ).getDouble();

        max_keepinventory = PlayerDifficultyHelper.numberToDifficulty( config.get( "Keep Inventory", "KeepInventory", 1, "Max level for KeepInventory (1=easy, 2=med, 3=hard)" ).getInt() );

        time_between_difficulty_changes = config.get( "Rate Limits", "Time Between Difficulty Changes", 60, "Time in positive integer minutes; set 0 for no limit" ).getInt();

        config.save();

        PlayerDifficultyHelper.setConfig( damage_taken, damage_dealt, food, saturation, max_keepinventory );
    }
}
