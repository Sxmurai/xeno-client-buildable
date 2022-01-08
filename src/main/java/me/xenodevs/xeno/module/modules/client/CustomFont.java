package me.xenodevs.xeno.module.modules.client;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;

public class CustomFont extends Module {
	
	public static NumberSetting SHADOW_SPACING = new NumberSetting("Shadow", 1, 0, 1, 0.05);
	public static ModeSetting font = new ModeSetting("Font", "Comfortaa", "Red Hat Mono");
	
	public CustomFont() {
		super("CustomFont", "change font to comfortaa", 0, Category.CLIENT);
		this.addSettings(SHADOW_SPACING);
	}

}
