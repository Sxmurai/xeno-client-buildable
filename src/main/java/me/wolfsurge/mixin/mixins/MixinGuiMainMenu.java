package me.wolfsurge.mixin.mixins;

import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.mainmenu.custom.XenoMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.xenodevs.xeno.gui.mainmenu.MainMenuHook;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu {

    @Inject(method = {"drawScreen"}, at = {@At("TAIL")}, cancellable = true)
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if(GuiUtil.customMainMenu) {
            Minecraft.getMinecraft().displayGuiScreen(new XenoMainMenu());
            return;
        }

		MainMenuHook.render(mouseX, mouseY);
    }

    @Inject(method = {"mouseClicked"}, at = {@At("HEAD")})
    public void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        MainMenuHook.buttonPress(mouseX, mouseY, mouseButton);
    }

}
