package me.xenodevs.xeno.utils.render;

import java.awt.Dimension;
import java.awt.Toolkit;

public class DisplayUtils {
	
	public static int getDisplayWidth() {
	    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
	    
	    // width will store the width of the screen
	    int width = (int) size.getWidth() / 2;
	    
	    return width;
	}
	
	public static int getDisplayHeight() {
	    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
	    
	    // height will store the height of the screen
	    int height = (int) size.getHeight() / 2;
	    
	    return height;
	}

}
