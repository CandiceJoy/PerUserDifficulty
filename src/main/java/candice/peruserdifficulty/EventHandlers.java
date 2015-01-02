package candice.peruserdifficulty;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

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

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = false)
    public void onEvent( PlayerDropsEvent event )
    {
        Entity entity = event.entity;

        if( !(entity instanceof EntityPlayer ) )
        {
            return;
        }

        EntityPlayer player = (EntityPlayer)entity;
        PlayerDifficulty difficulty = NBTHelper.getDifficultyLevel( player );

        if( difficulty != null && difficulty != PlayerDifficulty.DISABLED )
        {
            if( PlayerDifficultyHelper.shouldDoKeepInventory( difficulty ) )
            {
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
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
    public void onEvent( PlayerUseItemEvent.Finish event )
    {
        Entity entity = event.entity;

        if( !( entity instanceof EntityPlayer ) )
        {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;
        PlayerDifficulty difficulty = NBTHelper.getDifficultyLevel( player );

        ItemStack stack = event.item;
        Item item = stack.getItem();

        if( item instanceof ItemFood )
        {
            ItemFood food = (ItemFood) item;

            double food_modifier = PlayerDifficultyHelper.getFoodMultiplier( difficulty );
            double saturation_modifier = PlayerDifficultyHelper.getSaturationMultiplier( difficulty );
            int food_level = food.func_150905_g( stack );
            double saturation_level = food.func_150906_h( stack );
            int food_to_add = (int) Math.round( food_level * ( food_modifier - 1.0 ) );
            float saturation_to_add = (float) ( saturation_level * ( saturation_modifier - 1.0 ) );

            player.getFoodStats().addStats( food_to_add, saturation_to_add );
        }
    }
}