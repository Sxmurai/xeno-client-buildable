package me.xenodevs.xeno.module.modules.player;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;

public class FastBreak extends Module {

	public FastBreak() {
		super("FastBreak", "removes break delay lel", Category.PLAYER);
	}

	@Override
	public void onUpdate() {
		mc.playerController.blockHitDelay = 0;
	}
}
