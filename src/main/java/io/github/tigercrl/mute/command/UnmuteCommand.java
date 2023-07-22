package io.github.tigercrl.mute.command;

import io.github.tigercrl.mute.Mute;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnmuteCommand implements TabExecutor {
    Mute plugin;

    public UnmuteCommand(Mute plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("unmute") && args.length == 1) {
            Player p = Bukkit.getPlayerExact(args[0]); // Get Player
            if (p == null) { // Cannot find player
                sender.sendMessage(ChatColor.RED + "[Mute] " + plugin.lang.getLang("player-not-found").replace("%player%", args[0]));
                return true;
            }
            plugin.mutedPlayersConfig.unmutePlayer(p);
            sender.sendMessage("[Mute] " + plugin.lang.getLang("unmute-success").replace("%player%", p.getName()));
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) { // Player
            List<String> playerList = new ArrayList();
            for (Player p : Bukkit.getOnlinePlayers()) {
                playerList.add(p.getName());
            }
            Collections.sort(playerList);
            return playerList;
        }
        return new ArrayList();
    }
}