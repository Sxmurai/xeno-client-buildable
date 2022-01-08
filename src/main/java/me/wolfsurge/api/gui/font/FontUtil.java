package me.wolfsurge.api.gui.font;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.managers.FontManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FontUtil {
    public static volatile int completed;
    
    // Comfortaa
    public static MinecraftFontRenderer comfortaa;
    private static Font comfortaa_;
    
    // Comfortaa Big
    public static MinecraftFontRenderer comfortaaBig;
    private static Font comfortaaBig_;

    public static Map<String, Font> locationMap = new HashMap<>();
    
    private static Font getFont(Map<String, Font> locationMap, String fontName, int size) {
        Font font = null;

        Logger logger = LogManager.getLogger();
        
        try {
            if (locationMap.containsKey(fontName)) {
                font = locationMap.get(fontName).deriveFont(Font.PLAIN, size);
            } else {
                InputStream inputStream = FontManager.class.getResourceAsStream("/assets/xeno/font/" + fontName + ".ttf");
                Font awtClientFont = Font.createFont(0, inputStream);
                locationMap.put(fontName, font);
                font = awtClientFont.deriveFont(Font.PLAIN, size);
                inputStream.close();
            }
        } catch (Exception e) {
            logger.error("Error loading font");
            font = new Font("default", Font.PLAIN, 18);
        }

        return font;
    }

    public static boolean hasLoaded() {
        return completed >= 3;
    }

    public static void bootstrap() {
        Xeno.logger.info("Initializing fonts...");
        new Thread(() ->
        {
            locationMap = new HashMap<>();
            comfortaa_ = getFont(locationMap, "comfortaa", 18);
            comfortaaBig_ = getFont(locationMap, "comfortaa", 40);
            completed++;
        }).start();
        
        new Thread(() ->
        {
            Map<String, Font> locationMap = new HashMap<>();
            completed++;
        }).start();
        new Thread(() ->
        {
            Map<String, Font> locationMap = new HashMap<>();
            completed++;
        }).start();

        while (!hasLoaded()) {
        }

        comfortaa = new MinecraftFontRenderer(comfortaa_, true, true);
        comfortaaBig = new MinecraftFontRenderer(comfortaaBig_, true, true);
    }
}