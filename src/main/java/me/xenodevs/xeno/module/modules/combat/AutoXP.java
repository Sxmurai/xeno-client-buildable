package me.xenodevs.xeno.module.modules.combat;

import me.xenodevs.xeno.event.impl.PacketEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.player.InventoryUtil;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

public class AutoXP extends Module {

    BooleanSetting autoRepair = new BooleanSetting("AutoRepair", true);
    BooleanSetting switchh = new BooleanSetting("Switch", false);
    BooleanSetting switchBack = new BooleanSetting("Switch Back", true);
    NumberSetting dmg = new NumberSetting("Dmg", 90, 10, 90, 1);


    public AutoXP() {
        super("AutoXP", "repairs armour automatically (works well with fastxp)", Category.COMBAT);
        this.addSettings(autoRepair, switchh, switchBack, dmg);
    }

    @Override
    public void onUpdate() {
        if(nullCheck())
            return;

        if(autoRepair.isEnabled()) {
            boolean doRepair = false;
            for (ItemStack stack : mc.player.inventory.armorInventory) {
                boolean hasMending = false;
                NBTTagList enchants = stack.getEnchantmentTagList();
                for (int index = 0; index < enchants.tagCount(); ++index) {
                    short id = enchants.getCompoundTagAt(index).getShort("id");
                    Enchantment enc = Enchantment.getEnchantmentByID(id);
                    if (enc != null && enc.getName().equalsIgnoreCase("enchantment.mending"))
                        hasMending = true;
                }

                if (stack != null && hasMending && stack.getMaxDamage() - (float) stack.getItemDamage() <= dmg.getDoubleValue()) {
                    doRepair = true;
                }
            }

            if (doRepair) {
                if (switchh.isEnabled())
                    InventoryUtil.switchToSlot(Items.EXPERIENCE_BOTTLE);
                int slot = -1;
                if (switchh.isEnabled() && switchBack.isEnabled())
                    slot = mc.player.inventory.currentItem;
                if (InventoryUtil.isHolding(Items.EXPERIENCE_BOTTLE)) {
                    EnumHand hand = EnumHand.MAIN_HAND;
                    if (mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE)
                        hand = EnumHand.OFF_HAND;
                    mc.rightClickMouse();
                }
                if (switchh.isEnabled() && switchBack.isEnabled())
                    mc.player.inventory.currentItem = slot;
            }
        }
    }

    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if(event.getPacket() instanceof CPacketPlayerTryUseItem && mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle) {
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90.0f, mc.player.onGround));
        }
    });

}
