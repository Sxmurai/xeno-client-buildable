package me.xenodevs.xeno.module.modules.misc;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.utils.player.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Mouse;

public class MCP extends Module {

    private boolean clicked = false;

    BooleanSetting silentSwitch = new BooleanSetting("SilentSwitch", true);
    BooleanSetting switchBack = new BooleanSetting("SwitchBack", true);

    public MCP() {
        super("MCP", "throws a pearl when you middle click", Category.MISC);
        this.addSettings(silentSwitch, switchBack);
    }

    @Override
    public void onUpdate() {
        if(Mouse.isButtonDown(2)) {
            if (!this.clicked && mc.currentScreen == null) {
                this.onClick();
            }
            this.clicked = true;
        } else {
            this.clicked = false;
        }
    }

    private void onClick() { // techale is triggered
        int currSlot = mc.player.inventory.currentItem;
        if(silentSwitch.isEnabled()) {
            if(mc.player.getHeldItemOffhand().getItem() != Items.ENDER_PEARL && mc.player.getHeldItemMainhand().getItem() != Items.ENDER_PEARL) {
                if(InventoryUtil.hasItem(Items.ENDER_PEARL)) {
                    boolean hasPearlInHotbar = false;

                    for(int i = 0; i < 9; i++) {
                        Item item = mc.player.inventory.getStackInSlot(i).getItem();
                        if (item instanceof ItemEnderPearl) {
                            hasPearlInHotbar = true;
                            break;
                        }
                    }

                    if(hasPearlInHotbar) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(InventoryUtil.getHotbarItemSlot(Items.ENDER_PEARL)));
                        mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);

                        if (switchBack.isEnabled())
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(currSlot));
                    }
                }
            } else {
                EnumHand e = EnumHand.MAIN_HAND;
                if(mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL)
                    e = EnumHand.OFF_HAND;
                mc.playerController.processRightClick(mc.player, mc.world, e);
            }
        } else {
            if (mc.player.getHeldItemOffhand().getItem() != Items.ENDER_PEARL && mc.player.getHeldItemMainhand().getItem() != Items.ENDER_PEARL)
                InventoryUtil.switchToSlot(Items.ENDER_PEARL);

            if (InventoryUtil.isHolding(Items.ENDER_PEARL)) {
                EnumHand e = EnumHand.MAIN_HAND;
                if (mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL)
                    e = EnumHand.OFF_HAND;
                mc.playerController.processRightClick(mc.player, mc.world, e);

                if (switchBack.isEnabled())
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(currSlot));
            }
        }

        this.clicked = true;
    }

}
