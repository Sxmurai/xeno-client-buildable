package me.xenodevs.xeno.module.settings;

import java.util.Arrays;
import java.util.List;

import me.xenodevs.xeno.module.Module;

public class ModeSetting extends Setting {

	public interface method{
		void method();
	}
	public method method;

	public int index;
	public List<String> modes;
	
	public ModeSetting(String name, String... modes) {
		this.name = name;
		this.modes = Arrays.asList(modes);
		index = 0;
	}

	public ModeSetting(String name, method m, String... modes) {
		this.name = name;
		this.modes = Arrays.asList(modes);
		index = 0;
	}

	public ModeSetting(Setting par, String name, String... modes) {
		this.name = name;
		this.parent = par;
		this.modes = Arrays.asList(modes);
		index = 0;
	}
	
	public ModeSetting(String name, Module m, String... modes) {
		this.name = name;
		this.modes = Arrays.asList(modes);
		index = 0;
	}

	public void setMode(String mode) {
		int index = -1;
		for(String str : modes) {
			index++;
			if(str.equalsIgnoreCase(mode))
				break;
		}

		this.index = (index);
	}

	public String getMode() {
		return modes.get(index);
	}
	
	public boolean is(String mode) {
		return index == modes.indexOf(mode);
	}
	
	public void cycle() {
		if(index < modes.size() - 1) {
			index++;
		} else {
			index = 0;
		}
	}
	
	public int getValue() {
		return index;
	}
}
