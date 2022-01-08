package me.xenodevs.xeno.command.impl;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.command.Command;
import me.xenodevs.xeno.managers.CommandManager;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.utils.other.ChatUtil;

public class GuiCommand extends Command {

	public GuiCommand() {
		super("Gui", "clickgui stuff", "gui reset", "gui");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length > 0 && args[0].equalsIgnoreCase("reset")) {
			Xeno.clickGui.reset();
			ChatUtil.addChatMessage("reset clickgui");
		} else if(args.length == 2 && args[0].equalsIgnoreCase("theme")) {

		} else {
			CommandManager.correctUsageMsg(getName(), getSyntax());
		}
	}
}
