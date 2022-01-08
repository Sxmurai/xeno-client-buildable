package me.xenodevs.xeno.module.settings;

public class KeybindSetting extends Setting {
	
	public int code, defaultCode;
	
	public KeybindSetting(int code) {
		this.name = "Key";
		this.code = code;
		this.defaultCode = code;
	}
	
	public KeybindSetting(String name, int code) {
		this.name = name;
		this.code = code;
		this.defaultCode = code;
	}

	public KeybindSetting(Setting par, String name, int code) {
		this.name = name;
		this.parent = par;
		this.code = code;
		this.defaultCode = code;
	}

	public int getKeyCode() {
		return code;
	}

	public void setKeyCode(int code) {
		this.code = code;
	}

}
