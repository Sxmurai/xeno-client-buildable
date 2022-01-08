package me.wolfsurge.mixin.mixins;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.alt.GuiAltManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer extends GuiScreen {

    @Inject(method="drawScreen", at={@At("TAIL")})
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        Xeno.discordManager.update("In Multiplayer Menu");
    }

    @Inject(method="initGui", at={@At("TAIL")})
    public void initGui(CallbackInfo ci) {
        this.buttonList.add(new GuiButton(-100, 3, 3, "Alts"));
    }

    @Inject(method="actionPerformed", at={@At("TAIL")})
    protected void actionPerformed(GuiButton s1, CallbackInfo ci) throws IOException {
        switch (s1.id) {
            case -100:
                mc.displayGuiScreen(new GuiAltManager());
        }
    }
}
