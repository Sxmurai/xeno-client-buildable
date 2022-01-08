package me.xenodevs.xeno.module.settings;

public class ButtonSetting extends Setting {

    public interface method{
        void method();
    }
    public method method;

    public ButtonSetting(String name, method m) {
        this.name = name;
        this.method = m;
    }

    public void onClick() {
        method.method();
    }

}
