package me.xenodevs.xeno.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.entity.EntityHelper;
import me.xenodevs.xeno.utils.entity.EntityUtil;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.player.InventoryUtil;
import me.xenodevs.xeno.utils.player.PlaceUtil;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Surround extends Module {

	public static Surround INSTANCE;
	
	BooleanSetting disable = new BooleanSetting("AutoDisable", false);
	ModeSetting rotate = new ModeSetting("Rotate", "Legit", "Packet", "None");
	NumberSetting delay = new NumberSetting("Delay", 100, 0, 300, 1);

	Timer timer = new Timer();
	
	public Surround() {
		super("Surround", "places obsidian around u", 0, Category.COMBAT);
		this.addSettings(disable, rotate, delay);
		INSTANCE = this;
	}

	@Override
	public void onUpdate() {
		if(nullCheck())
			return;

		Vec3d vec3d = EntityUtil.getInterpolatedPos(mc.player, 0.0F);

		BlockPos northBlockPos = (new BlockPos(vec3d)).north();
		BlockPos southBlockPos = (new BlockPos(vec3d)).south();
		BlockPos eastBlockPos = (new BlockPos(vec3d)).east();
		BlockPos westBlockPos = (new BlockPos(vec3d)).west();
		BlockPos downBlockPos = (new BlockPos(vec3d)).down();

		ArrayList<BlockPos> pos = new ArrayList<BlockPos>();
		pos.add(northBlockPos);
		pos.add(southBlockPos);
		pos.add(eastBlockPos);
		pos.add(westBlockPos);

		if((mc.world.getBlockState(downBlockPos).getBlock() == Blocks.AIR))
			pos.add(downBlockPos);

		InventoryUtil.switchToSlot(Blocks.OBSIDIAN);

		int count = 0;
		float oldYaw = mc.player.rotationYaw;
		float oldPitch = mc.player.rotationPitch;
		if(this.timer.hasTimeElapsed((long) delay.getDoubleValue(), true)) {
			for(BlockPos bp : pos) {
				if((mc.world.getBlockState(bp).getBlock() == Blocks.AIR)) {
					PlaceUtil.placeBlock(bp, !rotate.is("None"), rotate.is("Packet"));

					count++;
				}
			}
		}

		mc.player.rotationYaw = oldYaw;
		mc.player.rotationPitch = oldPitch;
		
		if(disable.enabled) {
			this.toggle();
		}
	}
	
	public void place() {

	}
}
