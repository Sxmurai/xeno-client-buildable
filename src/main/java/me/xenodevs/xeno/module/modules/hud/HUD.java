package me.xenodevs.xeno.module.modules.hud;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;

public class HUD extends Module {

	BooleanSetting editorReset = new BooleanSetting("EditorReset", false);
	public static BooleanSetting blur = new BooleanSetting("Blur", true);

	public HUD() {
		super("HUD", "display the hud (required for hud to display!)", Category.HUD);
		this.addSettings(blur);
	}
	
	@Override
	public void onNonToggledUpdate() {
		if(editorReset.isEnabled()) {
			Xeno.hudManager.reset();
			editorReset.toggle();
		}
	}
}
