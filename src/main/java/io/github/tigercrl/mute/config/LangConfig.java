package io.github.tigercrl.mute.config;

import io.github.tigercrl.mute.Mute;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LangConfig {
    private Mute plugin;
    private File configFile;
    private YamlConfiguration config;

    public LangConfig(Mute plugin) {
        this.plugin = plugin;
    }

    public void saveDefaultConfig() {
        if (!new File(plugin.getDataFolder(), "lang_en.yml").exists()) plugin.saveResource("lang_en.yml", false);
        if (!new File(plugin.getDataFolder(), "lang_zh.yml").exists()) plugin.saveResource("lang_zh.yml", false);
    }

    public void loadConfig() {
        configFile = new File(plugin.getDataFolder(), "lang_" + plugin.getConfig().getString("lang") + ".yml");
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLang(String path) {
        return config.getString(path);
    }
}

