package me.xenodevs.xeno.module.modules.misc;

import me.xenodevs.xeno.module.settings.StringSetting;
import org.lwjgl.input.Keyboard;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.client.event.ClientChatEvent;

public class Suffix extends Module {
	
	StringSetting suffixMessage = new StringSetting("MSG", "xeno on top");
	
	public Suffix() {
		super("Suffix", "adds text after every message", Keyboard.KEY_NONE, Category.MISC);
		this.addSettings(suffixMessage);
	}
	
	@EventHandler
	private final Listener<ClientChatEvent> chatListener = new Listener<>(event -> {
		if(!event.getMessage().startsWith(Xeno.commandManager.prefix) && !event.getMessage().startsWith("/")) {
			event.setCanceled(true);
		
			mc.getConnection().sendPacket(new CPacketChatMessage(event.getMessage() + " | " + suffixMessage.getText()));
		}
	});
	
}
