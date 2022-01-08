package me.xenodevs.xeno.module.modules.player;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;

public class FastPlace extends Module {

	public FastPlace() {
		super("FastPlace", "removes place delay lel", Category.PLAYER);
	}

	@Override
	public void onUpdate() {
		mc.rightClickDelayTimer = 0;
	}
}
