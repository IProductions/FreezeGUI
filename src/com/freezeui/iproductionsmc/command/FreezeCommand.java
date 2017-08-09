package com.freezeui.iproductionsmc.command;

import com.freezeui.iproductionsmc.FreezeUI;
import com.freezeui.iproductionsmc.handler.FreezeHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

        if (cmd.getName().equalsIgnoreCase("freeze")) {
            if (sender.hasPermission("freezeui.freeze")) {
                if (args.length == 0) {

                    sender.sendMessage(FreezeHandler.color("&cUsage: /freeze [player]"));

                    return true;
                }
                if (args.length == 1) {

                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {

                        FreezeUI.getInstance().getFreezeHandler().freezePlayer(target);

                        if (FreezeUI.isFrozen(target)) {

                            for (String s : FreezeUI.getInstance().getConfig().getStringList("freeze.message-on-freeze")) {
                                target.sendMessage(FreezeHandler.color(s).replaceAll("%player%", target.getName()));
                            }
                            sender.sendMessage(ChatColor.RED + "You have frozen " + target.getName());
                            return true;

                        } else {

                            target.sendMessage(ChatColor.GREEN + "You are no longer frozen.");

                            sender.sendMessage(ChatColor.RED + "That player is no longer frozen.");

                            return true;

                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Player is offline.");
                    }
                } else {

                    sender.sendMessage(FreezeHandler.color("&cUsage: /freeze [player]"));

                    return true;
                }

            } else {

                sender.sendMessage(ChatColor.RED + "No permission.");

                return true;
            }
        }
        return true;
    }
}
