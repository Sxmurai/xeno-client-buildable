package me.wolfsurge.mixin.mixins;

import me.xenodevs.xeno.Xeno;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Render.class)
public class MixinRender<T extends Entity> {

    @Shadow
    @Final
    protected RenderManager renderManager;

    @Inject(method="renderLivingLabel",at={@At("HEAD")})
    public void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance, CallbackInfo info) {
        /* if(entityIn instanceof EntityTameable && Xeno.moduleManager.getModule("MobOwner").isEnabled()) {
            try {
                str = str + " [" + TextFormatting.GRAY + ((EntityTameable) entityIn).getOwner() + TextFormatting.RESET + "]";
            } catch (NullPointerException ignored) {} // required for some reason
        } */
        if(Xeno.moduleManager.getModule("MobOwner").isEnabled()) {
            info.cancel();

            double d0 = entityIn.getDistanceSq(this.renderManager.renderViewEntity);

            if (d0 <= (double)(maxDistance * maxDistance))
            {
                boolean flag = entityIn.isSneaking();
                float f = this.renderManager.playerViewY;
                float f1 = this.renderManager.playerViewX;
                boolean flag1 = this.renderManager.options.thirdPersonView == 2;
                float f2 = entityIn.height + 0.5F - (flag ? 0.25F : 0.0F);
                int i = "deadmau5".equals(str) ? -10 : 0;
                EntityRenderer.drawNameplate(Minecraft.getMinecraft().fontRenderer, str + (entityIn instanceof EntityTameable ? " [" + TextFormatting.GRAY + ((EntityTameable) entityIn).getOwner() + TextFormatting.RESET + "]" : ""), (float)x, (float)y + f2, (float)z, i, f, f1, flag1, flag);
            }
        }
    }
}
