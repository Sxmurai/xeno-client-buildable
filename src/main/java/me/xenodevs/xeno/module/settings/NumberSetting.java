package me.xenodevs.xeno.module.settings;

import me.xenodevs.xeno.module.Module;

public class NumberSetting extends Setting {
	
	public double value, minimum, maximum, increment, defaultValue;

	public NumberSetting(String name, double value, double minimum, double maximum, double increment) {
		this.name = name;
		this.value = value;
		this.defaultValue = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.increment = increment;
	}
	
	public NumberSetting(Setting par, String name, double value, double minimum, double maximum, double increment) {
		this.name = name;
		this.parent = par;
		this.value = value;
		this.defaultValue = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.increment = increment;
	}
	
	public NumberSetting(String name, Module m, double value, double minimum, double maximum, double increment) {
		this.name = name;
		this.value = value;
		this.defaultValue = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.increment = increment;
	}

	public float getFloatValue() {
		return (float) value;
	}

	public int getIntValue() {
		return (int) value;
	}

	public double getDoubleValue() {
		return value;
	}

	public void setValue(double value) {
		double precision = 1 / increment;
		this.value = Math.round(Math.max(minimum, Math.min(maximum,  value)) * precision) / precision;
	}
	
	public void setValue(int value) {
		double precision = 1 / increment;
		this.value = Math.round(Math.max(minimum, Math.min(maximum,  value)) * precision) / precision;
	}
	
	public void setValue(float value) {
		double precision = 1 / increment;
		this.value = Math.round(Math.max(minimum, Math.min(maximum,  value)) * precision) / precision;
	}

	public double getMinimum() {
		return minimum;
	}

	public double getMaximum() {
		return maximum;
	}
	
	public double getVal() {
		return value;
	}
	
	public void setVal(double val) {
		this.value = val;
	}
}
