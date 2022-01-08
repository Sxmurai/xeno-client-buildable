package me.xenodevs.xeno;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import me.xenodevs.xeno.alt.AltManager;
import me.xenodevs.xeno.discord.DiscordManager;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.component.Frame;
import me.xenodevs.xeno.gui.windowgui.WindowGui;
import me.xenodevs.xeno.managers.*;
import me.xenodevs.xeno.utils.render.BlurUtil;
import net.minecraft.util.Session;
import org.apache.logging.log4j.Logger;
import me.wolfsurge.api.gui.font.FontUtil;
import me.xenodevs.xeno.storage.Config;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Xeno.MODID, name = Xeno.MODNAME, version = Xeno.VERSION)
public class Xeno {
    public static final String MODID = "xeno";
    public static final String MODNAME = "Xeno";
    public static final String VERSION = "1.2";
    
    public static String NAME = "Xeno Client";
    
	public static boolean isDev = false;
    public static Logger logger;
    public static me.zero.alpine.EventManager EVENT_BUS = new me.zero.alpine.EventManager();
    public static boolean hasFinishedLoading = false; // For Discord RPC

    public static BlurUtil blurManager;
    public static ModuleManager moduleManager;
    public static ClickGui clickGui;
    public static GuiManager guiManager;
    public static WindowGui windowGui;
    public static EventManager eventManager;
    public static HudManager hudManager;
    public static Config config;
    public static FriendManager friendManager;
    public static CommandManager commandManager;
    public static TickManager tickManager;
    public static NotificationManager notificationManager;
    public static DiscordManager discordManager;
    public static AltManager altManager;
    public static RotationManager rotationManager;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        discordManager = new DiscordManager();
        discordManager.startPresence();
    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        FontUtil.bootstrap();
        config = new Config();
        config.loadHUDConfig();
        config.loadClickGUIConfig();
        blurManager = new BlurUtil();
        moduleManager = new ModuleManager();
        config.loadModConfig();
        clickGui = new ClickGui();
        guiManager = new GuiManager();
        windowGui = new WindowGui();
        for(Frame f : clickGui.frames)
            f.refresh(); // Stop a weird bug

        windowGui = new WindowGui();
        eventManager = new EventManager();
        hudManager = new HudManager();
        friendManager = new FriendManager();
        config.loadFriendConfig();
        commandManager = new CommandManager();
        config.loadMisc();
        notificationManager = new NotificationManager();
        tickManager = new TickManager();
        FontManager.load();
        altManager = new AltManager();
        rotationManager = new RotationManager();
        hasFinishedLoading = true;

        logger.info("\nXeno Initialized!\n");
    }
}
