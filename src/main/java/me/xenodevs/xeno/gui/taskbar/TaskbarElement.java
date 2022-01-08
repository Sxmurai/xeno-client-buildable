package me.xenodevs.xeno.gui.taskbar;

import me.wolfsurge.api.util.Globals;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class TaskbarElement implements Globals {

    public String name;
    public ResourceLocation imageLocation;
    public int x;
    public int y;

    public TaskbarElement(String name, String imagePath, int x) {
        ScaledResolution sr = new ScaledResolution(mc);
        this.name = name;
        this.imageLocation = new ResourceLocation("xeno", imagePath);
        this.x = x;
        this.y = sr.getScaledHeight() - 64;
    }

    public void update() {ScaledResolution sr = new ScaledResolution(mc); this.y = sr.getScaledHeight() - 64;}
    public void render() {}
    public void onClick() {}

}
