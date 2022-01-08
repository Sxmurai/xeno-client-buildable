package me.xenodevs.xeno.module.settings;

import java.awt.Color;
import java.util.function.Predicate;

import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.utils.render.Colour;

public class ColourPicker extends Setting {

    private boolean rainbow;
    public Colour value;
    
    public ColourPicker(String name, Colour colour) {
    	this.name = name;
    	this.value = colour;
    }

    public ColourPicker(String name, Colour colour, boolean doRainbow) {
        this.name = name;
        this.value = colour;
        this.rainbow = doRainbow;
    }

    public ColourPicker(String name, Color colour) {
        this.name = name;
        this.value = new Colour(colour);
    }

    public ColourPicker(Setting par, String name, Color colour) {
        this.name = name;
        this.parent = par;
        this.value = new Colour(colour);
    }

    public void doRainBow() {
        if (rainbow) {
            Color c = Colour.fromHSB((System.currentTimeMillis() % (360 * 32)) / (360f * 32), value.getSaturation(), value.getBrightness());
            setValue(new Colour(c.getRed(), c.getGreen(), c.getBlue(), value.getAlpha()));
        }
    }


    public void setValue(Color value) {
        this.value = new Colour(value);
    }

    public void setValue(int red, int green, int blue, int alpha) {
        this.value = new Colour(red, green, blue, alpha);
    }

    public Color getColor() {
    	this.doRainBow();
        return this.value;
    }

    public boolean getRainbow() {
        return this.rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }
}
