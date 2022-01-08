package me.xenodevs.xeno.module.settings;

public class StringSetting extends Setting {

    public String text, defaultValue;

    public StringSetting(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.text = defaultValue;
    }

    public StringSetting(Setting par, String name, String defaultValue) {
        this.name = name;
        this.parent = par;
        this.defaultValue = defaultValue;
        this.text = defaultValue;
    }

    public String getText() {
        return text;
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

}
