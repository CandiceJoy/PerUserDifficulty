package candice.peruserdifficulty;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class EventHandlers
{
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent( LivingHurtEvent event )
    {
        Entity damaged = event.entity;
        Entity damager = event.source.getEntity();

        if( damaged instanceof EntityPlayer && !( damager instanceof EntityPlayer ) )
        {
            EntityPlayer player = (EntityPlayer) damaged;
            PlayerDifficulty difficulty = PlayerDifficultyNBTHelper.getDifficultyLevel( player );
            System.out.println( "Difficulty: " + difficulty );
            System.out.println( "Multiplier: " + PlayerDifficultyHelper.getDamageTakenMultiplier( difficulty ) );

            if( difficulty != null )
            {
                event.ammount *= PlayerDifficultyHelper.getDamageTakenMultiplier( difficulty );
            }
        }
        else if( damager instanceof EntityPlayer && !( damaged instanceof EntityPlayer ) )
        {
            EntityPlayer player = (EntityPlayer) damager;
            PlayerDifficulty difficulty = PlayerDifficultyNBTHelper.getDifficultyLevel( player );
            System.out.println( "Difficulty: " + difficulty );

            if( difficulty != null )
            {
                event.ammount *= PlayerDifficultyHelper.getDamageDealtMultiplier( difficulty );
            }
        }
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
        PlayerDifficulty difficulty = PlayerDifficultyNBTHelper.getDifficultyLevel( player );

        if( difficulty != null )
        {
            if( !PlayerDifficultyHelper.shouldDoKeepInventory( difficulty ) )
            {
                    player.inventory.dropAllItems();
            }
        }
    }
}