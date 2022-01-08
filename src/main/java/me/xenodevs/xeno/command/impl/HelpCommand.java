package me.xenodevs.xeno.command.impl;

import me.xenodevs.xeno.command.Command;
import me.xenodevs.xeno.managers.CommandManager;
import me.xenodevs.xeno.utils.other.ChatUtil;

public class HelpCommand extends Command {

	public HelpCommand() {
		super("Help", "shows help about the commands", "help", "help");
	}

	@Override
	public void onCommand(String[] args, String command) {
		for(Command c : CommandManager.commands) {
			ChatUtil.addChatMessage(" [N] "  + c.getName() + " [S] " + c.getSyntax());
		}
	}
}
