package io.github.tigercrl.mute.event;

import io.github.tigercrl.mute.Mute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatEventHandler implements Listener {
    Mute plugin;

    public AsyncPlayerChatEventHandler(Mute plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (plugin.mutedPlayersConfig.isPlayerMuted(p)) {
            e.setCancelled(true);
            plugin.mutedPlayersConfig.sendMutedMessage(p);
        }
    }
}
