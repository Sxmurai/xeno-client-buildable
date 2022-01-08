package me.xenodevs.xeno.managers;

import java.util.ArrayList;
import java.util.Collections;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.gui.hud.modules.impl.*;
import me.xenodevs.xeno.module.modules.hud.ArrayListModule;

public class HudManager {

	public ArrayList<HUDMod> hudMods = new ArrayList<HUDMod>();
	
	public HudManager() {	
		hudMods.add(new HUDClientName());
		// hudMods.add(new HUDArrayList());
		hudMods.add(new HUDWelcomer());
		hudMods.add(new HUDFPS());
		hudMods.add(new HUDPing());
		hudMods.add(new HUDCoords());
		hudMods.add(new HUDTPS());
		hudMods.add(new HUDArmour());
		hudMods.add(new HUDInventory());
		hudMods.add(new HUDTotems());
		
		for(HUDMod m : hudMods) {
			m.sub();
		}

		Collections.reverse(hudMods);

		Xeno.logger.info("Initialized HUD Manager");
	}
	
	public void reset() {
		for(HUDMod m : hudMods) {
			m.x = m.defaultX;
			m.y = m.defaultY;
			m.setEnabled(false);
			
			Xeno.config.saveHudConfig();
		}

		Collections.reverse(hudMods);
	}
	
	public void renderMods() {
		for(HUDMod m : hudMods) {
			if(m.parent.enabled)
				m.draw();
		}

		ArrayListModule.drawArrayList();
	}	 
	
	public HUDMod getModule(String name) {
		for(HUDMod m : hudMods) {
			if(m.name == name) {
				return m;
			}
		}
		
		return null;
	}
}
