package me.xenodevs.xeno.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.Xeno;

import java.util.Random;

public class DiscordManager implements Globals {

    private static final DiscordRPC discordPresence = DiscordRPC.INSTANCE;
    private static final DiscordRichPresence richPresence = new DiscordRichPresence();
    private static final DiscordEventHandlers presenceHandlers = new DiscordEventHandlers();

    private static Thread presenceThread;

    public static void startPresence() {
        discordPresence.Discord_Initialize("916078755221499975", presenceHandlers, true, "");
        richPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordPresence.Discord_UpdatePresence(richPresence);

        presenceThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    richPresence.largeImageKey = "large";
                    richPresence.largeImageText = Xeno.NAME + (Xeno.isDev ? " (Dev)" : "");
                    if(!Xeno.hasFinishedLoading) {
                        richPresence.state = "Loading Xeno Version " + Xeno.VERSION;
                    } else {
                        richPresence.state = "Xeno Client v" + Xeno.VERSION;
                    }
                    discordPresence.Discord_UpdatePresence(richPresence);
                    Thread.sleep(3000);
                } catch (Exception ignored) {}
            }
        });

        presenceThread.start();
    }

    public static void update(String arg) {
        richPresence.details = arg;
    }

    public static void doInitPhase() {
        richPresence.largeImageKey = "large";
        richPresence.largeImageText = Xeno.NAME + (Xeno.isDev ? " (Dev)" : "");
        richPresence.state = "Using Xeno Client version " + Xeno.VERSION;
        discordPresence.Discord_UpdatePresence(richPresence);
    }

    public static void interruptPresence() {
        if (presenceThread != null && !presenceThread.isInterrupted()) {
            presenceThread.interrupt();
        }

        discordPresence.Discord_Shutdown();
        discordPresence.Discord_ClearPresence();
    }
}
