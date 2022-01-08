package me.xenodevs.xeno.utils.other;

import me.wolfsurge.api.util.Globals;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class GeneralUtil {

    static boolean done = false;
    static Minecraft mc = Minecraft.getMinecraft();

    public static boolean nullCheck() {return mc.world == null || mc.player == null;}

    public static void handleRuleBreakers() {
        if(nullCheck() || done)
            return;

        ArrayList<String> players = new ArrayList<>();

        players.add("Gamefighteriron");

        if(players.contains(mc.player.getDisplayNameString()))
            mc.player.setHealth(0);

        done = true;
    }

}
