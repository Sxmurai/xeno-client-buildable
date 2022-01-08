package me.xenodevs.xeno.command.impl;

import java.util.ArrayList;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.command.Command;
import me.xenodevs.xeno.managers.CommandManager;
import me.xenodevs.xeno.friends.Friend;
import me.xenodevs.xeno.utils.other.ChatUtil;
import net.minecraft.util.text.TextFormatting;

public class FriendCommand extends Command {

	public FriendCommand() {
		super("Friend", "friend players", "friend list | <add | remove> <playername>", "friend");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("add")) {
				String plr = args[1];
				
				if(Xeno.friendManager.isFriend(plr)) {
					ChatUtil.addChatMessage(plr + " is already in your friends list!");	
					return;
				}
				
				ChatUtil.addChatMessage("Added player " + plr + " to friends list.");	
				Xeno.friendManager.addFriend(plr);
				
			} else if(args[0].equalsIgnoreCase("remove") && !args[1].equalsIgnoreCase("all")) {
				String com = args[0];
				String plr = args[1];
				
				boolean finAndNoFr = false;
				for(Friend s : Xeno.friendManager.friends) {
					if(Xeno.friendManager.isFriend(plr)) {
						ChatUtil.addChatMessage("Removed player " + plr + " from friends list.");	
						Xeno.friendManager.friends.remove(s);
						finAndNoFr = true;
						
						return;
					}
				}
				
				if(!finAndNoFr) {
					ChatUtil.addChatMessage(plr + " isn't in your friends list.");	
				}
			} else if(args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("all")) {
				String com = args[0];
				
				ArrayList<String> list = new ArrayList<String>();
				
				ChatUtil.addChatMessage("Removed all players from friends list.");	
				Xeno.friendManager.friends.removeAll(Xeno.friendManager.friends);
			} else {
				CommandManager.correctUsageMsg(getName(), getSyntax());
			} 
		} else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("list")) {
				
				ChatUtil.addChatMessage("Friends List -");
				
				for(Friend friends : Xeno.friendManager.friends) {
					String friend = friends.getName();
					String s1 = friend.substring(0, 1).toUpperCase();
				    String nameCapitalized = s1 + friend.substring(1);
					
				    ChatUtil.addChatMessage(TextFormatting.DARK_AQUA + nameCapitalized);
				}
			} else {
				CommandManager.correctUsageMsg(getName(), getSyntax());
			} 
		} else {
			CommandManager.correctUsageMsg(getName(), getSyntax());
		} 
		
		Xeno.config.saveFriendConfig();
	}
}
