package me.xenodevs.xeno.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.KeybindSetting;
import me.xenodevs.xeno.module.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class Module implements Globals {

    public String name, description;
    public KeybindSetting keyCode = new KeybindSetting(0);
    public BooleanSetting visible = new BooleanSetting("Visible", true);
    public Category category;
    public boolean enabled;
    public boolean isHudParent;

    public Minecraft mc = Minecraft.getMinecraft();

    public List<Setting> settings = new ArrayList<Setting>();

    public Module(String name, String description, Category category, boolean isHudParent) {
        this.name = name;
        this.description = description;
        this.keyCode.code = 0;
        this.category = category;

        if(isHudParent) {
            visible = new BooleanSetting("Visible", false);
            isHudParent = true;
        }

        this.addSettings(visible);
        this.addSettings(this.keyCode);
        setup();
    }

    public Module(String name, String description, Category category) {
        isHudParent = false;
        this.name = name;
        this.description = description;
        this.keyCode.code = 0;
        this.category = category;

        this.addSettings(visible);
        this.addSettings(this.keyCode);
        setup();
    }
    
    public Module(String name, String description, int key, Category category) {
        isHudParent = false;
        this.name = name;
        this.description = description;
        this.keyCode.code = key;
        this.category = category;

        this.addSettings(visible);
        this.addSettings(this.keyCode);
        setup();
    }
    
    public void setup() {
    	
    }
    
    public void addSetting(Setting setting) {
    	this.settings.add(setting);
    	this.settings.sort(Comparator.comparingInt(s -> s == visible ? 1 : 0));
        this.settings.sort(Comparator.comparingInt(s -> s == keyCode ? 1 : 0));
    }

    public void addSettings(Setting... settings) {
        this.settings.addAll(Arrays.asList(settings));
        this.settings.sort(Comparator.comparingInt(s -> s == visible ? 1 : 0));
        this.settings.sort(Comparator.comparingInt(s -> s == keyCode ? 1 : 0));
    }

    public void onUpdate() {}   
    public void onNonToggledUpdate() {}
    public void onRenderWorld() {}
    public void onRenderGUI() {}
    public void onFastUpdate() {}
	public void onServerUpdate() {}

    public void enable() { // Don't touch
        MinecraftForge.EVENT_BUS.register(this);
        Xeno.EVENT_BUS.subscribe(this);
        onEnable();
    }

    public void disable() { // Don't touch
        MinecraftForge.EVENT_BUS.unregister(this);
        Xeno.EVENT_BUS.unsubscribe(this);
        onDisable();
    }

    public void onEnable() {}
    public void onDisable() {}

    public void toggle() {
        this.enabled = !enabled;

        if(enabled)
            enable();
        else
            disable();
    }

    public String getName() {
        return name;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public int getKey() {
        return keyCode.code;
    }
    public boolean nullCheck() {
    	if(mc.player == null || mc.world == null)
    		return true;
    	
        return false;
    }

    public String getHUDData() {
    	return "";
    }
    
}
