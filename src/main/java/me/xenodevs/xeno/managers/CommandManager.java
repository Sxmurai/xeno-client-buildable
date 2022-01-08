package me.xenodevs.xeno.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.xenodevs.xeno.command.Command;
import org.lwjgl.input.Keyboard;
import com.mojang.realmsclient.gui.ChatFormatting;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.command.impl.*;
import me.xenodevs.xeno.friends.Friend;
import me.xenodevs.xeno.utils.other.ChatUtil;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class CommandManager {
	public static List<Command> commands = new ArrayList<Command>();
	public static String prefix = "+";
	public boolean commandFound = false;
	
	public CommandManager() {
		register();
		Xeno.logger.info("Initialized Command Manager");
	}
	
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
		Xeno.EVENT_BUS.subscribe(this);

		commands.add(new FriendCommand());
		commands.add(new GuiCommand());
		commands.add(new ElytraFlyCommand());
		commands.add(new HelpCommand());
		commands.add(new BindCommand());
	}
	
	@EventHandler
    public Listener<ClientChatEvent> listener = new Listener<>(event -> {
        String message = event.getMessage();
        
        if(!message.startsWith(prefix))
        	return;
        
        event.setCanceled(true);
        message = message.substring(prefix.length());
        if(message.split(" ").length > 0) {
        	boolean commandFound = false;
        	String commandName = message.split(" ")[0];
        	for(Command c : commands) {
        		if(c.aliases.contains(commandName) || c.name.equalsIgnoreCase(commandName)) {
	        		c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
	        		commandFound = true;
	        		break;
        		}
        	}
        	if(!commandFound) {
        		ChatUtil.addChatMessage(ChatFormatting.DARK_RED + "Command not found! Use " + ChatFormatting.RESET + ChatFormatting.ITALIC + prefix + "help " + ChatFormatting.DARK_RED + "" + "for help.");
        	}
        }
    });
	
	@SubscribeEvent
	public void key(KeyInputEvent e) {
		if (prefix.length() == 1) {
            final char key = Keyboard.getEventCharacter();
            if (prefix.charAt(0) == key) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
                ((GuiChat) Minecraft.getMinecraft().currentScreen).inputField.setText(prefix);
            }
        }
	}
	
	public static void setCommandPrefix(String pre) {
        prefix = pre;
    }
	
	public static String getCommandPrefix(String name) {
        return prefix;
    }
	
	public static void correctUsageMsg(String name, String syntax) {
		String usage = "Correct usage of " + name + " command > " + prefix + syntax;
		
		ChatUtil.addChatMessage(usage);
	}
}
