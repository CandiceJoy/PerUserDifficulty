package candice.peruserdifficulty;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

import java.util.ArrayList;

public class EventHandlers
{
    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = false)
    public void onEvent( LivingHurtEvent event )
    {
        Entity damaged = event.entity;
        Entity damager = event.source.getEntity();

        if( damaged instanceof EntityPlayer && !( damager instanceof EntityPlayer ) )
        {
            EntityPlayer player = (EntityPlayer) damaged;
            PlayerDifficulty difficulty = NBTHelper.getDifficultyLevel( player );

            if( difficulty != null && difficulty != PlayerDifficulty.DISABLED )
            {
                event.ammount *= PlayerDifficultyHelper.getDamageTakenMultiplier( difficulty );
            }
        }
        else if( damager instanceof EntityPlayer && !( damaged instanceof EntityPlayer ) )
        {
            EntityPlayer player = (EntityPlayer) damager;
            PlayerDifficulty difficulty = NBTHelper.getDifficultyLevel( player );

            if( difficulty != null && difficulty != PlayerDifficulty.DISABLED )
            {
                event.ammount *= PlayerDifficultyHelper.getDamageDealtMultiplier( difficulty );
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
    public void onEvent( PlayerDropsEvent event )
    {
        System.out.println( "Player drops!!!" );

        Entity entity = event.entity;

        if( !(entity instanceof EntityPlayer ) )
        {
            return;
        }

        EntityPlayer player = (EntityPlayer)entity;
        PlayerDifficulty difficulty = NBTHelper.getDifficultyLevel( player );

        System.out.println( "difficulty: " + difficulty );

        if( difficulty != null && difficulty != PlayerDifficulty.DISABLED )
        {
            if( PlayerDifficultyHelper.shouldDoKeepInventory( difficulty ) )
            {
                System.out.println( "Keep inventory!" );

                final ArrayList<EntityItem> drops_as_entities = event.drops;
                ArrayList<ItemStack> drops_as_itemstack_arraylist = new ArrayList<ItemStack>();

                for( EntityItem item : drops_as_entities )
                {
                    drops_as_itemstack_arraylist.add( item.getEntityItem() );
                }

                NBTHelper.saveInventory( player, drops_as_itemstack_arraylist );

                event.drops.clear();
                event.setCanceled( true );
            }
        }

        System.out.println( "canceled: " + event.isCanceled() );
    }
}