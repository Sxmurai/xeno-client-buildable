package me.xenodevs.xeno.module.modules.render;
import java.awt.Color;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;

public class ESP extends Module {

	public static ESP INSTANCE;

	public static BooleanSetting passive = new BooleanSetting("Passive", true);
	public static ColourPicker passiveColour = new ColourPicker("Passive Colour", new Colour(Color.GREEN));
	public static BooleanSetting mobs = new BooleanSetting("Mobs", true);
	public static ColourPicker mobColour = new ColourPicker("Mob Colour", new Colour(Color.RED));
	public static BooleanSetting players = new BooleanSetting("Players", true);
	public static ColourPicker playerColour = new ColourPicker("Player Colour", new Colour(Color.CYAN));
	public static BooleanSetting items = new BooleanSetting("Items", true);
	public static ColourPicker itemColour = new ColourPicker("Item Colour", new Colour(Color.WHITE));
	public static BooleanSetting crystals = new BooleanSetting("Crystals", true);
	public static ColourPicker crystalsColour = new ColourPicker("Crystal Colour", new Colour(Color.PINK));
	public static NumberSetting lineWidth = new NumberSetting("Line Width", 3, 1, 3, 0.5);
	public static ModeSetting mode = new ModeSetting("Mode", "Outline", "Box", "Glow");
	public static BooleanSetting invisibles = new BooleanSetting("Invisibles", true);
	
	public ESP() {
		super("ESP", "highlights entities through walls", 0, Category.RENDER);
		this.addSettings(passive, mobs, players, items, crystals, passiveColour, mobColour, playerColour, crystalsColour, lineWidth, mode);
		INSTANCE = this;
	}

	@Override
	public void onEnable() {
		
	}
	
	@Override
	public void onDisable() {
		for(Entity e : mc.world.loadedEntityList) {
			if(e.isInvisible() && !invisibles.enabled)
				return;

			e.setGlowing(false);
		}
	}
	
	@Override
	public void onRenderWorld() {
		if(mode.is("Box")) {
			for(Entity e : mc.world.loadedEntityList) {
				if(e.isInvisible() && !invisibles.enabled)
					return;

				if(e instanceof EntityLiving && !(e instanceof EntityMob) && passive.isEnabled())
					RenderUtils3D.drawESPBox(e, (float) this.lineWidth.getDoubleValue(), this.passiveColour.getColor());
				
				if(e instanceof EntityMob && mobs.isEnabled())
					RenderUtils3D.drawESPBox(e, (float) this.lineWidth.getDoubleValue(), this.mobColour.getColor());
				
				if(e instanceof EntityOtherPlayerMP && passive.isEnabled())
					RenderUtils3D.drawESPBox(e, (float) this.lineWidth.getDoubleValue(), this.playerColour.getColor());

				if(e instanceof EntityEnderCrystal && crystals.isEnabled())
					RenderUtils3D.drawESPBox(e, (float) this.lineWidth.getDoubleValue(), this.playerColour.getColor());
			}
		}
		
		if(!mode.is("Glow")) {
			for(Entity e : mc.world.loadedEntityList) {
				if(e.isInvisible() && !invisibles.enabled)
					return;

				e.setGlowing(false);
			}
		}
		
		if(mode.is("Glow")) {
			for(Entity e : mc.world.loadedEntityList) {
				if(e.isInvisible() && !invisibles.enabled)
					return;
				if(e instanceof EntityLiving && !(e instanceof EntityMob) && passive.isEnabled())
					e.setGlowing(true);
				else if(e instanceof EntityMob && mobs.isEnabled())
					e.setGlowing(true);
				else if(e instanceof EntityPlayer && players.isEnabled() && e != mc.player)
					e.setGlowing(true);
			}
		}
	}
	
	@Override
	public String getHUDData() {
		return " " + this.mode.getMode();
	}
}
