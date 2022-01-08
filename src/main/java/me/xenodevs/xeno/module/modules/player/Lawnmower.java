package me.xenodevs.xeno.module.modules.player;

import io.netty.util.internal.MathUtil;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.block.BlockUtil;
import me.xenodevs.xeno.utils.other.MathUtils;
import me.xenodevs.xeno.utils.player.RotationUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class Lawnmower extends Module {

    NumberSetting range = new NumberSetting("Range", 5, 1, 7, 1);

    BooleanSetting packetRotate = new BooleanSetting("Packet Rotate", false);
    BooleanSetting rotateBack = new BooleanSetting("Rotate Back", true);

    BooleanSetting redFlower = new BooleanSetting("Red Flower", true);
    BooleanSetting yellowFlower = new BooleanSetting("Yellow Flower", true);
    BooleanSetting grass = new BooleanSetting("Grass", true);
    BooleanSetting tallGrass = new BooleanSetting("Tall Grass", true);

    public Lawnmower() {
        super("Lawnmower", "mows the lawn", Category.PLAYER);
        this.addSettings(range, packetRotate, rotateBack, redFlower, yellowFlower, grass, tallGrass);
    }

    @Override
    public void onUpdate() {
        if(nullCheck())
            return;

        ArrayList<BlockPos> validBlocks = new ArrayList<>();

        for(BlockPos pos : BlockUtil.getNearbyBlocks(mc.player, range.getFloatValue(), false)) {
            if(isBlockValid(getBlock(pos)))
                validBlocks.add(pos);
        }

        int blocksBroken = 0;

        if(!validBlocks.isEmpty()) {
            for(BlockPos pos : validBlocks) {
                if(blocksBroken < 1) {
                    float yaw = mc.player.rotationYaw;
                    float pitch = mc.player.rotationPitch;

                    RotationUtil.rotateToBlockPos(pos, packetRotate.isEnabled());

                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.playerController.clickBlock(pos, EnumFacing.UP);

                    if (rotateBack.isEnabled()) {
                        mc.player.rotationYaw = yaw;
                        mc.player.rotationPitch = pitch;
                    }

                    blocksBroken++;
                }
            }
        }
    }

    private BlockPos getNearestBlock() {
        float currRange = range.getFloatValue();
        BlockPos fin = null;
        for(BlockPos pos : BlockUtil.getNearbyBlocks(mc.player, range.getFloatValue(), false)) {
            if(isBlockValid(getBlock(pos)))
                if(mc.player.getDistanceSq(pos) < MathUtils.square(currRange)) {
                    currRange = (float) mc.player.getDistanceSq(pos);
                    fin = pos;
                }
        }

        return fin;
    }

    private boolean isBlockValid(Block b) {
        return redFlower.isEnabled() && b == Blocks.RED_FLOWER ||
                tallGrass.isEnabled() && b == Blocks.TALLGRASS ||
                tallGrass.isEnabled() && b == Blocks.DOUBLE_PLANT ||
                yellowFlower.isEnabled() && b == Blocks.YELLOW_FLOWER;
    }

    private Block getBlock(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock();
    }
}
