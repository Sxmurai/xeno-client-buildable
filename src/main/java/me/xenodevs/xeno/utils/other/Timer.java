package me.xenodevs.xeno.utils.other;

public class Timer {
	
	public long lastMS = System.currentTimeMillis();
	
	public void reset() {
		lastMS = System.currentTimeMillis();
	}
	
	public boolean hasTimeElapsed(long time, boolean reset) {
		if(System.currentTimeMillis() - lastMS > time) {
			if(reset)
				reset();
			
			return true;
		}
		
		return false;
	}
	
    public Timer() {
        this.lastMS = 0L;
    }

    public boolean delay(float milliSec) {
        return (float) (getTime() - this.lastMS) >= milliSec;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public long getDifference() {
        return getTime() - this.lastMS;
    }

    public void setDifference(long difference) {
        this.lastMS = (getTime() - difference);
    }
    
}
