package me.xenodevs.xeno.module.modules.render;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.ModeSetting;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class Fullbright extends Module {

	public static Fullbright INSTANCE;
	public static ModeSetting mode = new ModeSetting("Mode", "Gamma", "Effect");
	float oldBright;
	
	public Fullbright() {
		super("Fullbright", "makes everything bright", Category.RENDER);
		this.addSettings(mode);
		INSTANCE = this;
	}

	@Override
	public void onEnable() {
		oldBright = mc.gameSettings.gammaSetting;

        if (mode.getValue() == 0)
            mc.gameSettings.gammaSetting = +100;
	}
	
	@Override
	public void onDisable() {
		mc.player.removePotionEffect(MobEffects.NIGHT_VISION);

        if (mode.getValue() == 0)
            mc.gameSettings.gammaSetting = oldBright;
	}
	
	@Override
	public void onUpdate() {
		if (nullCheck())
            return;
		
		if (mode.is("Effect"))
            mc.player.addPotionEffect(new PotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 80950, 1, false, false)));
	}
	
	@Override
	public String getHUDData() {
		return " " + this.mode.getMode();
	}
}
