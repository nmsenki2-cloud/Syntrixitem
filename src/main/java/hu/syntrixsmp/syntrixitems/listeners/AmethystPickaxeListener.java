package hu.syntrixsmp.syntrixitems.listeners;

import hu.syntrixsmp.syntrixitems.SyntrixItemsPlugin;
import hu.syntrixsmp.syntrixitems.utils.ItemUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AmethystPickaxeListener implements Listener {
    private final SyntrixItemsPlugin plugin;
    private final Set<org.bukkit.Location> breaking = new HashSet<>();

    public AmethystPickaxeListener(SyntrixItemsPlugin plugin) { this.plugin = plugin; }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemUtils.isCustomItem(item, ItemUtils.AMETHYST_PICKAXE_KEY)) return;
        if (breaking.contains(event.getBlock().getLocation())) return;
        Block center = event.getBlock();
        List<Block> toBreak = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++)
            for (int dy = -1; dy <= 1; dy++)
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    Block b = center.getRelative(dx, dy, dz);
                    if (!b.getType().isAir() && b.getType().isSolid()) toBreak.add(b);
                }
        for (Block b : toBreak) {
            breaking.add(b.getLocation());
            BlockBreakEvent fakeEvent = new BlockBreakEvent(b, player);
            plugin.getServer().getPluginManager().callEvent(fakeEvent);
            if (!fakeEvent.isCancelled()) b.breakNaturally(item);
            breaking.remove(b.getLocation());
        }
    }
}
