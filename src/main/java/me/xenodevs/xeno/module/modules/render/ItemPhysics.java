package me.xenodevs.xeno.module.modules.render;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;

public class ItemPhysics extends Module {

	public static ItemPhysics INSTANCE;
	public static NumberSetting scale = new NumberSetting("Scale", 0.5, 0.0, 5.0, 0.1);
	
	public ItemPhysics() {
		super("ItemPhysics", "gives physics to dropped items", 0, Category.RENDER);
		this.addSettings(scale);
		INSTANCE = this;
	}
}
