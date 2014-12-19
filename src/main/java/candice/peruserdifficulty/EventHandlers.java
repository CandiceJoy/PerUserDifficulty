package candice.peruserdifficulty;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

public class EventHandlers
{
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)

    public void onEvent(LivingHurtEvent event)
    {
        System.out.println( FMLCommonHandler.instance().getEffectiveSide() );

        Entity entity = event.entity;
        PlayerDifficulty difficulty = PlayerDifficultyList.getPlayerDifficulty( entity.getUniqueID() );
        double damage_multiplier = 1.0;

        System.out.println( entity.getUniqueID() );

        if( difficulty != null )
        {
            switch( difficulty )
            {
                case EASY:
                    damage_multiplier = 0.5;
                    break;
                case MEDIUM:
                    damage_multiplier = 1.0;
                    break;
                case HARD:
                    damage_multiplier = 2.0;
                    break;
            }
        }

        event.ammount *= damage_multiplier;
    }
}