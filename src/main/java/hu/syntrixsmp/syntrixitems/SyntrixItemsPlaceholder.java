package hu.syntrixsmp.syntrixitems;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SyntrixItemsPlaceholder extends PlaceholderExpansion {

    private final SyntrixItemsPlugin plugin;

    public SyntrixItemsPlaceholder(SyntrixItemsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override public @NotNull String getIdentifier() { return "syntrix"; }
    @Override public @NotNull String getAuthor() { return "SyntrixSMP"; }
    @Override public @NotNull String getVersion() { return plugin.getDescription().getVersion(); }
    @Override public boolean persist() { return true; }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return "";
        if (identifier.equals("booster_active")) {
            return plugin.getBoosterManager().hasActiveBooster(player) ? "Aktív" : "Inaktív";
        }
        if (identifier.equals("booster_remaining")) {
            return plugin.getBoosterManager().getFormattedRemaining(player);
        }
        return null;
    }
}
