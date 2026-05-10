package hu.syntrixsmp.syntrixitems.commands;

import hu.syntrixsmp.syntrixitems.SyntrixItemsPlugin;
import hu.syntrixsmp.syntrixitems.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.Arrays;
import java.util.List;

public class SyntrixItemCommand implements CommandExecutor, TabCompleter {
    private final SyntrixItemsPlugin plugin;

    public SyntrixItemCommand(SyntrixItemsPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("syntrixitems.admin")) { send(sender, "&cNincs jogosultságod!"); return true; }
        if (args.length < 3 || !args[0].equalsIgnoreCase("give")) {
            send(sender, "&e/si give <játékos> <item> [mennyiség]");
            send(sender, "&7Itemek: amethyst-pickaxe, amethyst-axe, sell-axe, shard-booster");
            return true;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) { send(sender, "&cNem található: &f" + args[1]); return true; }
        int amount = 1;
        if (args.length >= 4) try { amount = Integer.parseInt(args[3]); } catch (NumberFormatException ignored) {}
        ItemStack item = switch (args[2].toLowerCase()) {
            case "amethyst-pickaxe" -> ItemUtils.createAmethystPickaxe();
            case "amethyst-axe" -> ItemUtils.createAmethystAxe();
            case "sell-axe" -> ItemUtils.createSellAxe();
            case "shard-booster" -> ItemUtils.createShardBooster();
            default -> null;
        };
        if (item == null) { send(sender, "&cIsmeretlen item: &f" + args[2]); return true; }
        item.setAmount(amount);
        target.getInventory().addItem(item);
        send(sender, "&aAdtál &f" + amount + "x " + args[2] + " &aitemet: &f" + target.getName());
        send(target, "&b&l[SyntrixSMP] &eKaptál egy &f" + args[2] + " &eitemet!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return List.of("give");
        if (args.length == 2) return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        if (args.length == 3) return Arrays.asList("amethyst-pickaxe", "amethyst-axe", "sell-axe", "shard-booster");
        return List.of();
    }

    private void send(CommandSender s, String msg) { s.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msg)); }
}
