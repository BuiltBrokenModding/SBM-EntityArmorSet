package com.builtbroken.armorset;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-06.
 */
@Mod(modid = EntityArmorSetter.MOD_ID, name = "[SBM] Entity Armor Setter", version = EntityArmorSetter.VERSION)
@Mod.EventBusSubscriber
public class EntityArmorSetter
{
    public static final String MOD_ID = "entityarmorsetter";

    public static final String MAJOR_VERSION = "@MAJOR@";
    public static final String MINOR_VERSION = "@MINOR@";
    public static final String REVISION_VERSION = "@REVIS@";
    public static final String BUILD_VERSION = "@BUILD@";
    public static final String MC_VERSION = "@MC@";
    public static final String VERSION = MC_VERSION + "-" + MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION + "." + BUILD_VERSION;

    @SubscribeEvent
    public static void onMobInteraction(PlayerInteractEvent.EntityInteract event)
    {
        if (!event.getWorld().isRemote && event.getEntityPlayer().isCreative())
        {
            final ItemStack stack = event.getItemStack();
            if(!stack.isEmpty())
            {

                //Mod support, allow item to define its insert slot
                EntityEquipmentSlot slot = stack.getItem().getEquipmentSlot(stack);

                //Default logic
                if (slot == null)
                {
                    if (stack.getItem() != Item.getItemFromBlock(Blocks.PUMPKIN) && stack.getItem() != Items.SKULL)
                    {
                        if (stack.getItem() instanceof ItemArmor)
                        {
                            slot = ((ItemArmor) stack.getItem()).armorType;
                        }
                        else if (stack.getItem() == Items.ELYTRA)
                        {
                            slot = EntityEquipmentSlot.CHEST;
                        }
                        //Shields are forced to offhand
                        else if (stack.getItem().isShield(stack, null))
                        {
                            slot = EntityEquipmentSlot.OFFHAND;
                        }
                        //Allow inverting hand with sneak
                        else if (event.getEntityPlayer().isSneaking())
                        {
                            slot = EntityEquipmentSlot.OFFHAND;
                        }
                        else
                        {
                            slot = EntityEquipmentSlot.MAINHAND;
                        }
                        //TODO add ctrl to force blocks into head slot
                    }
                    else
                    {
                        slot = EntityEquipmentSlot.HEAD;
                    }
                }


                //Insert
                if (slot != null)
                {
                    event.getTarget().setItemStackToSlot(slot, stack.copy());
                }

                event.setCanceled(true);
                event.setCancellationResult(EnumActionResult.SUCCESS);
            }
        }
    }
}
