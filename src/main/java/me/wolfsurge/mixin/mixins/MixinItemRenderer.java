package me.wolfsurge.mixin.mixins;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.event.impl.TransformFirstPersonEvent;
import me.xenodevs.xeno.module.modules.render.ViewModel;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public final class MixinItemRenderer {
    @Inject(method = "transformEatFirstPerson", at = @At("HEAD"), cancellable = true)
    public void transformEatFirstPerson(float p_187454_1_, EnumHandSide enumHandSide, ItemStack itemStack, CallbackInfo callbackInfo) {
        TransformFirstPersonEvent event = new TransformFirstPersonEvent.Pre(enumHandSide);
        Xeno.EVENT_BUS.post(event);

        if (Xeno.moduleManager.getModuleByName("ViewModel").isEnabled() && ViewModel.cancelEating.getValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "transformFirstPerson", at = @At("HEAD"))
    public void transformFirstPersonHead(EnumHandSide enumHandSide, float p_187453_2_, CallbackInfo callbackInfo) {
        TransformFirstPersonEvent event = new TransformFirstPersonEvent.Pre(enumHandSide);
        Xeno.EVENT_BUS.post(event);
    }

    @Inject(method = "transformFirstPerson", at = @At("TAIL"))
    public void transformFirstPersonTail(EnumHandSide enumHandSide, float p_187453_2_, CallbackInfo callbackInfo) {
        TransformFirstPersonEvent event = new TransformFirstPersonEvent.Post(enumHandSide);
        Xeno.EVENT_BUS.post(event);
    }

    @Inject(method = "transformSideFirstPerson", at = @At("HEAD"))
    public void transformSideFirstPerson(EnumHandSide enumHandSide, float p_187459_2_, CallbackInfo callbackInfo) {
        TransformFirstPersonEvent event = new TransformFirstPersonEvent.Pre(enumHandSide);
        Xeno.EVENT_BUS.post(event);
    }
}
