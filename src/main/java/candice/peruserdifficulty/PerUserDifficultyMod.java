package candice.peruserdifficulty;

import candice.peruserdifficulty.commands.*;
import candice.peruserdifficulty.enums.PlayerDifficulty;
import candice.peruserdifficulty.events.FMLEventHandlers;
import candice.peruserdifficulty.events.MFEventHandlers;
import candice.peruserdifficulty.helpers.PlayerDifficultyHelper;
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

    private static long time_between_difficulty_changes = 0;

    public static long getMinimumTimeBetweenDifficultyChanges()
    {
        long min_time = time_between_difficulty_changes * 60l * 1000l;

        if( min_time >= 0 )
        {
            return time_between_difficulty_changes * 60l * 1000l;
        }
        else
        {
            return 0;
        }
    }

    @Mod.EventHandler
    public void init( FMLInitializationEvent event )
    {
        MinecraftForge.EVENT_BUS.register( new MFEventHandlers() );
        FMLCommonHandler.instance().bus().register( new FMLEventHandlers() );
    }

    @EventHandler
    public void serverLoad( FMLServerStartingEvent event )
    {
        event.registerServerCommand( new SetDifficultyCommand() );
        event.registerServerCommand( new AdminSetDifficultyCommand() );
        event.registerServerCommand( new HomeCommand() );
        event.registerServerCommand( new BackCommand() );
        event.registerServerCommand( new SpawnCommand() );
        event.registerServerCommand( new TPZCommand() );
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
        PlayerDifficulty max_home = null;
        PlayerDifficulty max_back = null;
        PlayerDifficulty max_spawn = null;

        damage_taken[0] = config.get( "Damage Taken", "Easy", 0.75, "Multiplier as a decimal percentage" ).getDouble();
        damage_taken[1] = config.get( "Damage Taken", "Medium", 1.0 ).getDouble();
        damage_taken[2] = config.get( "Damage Taken", "Hard", 1.25 ).getDouble();

        damage_dealt[0] = config.get( "Damage Dealt", "Easy", 1.25, "Multiplier as a decimal percentage" ).getDouble();
        damage_dealt[1] = config.get( "Damage Dealt", "Medium", 1.0 ).getDouble();
        damage_dealt[2] = config.get( "Damage Dealt", "Hard", 0.75 ).getDouble();

        food[0] = config.get( "Food", "Easy", 1.25, "Multiplier as a decimal percentage" ).getDouble();
        food[1] = config.get( "Food", "Medium", 1.0 ).getDouble();
        food[2] = config.get( "Food", "Hard", 0.75 ).getDouble();

        saturation[0] = config.get( "Saturation", "Easy", 1.25, "Multiplier as a decimal percentage" ).getDouble();
        saturation[1] = config.get( "Saturation", "Medium", 1.0 ).getDouble();
        saturation[2] = config.get( "Saturation", "Hard", 0.75 ).getDouble();

        max_keepinventory = PlayerDifficultyHelper.numberToDifficulty( config.get( "Keep Inventory", "KeepInventory", 1, "Max difficulty level for KeepInventory (0=disabled, 1=easy, 2=med, 3=hard)" ).getInt() );
        max_home = PlayerDifficultyHelper.numberToDifficulty( config.get( "Location", "Home", 3, "Max difficulty level for /home (0=disabled,1=easy, 2=med, 3=hard)" ).getInt() );
        max_back = PlayerDifficultyHelper.numberToDifficulty( config.get( "Location", "Back", 2, "Max difficulty level for /back (0=disabled,1=easy, 2=med, 3=hard)" ).getInt() );
        max_spawn = PlayerDifficultyHelper.numberToDifficulty( config.get( "Location", "Spawn", 2, "Max difficulty level for /spawn (0=disabled,1=easy,2=med,3=hard" ).getInt() );

        time_between_difficulty_changes = config.get( "Rate Limits", "Time Between Difficulty Changes", 60, "Time in positive integer minutes (max 2147483647); set 0 for no limit" ).getInt();

        config.save();

        PlayerDifficultyHelper.setConfig( damage_taken, damage_dealt, food, saturation, max_keepinventory, max_home, max_back, max_spawn );
    }
}
