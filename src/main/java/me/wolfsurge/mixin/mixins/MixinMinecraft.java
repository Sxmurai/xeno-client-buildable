package me.wolfsurge.mixin.mixins;

import fr.lavache.anime.Delta;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.mainmenu.custom.XenoMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.world.WorldSettings;
import org.lwjgl.Sys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    long lastFrame = (Sys.getTime() * 1000) / Sys.getTimerResolution();;

    @Inject(method={"init"}, at={@At("TAIL")})
    private void displayMainMenu(CallbackInfo ci) {
        if(GuiUtil.customMainMenu)
            Minecraft.getMinecraft().displayGuiScreen(new XenoMainMenu());
    }

    @Inject(method = "launchIntegratedServer", at={@At("TAIL")})
    public void singlePlayerDiscordRP(String minecraftsessionservice, String gameprofilerepository, WorldSettings playerprofilecache, CallbackInfo ci) {
        Xeno.discordManager.update("Playing Singleplayer");
    }

    @Inject(method="runGameLoop", at={@At("HEAD")})
    public void runGameLoop(CallbackInfo ci) {
        long currentTime = (Sys.getTime() * 1000) / Sys.getTimerResolution();
        int deltaTime = (int) (currentTime - lastFrame);
        lastFrame = currentTime;
        Delta.DELTATIME = deltaTime;
    }

}
