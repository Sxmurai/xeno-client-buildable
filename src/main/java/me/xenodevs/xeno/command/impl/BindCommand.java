package me.xenodevs.xeno.command.impl;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.command.Command;
import me.xenodevs.xeno.managers.CommandManager;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.utils.other.ChatUtil;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("Bind", "bind module keybinds to keys", "bind <set|clear> <module> <key>", "bind");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args != null) {
            if(args.length > 0) {
                try {
                    if(args[0].equalsIgnoreCase("set") && args.length == 3) {
                        for(Module m : Xeno.moduleManager.modules) {
                            if(args[1].equalsIgnoreCase(m.name)) {
                                m.keyCode.code = Keyboard.getKeyIndex(args[2].toUpperCase());
                                ChatUtil.addChatMessage("set " + m.name + "'s bind to " + args[2].toUpperCase());
                                Xeno.config.saveModConfig(m);
                                break;
                            }
                        }
                    } else if(args[0].equalsIgnoreCase("clear") && args.length == 2) {
                        for(Module m : Xeno.moduleManager.modules) {
                            if(args[1].equalsIgnoreCase(m.name)) {
                                m.keyCode.code = 0;
                                ChatUtil.addChatMessage("cleared " + m.name + "'s bind");
                                Xeno.config.saveModConfig(m);
                                break;
                            }
                        }
                    } else {
                        CommandManager.correctUsageMsg(getName(), getSyntax());
                    }
                } catch (Exception e) {
                    CommandManager.correctUsageMsg(getName(), getSyntax());
                }
            } else {
                CommandManager.correctUsageMsg(getName(), getSyntax());
            }
        }
    }
}
