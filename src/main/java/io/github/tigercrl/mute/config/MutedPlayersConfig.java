package io.github.tigercrl.mute.config;

import io.github.tigercrl.mute.Mute;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MutedPlayersConfig {
    private final Mute plugin;
    private File configFile;
    private final YamlConfiguration config = new YamlConfiguration();
    private Timer timer = new Timer(true);

    public MutedPlayersConfig(Mute plugin) {
        this.plugin = plugin;
    }

    public void saveDefaultConfig() {
        if (!new File(plugin.getDataFolder(), "muted_players.yml").exists())
            plugin.saveResource("muted_players.yml", false);
    }

    private void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        configFile = new File(plugin.getDataFolder(), "muted_players.yml");
        try {
            config.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String key : config.getKeys(false)) {
            String unmuteTime = config.getString(key + ".time");
            if (unmuteTime != null) { // Not infinite
                Date now = new Date();
                Date unmuteDate = new Date(Long.parseLong(unmuteTime));
                if (unmuteDate.after(now)) { // Time passed
                    config.set(key, null);
                } else { // Not passed
                    TimerTask unmute = new TimerTask() {
                        @Override
                        public void run() {
                            unmutePlayer(key);
                        }
                    };
                    timer.schedule(unmute, unmuteDate);
                }
            }
        }
    }

    public boolean isPlayerMuted(Player p) {
        return !p.hasPermission("mute.bypass") && config.getKeys(false).contains(p.getUniqueId().toString());
    }

    public void mutePlayer(Player p, String time, String reason) {
        String uuid = p.getUniqueId().toString();
        config.set(uuid + ".name", p.getName());
        if (!time.equals("infinite")) {
            time = Long.toString(new Date().getTime() + Long.parseLong(time) * 60000);
            config.set(uuid + ".time", time);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    unmutePlayer(p);
                }
            }, new Date(Long.parseLong(time)));
        } else {
            config.set(uuid + ".time", null);
        }
        config.set(uuid + ".reason", reason);
        sendMutedMessage(p);
        saveConfig();
    }

    public void sendMutedMessage(Player p) {
        String time = config.getString(p.getUniqueId() + ".time");
        if (time == null) time = "infinite";
        else time = String.valueOf((int) Math.ceil((Long.parseLong(time) - new Date().getTime()) / 60000.0));
        p.sendMessage(ChatColor.RED + "[Mute] " + plugin.lang.getLang("muted").replace("%time%", time).replace("%reason%", config.getString(p.getUniqueId() + ".reason")));
    }

    public void unmutePlayer(Player p) {
        if (config.getKeys(false).contains(p.getUniqueId().toString())) {
            config.set(p.getUniqueId().toString(), null);
            p.sendMessage(ChatColor.RED + "[Mute] " + plugin.lang.getLang("unmuted"));
            saveConfig();
        }
    }

    public void unmutePlayer(String uuid) {
        if (config.getKeys(false).contains(uuid)) {
            config.set(uuid, null);
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) // Send unmuted message
                p.sendMessage(ChatColor.RED + "[Mute] " + plugin.lang.getLang("unmuted"));
            saveConfig();
        }
    }
}