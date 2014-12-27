package candice.peruserdifficulty;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * Created by Candice on 12/26/2014.
 */
public class EventHandlersCommon
{
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
    public void onEvent( PlayerEvent.PlayerRespawnEvent event )
    {
        System.out.println( "RESPAWN!" );
        EntityPlayer player = event.player;
        ArrayList<ItemStack> items = NBTHelper.getSavedInventory( player );

        if( items != null && items.size() > 0 )
        {
            System.out.println( "Repopulating Inventory!" );

            for( ItemStack stack : items )
            {
                player.inventory.addItemStackToInventory( stack );
            }

            items.clear();
            NBTHelper.eraseSavedInventory( player );
        }
    }
}
