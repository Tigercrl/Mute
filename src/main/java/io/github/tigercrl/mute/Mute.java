package io.github.tigercrl.mute;

import io.github.tigercrl.mute.command.MuteCommand;
import io.github.tigercrl.mute.command.UnmuteCommand;
import io.github.tigercrl.mute.config.LangConfig;
import io.github.tigercrl.mute.config.MutedPlayersConfig;
import io.github.tigercrl.mute.event.AsyncPlayerChatEventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mute extends JavaPlugin {
    public LangConfig lang = new LangConfig(this);
    public MutedPlayersConfig mutedPlayersConfig = new MutedPlayersConfig(this);

    @Override
    public void onEnable() {
        // Language Config
        saveDefaultConfig();
        lang.saveDefaultConfig();
        lang.loadConfig();
        // Muted Player List
        mutedPlayersConfig.saveDefaultConfig();
        mutedPlayersConfig.loadConfig();
        // Commands
        getCommand("mute").setExecutor(new MuteCommand(this));
        getCommand("unmute").setExecutor(new UnmuteCommand(this));
        // Chat Event
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatEventHandler(this), this);
        // Done
        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled.");
    }
}
