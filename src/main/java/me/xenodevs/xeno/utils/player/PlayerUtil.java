package me.xenodevs.xeno.utils.player;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.managers.TickManager;
import org.lwjgl.input.Mouse;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PlayerUtil {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	
	public static int findObiInHotbar() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (block instanceof BlockEnderChest)
                    return i;
                else if (block instanceof BlockObsidian)
                    return i;
            }
        }
        return -1;
    }

	public static double getHealth() {
        return mc.player.getHealth() + mc.player.getAbsorptionAmount();
    }

    public static void attackEntity(Entity entity, boolean packet, boolean cooldown, boolean sync, boolean swing) {
        if (!cooldown || (mc.player.getCooledAttackStrength(sync ? (20 - Xeno.tickManager.getTPS(TickManager.TPS.CURRENT)) : 0) >= 1)) {
            if (packet)
                mc.player.connection.sendPacket(new CPacketUseEntity(entity));
            else
                mc.playerController.attackEntity(mc.player, entity);

            if(swing)
                mc.player.swingArm(EnumHand.MAIN_HAND);

            mc.player.resetCooldown();
        }
    }

    public static boolean isTrapped() {
        BlockPos playerPos = new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));

        BlockPos[] trapPos = {
                playerPos.down(),
                playerPos.up().up(),
                playerPos.north(),
                playerPos.south(),
                playerPos.east(),
                playerPos.west(),
                playerPos.north().up(),
                playerPos.south().up(),
                playerPos.east().up(),
                playerPos.west().up(),
        };

        for (BlockPos pos : trapPos) {
            IBlockState state = mc.world.getBlockState(pos);

            if (state.getBlock() != Blocks.OBSIDIAN && mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK)
                return false;
        }

        return true;
    }

    public static Vec3d getCenter(double posX, double posY, double posZ) {
        return new Vec3d(Math.floor(posX) + 0.5D, Math.floor(posY), Math.floor(posZ) + 0.5D);
    }
	
	public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }
	
	public static void lockLimbs() {
        mc.player.prevLimbSwingAmount = 0;
        mc.player.limbSwingAmount = 0;
        mc.player.limbSwing = 0;
    }
	
	public static boolean isEating() {
        return mc.player.getHeldItemMainhand().getItemUseAction().equals(EnumAction.EAT) || mc.player.getHeldItemMainhand().getItemUseAction().equals(EnumAction.DRINK);
    }

    public static boolean isMending() {
        return InventoryUtil.isHolding(Items.EXPERIENCE_BOTTLE) && Mouse.isButtonDown(1);
    }

    public static boolean isMining() {
        return InventoryUtil.isHolding(Items.DIAMOND_PICKAXE) && mc.playerController.getIsHittingBlock();
    }

    public static boolean isInLiquid() {
        return mc.player.isInLava() || mc.player.isInWater();
    }

    public static boolean isCollided() {
        return mc.player.collidedHorizontally || mc.player.collidedVertically;
    }
}
