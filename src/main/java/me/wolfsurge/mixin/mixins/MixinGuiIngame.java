package me.wolfsurge.mixin.mixins;

import me.xenodevs.xeno.module.modules.render.NoRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.xenodevs.xeno.Xeno;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {
	
	@Inject(method = "renderPotionEffects", at = {@At("HEAD")}, cancellable = true)
	public void hookPotions(ScaledResolution sr, CallbackInfo info) {
		if(Xeno.moduleManager.isModuleEnabled("NoRender") && NoRender.potions.isEnabled())
			info.cancel();
	}

	@Inject(method={"renderPortal"}, at = {@At("HEAD")}, cancellable = true)
	public void hookPortal(CallbackInfo info) {
		if(Xeno.moduleManager.isModuleEnabled("NoRender") && NoRender.portal.isEnabled()) {
			info.cancel();
		}
	}
}

