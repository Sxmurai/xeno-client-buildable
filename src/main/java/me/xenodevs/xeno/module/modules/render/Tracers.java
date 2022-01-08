package me.xenodevs.xeno.module.modules.render;

import java.awt.Color;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.entity.EntityHelper;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.math.Vec3d;

public class Tracers extends Module {
    public static BooleanSetting passive = new BooleanSetting("Passives", true);
    public static ColourPicker passiveColour = new ColourPicker("Passive Colour", new Colour(Color.GREEN));
    public static BooleanSetting mobs = new BooleanSetting("Mobs", true);
    public static ColourPicker mobColour = new ColourPicker("Mob Colour", new Colour(Color.RED));
    public static BooleanSetting players = new BooleanSetting("Players", true);
    public static ColourPicker playerColour = new ColourPicker("Player Colour", new Colour(Color.WHITE));
    NumberSetting lineWidth = new NumberSetting("Width", 1, 0.1, 2, 0.1);
    public Tracers() {
        super("Tracers", "draws lines to entities", 0, Category.RENDER);
        this.addSettings(passive, passiveColour, mobs, mobColour, players, playerColour, lineWidth);
    }

    @Override
    public void onRenderWorld() {
        mc.gameSettings.viewBobbing = false;

        for(Entity e : mc.world.loadedEntityList) {
            if(e instanceof EntityLiving && !(e instanceof EntityMob) && passive.enabled) {
                RenderUtils3D.drawTracer(e, lineWidth.getFloatValue(), passiveColour.getColor());
            }

            if(e instanceof EntityLiving && e instanceof EntityMob && mobs.enabled) {
                RenderUtils3D.drawTracer(e, lineWidth.getFloatValue(), mobColour.getColor());
            }

            if(e instanceof EntityOtherPlayerMP && players.enabled) {
                RenderUtils3D.drawTracer(e, lineWidth.getFloatValue(), playerColour.getColor());
            }
        }
    }
}