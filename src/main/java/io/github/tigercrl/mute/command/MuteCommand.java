package io.github.tigercrl.mute.command;

import io.github.tigercrl.mute.Mute;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MuteCommand implements TabExecutor {
    Mute plugin;

    public MuteCommand(Mute plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("mute") && args.length >= 1) {
            // Get Player
            Player p = Bukkit.getPlayerExact(args[0]);
            if (p == null) { // Cannot find player
                sender.sendMessage(ChatColor.RED + "[Mute] " + plugin.lang.getLang("player-not-found").replace("%player%", args[0]));
                return true;
            }
            switch (args.length) { // arguments
                case 1:
                    plugin.mutedPlayersConfig.mutePlayer(p, "infinite", "None");
                    break;
                case 2:
                    if (!checkTime(sender, args[1])) return true;
                    plugin.mutedPlayersConfig.mutePlayer(p, args[1], "None");
                    break;
                default: // More than 3 arguments
                    if (!checkTime(sender, args[1])) return true;
                    plugin.mutedPlayersConfig.mutePlayer(p, args[1], String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
                    break;
            }
            sender.sendMessage("[Mute] " + plugin.lang.getLang("mute-success").replace("%player%", p.getName()));
            return true;
        }
        return false;
    }

    private boolean checkTime(CommandSender sender, String timeStr) {
        if (!timeStr.equals("infinite")) {
            try {
                long time = Long.parseLong(timeStr);
                if (time <= 0) return false;
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "[Mute] " + plugin.lang.getLang("argument-error").replace("%argument%", timeStr));
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1: // Player
                List<String> playerList = new ArrayList();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    playerList.add(p.getName());
                }
                Collections.sort(playerList);
                return playerList;
            case 2: // Time
                return Collections.singletonList("infinite");
        }
        return new ArrayList();
    }
}