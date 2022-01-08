package me.xenodevs.xeno.module.modules.combat;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.block.BlockUtil;
import me.xenodevs.xeno.utils.block.hole.Hole;
import me.xenodevs.xeno.utils.block.hole.HoleUtil;
import me.xenodevs.xeno.utils.other.MathUtils;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.player.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;

public class HoleFill extends Module {
    NumberSetting range = new NumberSetting("Range", 5, 2, 7, 1);
    NumberSetting placeMS = new NumberSetting("PlaceDelayMS", 20, 0, 40, 1);
    BooleanSetting swing = new BooleanSetting("Swing", true);
    BooleanSetting rotate = new BooleanSetting("Rotate", false);
    BooleanSetting rotateBack = new BooleanSetting("RotateBack", false);

    Timer timer = new Timer();

    public HoleFill() {
        super("HoleFill", "fills all the holes :smirk: :smirk: :smirk:", Category.COMBAT);
        this.addSettings(range, placeMS, swing, rotate, rotateBack);
    }

    @Override
    public void onUpdate() {
        if(nullCheck())
            return;

        doHoleFill();
    }

    public void doHoleFill() {
        for(EntityPlayer entityPlayer : mc.world.playerEntities) {
            if(entityPlayer != mc.player && mc.player.getDistanceSq(entityPlayer) <= MathUtils.square(range.getFloatValue())) {
                for(Hole h : HoleUtil.getHoles(5, entityPlayer)) {
                    if(timer.hasTimeElapsed((long) placeMS.getDoubleValue(), true) && !HoleUtil.isInHole(mc.player))
                        BlockUtil.placeBlock(h.hole, PlayerUtil.findObiInHotbar(), rotate.isEnabled(), rotateBack.isEnabled(), swing.isEnabled());
                }
            }
        }
    }
}
