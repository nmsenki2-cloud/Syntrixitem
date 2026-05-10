package hu.syntrixsmp.syntrixitems.listeners;

import hu.syntrixsmp.syntrixitems.SyntrixItemsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardListener implements Listener {
    private final SyntrixItemsPlugin plugin;

    public ScoreboardListener(SyntrixItemsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getScoreboardManager().updateScoreboard(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getScoreboardManager().removeScoreboard(event.getPlayer());
    }
}
