package candice.peruserdifficulty;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

public class EventHandlers
{
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent( LivingHurtEvent event )
    {
        Entity entity = event.entity;

        if( !(entity instanceof EntityPlayer ) )
        {
            return;
        }

        EntityPlayer player = (EntityPlayer)entity;

        //PlayerDifficulty difficulty = PlayerDifficultyList.getPlayerDifficulty( entity.getUniqueID() );
        PlayerDifficulty difficulty = PlayerDifficultyNBTHelper.getDifficultyLevel( player );
        System.out.println( "Difficulty: " + difficulty );
        double damage_multiplier = 1.0;

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

    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent( LivingDeathEvent event )
    {
        Entity entity = event.entity;

        if( !(entity instanceof EntityPlayer ) )
        {
            return;
        }

        EntityPlayer player = (EntityPlayer)entity;

        //PlayerDifficulty difficulty = PlayerDifficultyList.getPlayerDifficulty( entity.getUniqueID() );
        PlayerDifficulty difficulty = PlayerDifficultyNBTHelper.getDifficultyLevel( player );

        if( difficulty != null )
        {
            switch( difficulty )
            {
                case EASY:
                    break;
                case MEDIUM:
                case HARD:
                    player.inventory.dropAllItems();
                    break;
            }
        }
    }
}