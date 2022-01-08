package me.xenodevs.xeno.module.modules.combat;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.NumberSetting;
import net.minecraft.init.Items;

public class FastXP extends Module {

    NumberSetting delay = new NumberSetting("Delay", 0, 0, 3, 1);

    public FastXP() {
        super("FastXP", "throw xp bottles very fast", Category.COMBAT);
        this.addSettings();
    }

    @Override
    public void onUpdate() {
        if(nullCheck())
            return;

        if((mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE || mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE)) {
            mc.rightClickDelayTimer = 0;
        }
    }
}
