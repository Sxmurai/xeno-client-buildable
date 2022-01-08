package me.xenodevs.xeno.storage;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.friends.Friend;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.click.component.Frame;
import me.xenodevs.xeno.gui.hud.HudConfig;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.managers.ModuleManager;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.misc.FakePlayer;
import me.xenodevs.xeno.module.modules.movement.ElytraFly;
import me.xenodevs.xeno.module.settings.*;
import me.xenodevs.xeno.utils.render.Colour;

public class Config {

	public File configFolder = new File("Xeno");
	public File hudFolder = new File("Xeno/HUD");
	public File moduleFolder = new File("Xeno/Modules");
	public File hudParentFolder = new File("Xeno/HUD/ParentModules");
	public File clickGuiFolder = new File("Xeno/ClickGUI");
	public File miscFolder = new File("Xeno/Misc");
	
	public Configuration hudConfig;
	public Configuration moduleConfig;
	public Configuration clickGUIConfig;
	
	public Configuration hudConfigToSave = ConfigurationAPI.newConfiguration(new File("Xeno/HUD/HudConfiguration.json"));
	public Configuration moduleConfigToSave;
	public Configuration clickGUIConfigToSave = ConfigurationAPI.newConfiguration(new File("Xeno/ClickGUI/ClickGUI.json"));

	public void saveHudConfig() {	
		if(!configFolder.exists()) {
			configFolder.mkdirs();
		}
		
		if(!hudFolder.exists()) {
			hudFolder.mkdirs();
		}
		
		for(HUDMod m : Xeno.hudManager.hudMods) {
			hudConfigToSave.set(m.name.toLowerCase() + "X", m.getX());
			hudConfigToSave.set(m.name.toLowerCase() + "Y", m.getY());
			hudConfigToSave.set(m.name.toLowerCase() + "Enabled", m.parent.enabled);
		}

		for(Frame f : HudConfig.frames) {
			hudConfigToSave.set(f.category.name() + "X", f.getX());
			hudConfigToSave.set(f.category.name() + "Y", f.getY());
			hudConfigToSave.set(f.category.name() + "Open", f.isOpen());
		}
		
		try {
			hudConfigToSave.save();
		} catch (IOException e) {
		}		
	}
	
	public void loadHUDConfig() {
		if(!configFolder.exists()) {
			configFolder.mkdirs();
		}
		
		if(!hudFolder.exists()) {
			hudFolder.mkdirs();
		}
		
		try {
			hudConfig = ConfigurationAPI.loadExistingConfiguration(new File("Xeno/HUD/HudConfiguration.json"));
		} catch (IOException e) {}
	}
	
	public void saveModConfig(Module m) {	
		if(!configFolder.exists()) {
			configFolder.mkdirs();
		}
		
		if(!moduleFolder.exists()) {
			moduleFolder.mkdirs();
		}

		if(!hudParentFolder.exists()) {
			hudParentFolder.mkdirs();
		}

		if(m instanceof FakePlayer)
			return;

		if(m.isHudParent)
			moduleConfigToSave = ConfigurationAPI.newConfiguration(new File("Xeno/HUD/ParentModules" + m.name + ".json"));
		else
			moduleConfigToSave = ConfigurationAPI.newConfiguration(new File("Xeno/Modules/" + m.name + ".json"));

		moduleConfigToSave.setBool(m.name, m.isEnabled());
		
		for(Setting s : m.settings) {
			try {
				if(s instanceof BooleanSetting) {
					moduleConfigToSave.set("Bool_" + (s.parent != null ? s.parent.name : "") + s.name, (boolean) ((BooleanSetting) s).enabled);
				}
				
				if(s instanceof NumberSetting) {
					moduleConfigToSave.set("Num_" + (s.parent != null ? s.parent.name : "") + s.name, (double) ((NumberSetting) s).value);
				}
				
				if(s instanceof ModeSetting) {
					moduleConfigToSave.set("Mode_" + (s.parent != null ? s.parent.name : "") + s.name, (int) ((ModeSetting) s).index);
				}
				
				if(s instanceof KeybindSetting) {
					moduleConfigToSave.set("Key_" + (s.parent != null ? s.parent.name : "") + s.getName(), (int) ((KeybindSetting) s).getKeyCode());
				}
				
				if(s instanceof ColourPicker) {
					moduleConfigToSave.set("Col_" + (s.parent != null ? s.parent.name : "") + s.getName() + " R", ((ColourPicker) s).getColor().getRed());
					moduleConfigToSave.set("Col_" + (s.parent != null ? s.parent.name : "") + s.getName() + " G", ((ColourPicker) s).getColor().getGreen());
					moduleConfigToSave.set("Col_" + (s.parent != null ? s.parent.name : "") + s.getName() + " B", ((ColourPicker) s).getColor().getBlue());
					moduleConfigToSave.set("Col_" + (s.parent != null ? s.parent.name : "") + s.getName() + " A", ((ColourPicker) s).getColor().getAlpha());
					moduleConfigToSave.set("Col_" + (s.parent != null ? s.parent.name : "") + s.getName() + " Rainbow", ((ColourPicker) s).getRainbow());
				}

				if(s instanceof StringSetting) {
					moduleConfigToSave.set("Str_" + (s.parent != null ? s.parent.name : "") + s.getName(), ((StringSetting) s).getText());
				}
			} catch(Exception e) {
				
			}
		}
		
		try {
			moduleConfigToSave.save();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void loadModConfig() {
		if(!configFolder.exists()) {
			configFolder.mkdirs();
		}
		
		if(!moduleFolder.exists()) {
			moduleFolder.mkdirs();
		}

		if(!hudParentFolder.exists()) {
			hudParentFolder.mkdirs();
		}

		for(Module m : Xeno.moduleManager.modules) {
			try {
				if(m instanceof FakePlayer)
					continue;

				if(!m.isHudParent)
					moduleConfig = ConfigurationAPI.loadExistingConfiguration(new File("Xeno/Modules/" + m.name + ".json"));
				else
					moduleConfig = ConfigurationAPI.loadExistingConfiguration(new File("Xeno/HUD/ParentModules" + m.name + ".json"));
				m.enabled = (boolean) moduleConfig.get(m.name);
				
				if(m.enabled) {
					try {
						m.enable();
					} catch (Exception e) {
						
					}
				}
				
				for(Setting s : m.settings) {
					try {
						if (s instanceof BooleanSetting) {
							((BooleanSetting) s).setEnabled(moduleConfig.getBoolean("Bool_" + (s.parent != null ? s.parent.name : "") + s.getName()));
						}

						if (s instanceof NumberSetting) {
							((NumberSetting) s).setValue(moduleConfig.getDouble("Num_" + (s.parent != null ? s.parent.name : "") + s.getName(), ((NumberSetting) s).defaultValue));
						}

						if (s instanceof ModeSetting) {
							((ModeSetting) s).index = (int) moduleConfig.get("Mode_" + (s.parent != null ? s.parent.name : "") + s.name);
						}

						if (s instanceof KeybindSetting) {
							((KeybindSetting) s).setKeyCode((int) moduleConfig.getDouble("Key_" + (s.parent != null ? s.parent.name : "") + s.getName(), (double) ((KeybindSetting) s).defaultCode));
						}

						if (s instanceof ColourPicker) {
							Color c = new Color((int) moduleConfig.get("Col_" + (s.parent != null ? s.parent.name : "") + s.getName() + " R"), (int) moduleConfig.get("Col_" + (s.parent != null ? s.parent.name : "") + s.getName() + " G"), (int) moduleConfig.get("Col_" + (s.parent != null ? s.parent.name : "") + s.getName() + " B"), (int) moduleConfig.get("Col_" + (s.parent != null ? s.parent.name : "") + s.getName() + " A"));
							((ColourPicker) s).setValue(new Colour(c));
							((ColourPicker) s).setRainbow(moduleConfig.getBoolean("Col_" + (s.parent != null ? s.parent.name : "") + s.getName() + " Rainbow"));
						}

						if (s instanceof StringSetting) {
							((StringSetting) s).setText((String) moduleConfig.get("Str_" + (s.parent != null ? s.parent.name : "") + s.getName()));
						}
					} catch (Exception e) {}
				}
			} catch (IOException e) {} catch (NumberFormatException e) {} catch (NullPointerException e) {}
		}
	}
	
	public void saveClickGUIConfig() {	
		if(!configFolder.exists()) {
			configFolder.mkdirs();
		}
		
		if(!clickGuiFolder.exists()) {
			clickGuiFolder.mkdirs();
		}
		
		for(Frame f : Xeno.clickGui.frames) {
			clickGUIConfigToSave.set(f.category.name() + "X", f.getX());
			clickGUIConfigToSave.set(f.category.name() + "Y", f.getY());
			clickGUIConfigToSave.set(f.category.name() + "Open", f.isOpen());
		}
		
		try {
			clickGUIConfigToSave.save();
		} catch (IOException e) {
		}		
	}
	
	public void loadClickGUIConfig() {
		if(!configFolder.exists()) {
			configFolder.mkdirs();
		}
		
		if(!clickGuiFolder.exists()) {
			clickGuiFolder.mkdirs();
		}
		
		try {
			clickGUIConfig = ConfigurationAPI.loadExistingConfiguration(new File("Xeno/ClickGUI/ClickGUI.json"));
		} catch (IOException e) {} catch (Exception e) {}
	}
	
	public void saveFriendConfig() {	
		if(!configFolder.exists()) {
			configFolder.mkdirs();
		}
		
		if(!miscFolder.exists()) {
			miscFolder.mkdirs();
		}
		
		File file = new File(miscFolder + "/Friends.Xeno");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		try {
			PrintWriter pw = new PrintWriter(miscFolder + "/Friends.Xeno");
			for(Friend str : Xeno.friendManager.friends) {
				pw.println(str.getName());
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void loadFriendConfig() {
		if(!configFolder.exists()) {
			configFolder.mkdirs();
		}
		
		if(!miscFolder.exists()) {
			miscFolder.mkdirs();
		}
		
		ArrayList<String> lines = new ArrayList<String>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(miscFolder + "/Friends.Xeno"));
			String line = reader.readLine();
			while(line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			
		}
		
		for(String str : lines) {
			Xeno.friendManager.addFriend(str);
			Xeno.logger.info("Added friend: " + str);
		}
	}
	
	public void saveMisc() {
		if(!configFolder.exists()) {
			configFolder.mkdirs();
		}
		
		if(!miscFolder.exists()) {
			miscFolder.mkdirs();
		}

		try {
			PrintWriter pw = new PrintWriter(miscFolder + "/ElytraFlyActivate.Xeno");
			pw.println(ElytraFly.flightKey);
			pw.close();
		} catch (FileNotFoundException e) {e.printStackTrace();}

		try {
			PrintWriter pw = new PrintWriter(miscFolder + "/CustomMainMenu.Xeno");
			pw.println(String.valueOf(GuiUtil.customMainMenu));
			pw.close();
		} catch (FileNotFoundException e) {e.printStackTrace();}
	}
	
	public void loadMisc() {
		if(!configFolder.exists()) {
			configFolder.mkdirs();
		}
		
		if(!miscFolder.exists()) {
			miscFolder.mkdirs();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(miscFolder + "/ElytraFlyActivate.Xeno"));
			String line = reader.readLine();
			ElytraFly.flightKey = Integer.parseInt(line);
			reader.close();
		} catch (Exception e) {e.printStackTrace();}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(miscFolder + "/CustomMainMenu.Xeno"));
			String line = reader.readLine();
			GuiUtil.customMainMenu = Boolean.parseBoolean(line);
			reader.close();
		} catch (Exception e) {e.printStackTrace();}
	}

	public void savePreset(ModuleManager mm) {

	}
}
