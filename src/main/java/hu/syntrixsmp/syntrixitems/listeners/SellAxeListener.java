package hu.syntrixsmp.syntrixitems.listeners;

import hu.syntrixsmp.syntrixitems.SyntrixItemsPlugin;
import hu.syntrixsmp.syntrixitems.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;

public class SellAxeListener implements Listener {
    private final SyntrixItemsPlugin plugin;

    public SellAxeListener(SyntrixItemsPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemUtils.isCustomItem(item, ItemUtils.SELL_AXE_KEY)) return;
        Block clicked = event.getClickedBlock();
        if (clicked == null) return;
        if (clicked.getType() != Material.CHEST && clicked.getType() != Material.TRAPPED_CHEST && clicked.getType() != Material.BARREL) return;
        event.setCancelled(true);
        Inventory inv = null;
        if (clicked.getState() instanceof org.bukkit.block.Chest chest) inv = chest.getInventory();
        else if (clicked.getState() instanceof org.bukkit.block.Barrel barrel) inv = barrel.getInventory();
        if (inv == null) return;
        Economy eco = plugin.getEconomy();
        double totalEarned = 0.0;
        Map<Material, Integer> soldItems = new HashMap<>();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack slot = inv.getItem(i);
            if (slot == null || slot.getType() == Material.AIR) continue;
            double price = plugin.getConfig().getDouble("sell-prices." + slot.getType().name(), -1);
            if (price < 0) continue;
            totalEarned += price * slot.getAmount();
            soldItems.merge(slot.getType(), slot.getAmount(), Integer::sum);
            inv.setItem(i, null);
        }
        if (totalEarned > 0) {
            eco.depositPlayer(player, totalEarned);
            player.sendMessage(c("&a&l[Eladás] &aSikeresen eladva!"));
            for (Map.Entry<Material, Integer> e : soldItems.entrySet()) {
                double up = plugin.getConfig().getDouble("sell-prices." + e.getKey().name(), 0);
                player.sendMessage(c("&7 - &f" + e.getKey().name() + " &7x" + e.getValue() + " &8= &6$" + String.format("%.0f", up * e.getValue())));
            }
            player.sendMessage(c("&aÖsszesen: &6$" + String.format("%.0f", totalEarned)));
        } else {
            player.sendMessage(c("&c&l[Eladás] &cNincs eladható tárgy a ládában!"));
        }
    }

    private Component c(String text) { return LegacyComponentSerializer.legacyAmpersand().deserialize(text); }
}
