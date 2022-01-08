package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.NumberSetting;

public class ReverseStep extends Module {

    public static ReverseStep INSTANCE;

    NumberSetting down = new NumberSetting("Down Height", 2, 1, 3, 0.5);

    public ReverseStep() {
        super("ReverseStep", "step but down", 0, Category.MOVEMENT);
        this.addSettings(down);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if(nullCheck())
            return;

        if (mc.world == null || mc.player == null || mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder() || mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }

        if (mc.player != null && mc.player.onGround && !mc.player.isInWater() && !mc.player.isOnLadder()) {
            for (double y = 0.0; y < down.getVal(); y += 0.01) {
                if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                    mc.player.motionY = -10.0;
                    break;
                }
            }
        }
    }
}