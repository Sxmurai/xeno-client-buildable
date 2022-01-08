package me.xenodevs.xeno.module.modules.misc;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.module.settings.StringSetting;
import me.xenodevs.xeno.utils.entity.EntityFakePlayer;
import me.xenodevs.xeno.utils.render.RenderUtils3D;

import java.awt.*;

public class FakePlayer extends Module {

    StringSetting plrName = new StringSetting("Name", "FakePlayer");
    BooleanSetting trace = new BooleanSetting("Tracer", true);
    NumberSetting lineWidth = new NumberSetting("LineWidth", 0.5, 0.1, 2, 0.1);
    ColourPicker traceCol = new ColourPicker("TracerColour", Color.CYAN);

    EntityFakePlayer fake = null;

    public FakePlayer() {
        super("FakePlayer", "spawns you... but it ain't you", Category.MISC);
        this.addSettings(trace, lineWidth, traceCol);
    }

    @Override
    public void onRenderWorld() {
        if(trace.isEnabled() && fake != null) {
            RenderUtils3D.drawTracer(fake, lineWidth.getFloatValue(), traceCol.getColor());
        }
    }

    @Override
    public void onEnable() {
        fake = new EntityFakePlayer(plrName.getText());
        mc.world.playerEntities.add(fake);
    }

    @Override
    public void onDisable() {
        fake.despawn();
        fake = null;
    }
}
