package me.wolfsurge.mixin.mixins;

import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.event.impl.PushEvent;
import me.xenodevs.xeno.event.impl.RotationEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.event.Event.Era;
import me.xenodevs.xeno.event.impl.MotionEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP implements Globals {

	@Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
	public void hookUWPPre(CallbackInfo info) {
		RotationEvent event = new RotationEvent();
		Xeno.EVENT_BUS.post(event);

		if (event.isCancelled()) {
			info.cancel();
		}
	}

	@Inject(method = {"onUpdateWalkingPlayer"}, at = {@At("TAIL")}, cancellable = true)
	public void hookUWPPost(CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();
		MotionEvent e = new MotionEvent(mc.player.posX, (mc.player.getEntityBoundingBox()).minY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround);
		Xeno.EVENT_BUS.post(e);
	}

}
