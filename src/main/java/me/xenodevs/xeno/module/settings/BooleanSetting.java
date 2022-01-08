package me.xenodevs.xeno.module.settings;

import me.xenodevs.xeno.module.Module;

public class BooleanSetting extends Setting {

	public boolean enabled;
	public boolean clickGuiFancy;
	
	public BooleanSetting(String name, boolean defaultEnabled) {
		this.name = name;
		this.enabled = defaultEnabled;
		this.clickGuiFancy = false;
	}
	
	public BooleanSetting(Setting par, String name, boolean defaultEnabled) {
		this.name = name;
		this.parent = par;
		this.enabled = defaultEnabled;
		this.clickGuiFancy = false;
	}
	
	public BooleanSetting(String name, Module m, boolean defaultEnabled) {
		this.name = name;
		this.enabled = defaultEnabled;
		this.clickGuiFancy = false;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void toggle() {
		enabled = !enabled;
	}
	
	public boolean getValue() {
		return enabled;
	}
	
}
