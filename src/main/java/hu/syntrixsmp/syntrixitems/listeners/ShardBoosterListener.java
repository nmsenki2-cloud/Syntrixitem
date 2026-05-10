package hu.syntrixsmp.syntrixitems.listeners;

import hu.syntrixsmp.syntrixitems.SyntrixItemsPlugin;
import hu.syntrixsmp.syntrixitems.managers.BoosterManager;
import hu.syntrixsmp.syntrixitems.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ShardBoosterListener implements Listener {
    private final SyntrixItemsPlugin plugin;

    public ShardBoosterListener(SyntrixItemsPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemUtils.isCustomItem(item, ItemUtils.SHARD_BOOSTER_KEY)) return;
        event.setCancelled(true);
        BoosterManager manager = plugin.getBoosterManager();
        if (manager.hasActiveBooster(player)) {
            player.sendMessage(c("&c&l[Booster] &cMár van aktív boostere! Hátralévő: &e" + manager.getFormattedRemaining(player)));
            return;
        }
        manager.activateBooster(player);
        if (item.getAmount() > 1) item.setAmount(item.getAmount() - 1);
        else player.getInventory().setItemInMainHand(null);
        int hours = plugin.getConfig().getInt("shard-booster.duration-seconds", 86400) / 3600;
        int pps = plugin.getConfig().getInt("shard-booster.points-per-second", 4);
        player.sendMessage(c("&b&l[Booster] &bShard Booster aktiválva! &7" + pps + " Shard/mp " + hours + " óráig!"));
        player.sendTitle(c("&b&lShard Booster"), c("&7" + pps + " Shard/mp - " + hours + " óra"), 10, 60, 20);
    }

    private Component c(String text) { return LegacyComponentSerializer.legacyAmpersand().deserialize(text); }
}
