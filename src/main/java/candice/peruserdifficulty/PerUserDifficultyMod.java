package candice.peruserdifficulty;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Candice on 12/18/2014.
 */

@Mod(modid = PerUserDifficultyMod.MODID, version = PerUserDifficultyMod.VERSION)
public class PerUserDifficultyMod
{
    public static final String MODID = "CandiPerUserDifficulty";
    public static final String VERSION = "1.0";
    public static final long MINIMUM_TIME_BETWEEN_DIFFICULTY_CHANGES = 1000 * 5;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new EventHandlers());
    }

    @EventHandler
    public void serverLoad( FMLServerStartingEvent event )
    {
        event.registerServerCommand( new SetDifficultyCommand() );
    }
}
