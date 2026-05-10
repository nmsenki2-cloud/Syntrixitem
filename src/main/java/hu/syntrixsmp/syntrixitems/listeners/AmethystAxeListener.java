package hu.syntrixsmp.syntrixitems.listeners;

import hu.syntrixsmp.syntrixitems.SyntrixItemsPlugin;
import hu.syntrixsmp.syntrixitems.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class AmethystAxeListener implements Listener {
    private final SyntrixItemsPlugin plugin;
    private final Set<org.bukkit.Location> breaking = new HashSet<>();
    private static final Set<Material> LOGS = new HashSet<>(Arrays.asList(
        Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG, Material.JUNGLE_LOG,
        Material.ACACIA_LOG, Material.DARK_OAK_LOG, Material.MANGROVE_LOG, Material.CHERRY_LOG,
        Material.OAK_WOOD, Material.SPRUCE_WOOD, Material.BIRCH_WOOD, Material.JUNGLE_WOOD,
        Material.ACACIA_WOOD, Material.DARK_OAK_WOOD, Material.STRIPPED_OAK_LOG,
        Material.STRIPPED_SPRUCE_LOG, Material.STRIPPED_BIRCH_LOG, Material.STRIPPED_JUNGLE_LOG,
        Material.STRIPPED_ACACIA_LOG, Material.STRIPPED_DARK_OAK_LOG,
        Material.STRIPPED_MANGROVE_LOG, Material.STRIPPED_CHERRY_LOG
    ));

    public AmethystAxeListener(SyntrixItemsPlugin plugin) { this.plugin = plugin; }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemUtils.isCustomItem(item, ItemUtils.AMETHYST_AXE_KEY)) return;
        if (!LOGS.contains(event.getBlock().getType())) return;
        if (breaking.contains(event.getBlock().getLocation())) return;
        for (Block b : findConnectedLogs(event.getBlock())) {
            if (b.equals(event.getBlock())) continue;
            breaking.add(b.getLocation());
            b.breakNaturally(item);
            breaking.remove(b.getLocation());
        }
    }

    private List<Block> findConnectedLogs(Block start) {
        List<Block> result = new ArrayList<>();
        Queue<Block> queue = new LinkedList<>();
        Set<org.bukkit.Location> visited = new HashSet<>();
        queue.add(start);
        visited.add(start.getLocation());
        while (!queue.isEmpty() && result.size() < 500) {
            Block current = queue.poll();
            result.add(current);
            for (int dx = -1; dx <= 1; dx++)
                for (int dy = 0; dy <= 1; dy++)
                    for (int dz = -1; dz <= 1; dz++) {
                        Block neighbor = current.getRelative(dx, dy, dz);
                        if (!visited.contains(neighbor.getLocation()) && LOGS.contains(neighbor.getType())) {
                            visited.add(neighbor.getLocation());
                            queue.add(neighbor);
                        }
                    }
        }
        return result;
    }
}
