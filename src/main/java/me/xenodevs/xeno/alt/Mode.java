package me.xenodevs.xeno.alt;

public enum Mode {
    MOJANG,
    MICROSOFT;

    public Mode currentMode;

    public void updateService(Mode m) {
        this.currentMode = m;
    }
}
