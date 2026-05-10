package hu.syntrixsmp.syntrixitems.utils;

import hu.syntrixsmp.syntrixitems.SyntrixItemsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    public static final String AMETHYST_PICKAXE_KEY = "amethyst_pickaxe";
    public static final String AMETHYST_AXE_KEY = "amethyst_axe";
    public static final String SELL_AXE_KEY = "sell_axe";
    public static final String SHARD_BOOSTER_KEY = "shard_booster";

    private static Component color(String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }

    public static ItemStack createAmethystPickaxe() {
        SyntrixItemsPlugin plugin = SyntrixItemsPlugin.getInstance();
        ItemStack item = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(color(plugin.getConfig().getString("items.amethyst-pickaxe.name", "&5&lAmethyst Csákány")));
        List<Component> lore = new ArrayList<>();
        for (String line : plugin.getConfig().getStringList("items.amethyst-pickaxe.lore")) lore.add(color(line));
        meta.lore(lore);
        meta.addEnchant(Enchantment.EFFICIENCY, 5, true);
        meta.addEnchant(Enchantment.UNBREAKING, 3, true);
        meta.addEnchant(Enchantment.FORTUNE, 3, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, AMETHYST_PICKAXE_KEY), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createAmethystAxe() {
        SyntrixItemsPlugin plugin = SyntrixItemsPlugin.getInstance();
        ItemStack item = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(color(plugin.getConfig().getString("items.amethyst-axe.name", "&5&lAmethyst Balta")));
        List<Component> lore = new ArrayList<>();
        for (String line : plugin.getConfig().getStringList("items.amethyst-axe.lore")) lore.add(color(line));
        meta.lore(lore);
        meta.addEnchant(Enchantment.EFFICIENCY, 5, true);
        meta.addEnchant(Enchantment.UNBREAKING, 3, true);
        meta.addEnchant(Enchantment.FORTUNE, 3, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, AMETHYST_AXE_KEY), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createSellAxe() {
        SyntrixItemsPlugin plugin = SyntrixItemsPlugin.getInstance();
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(color(plugin.getConfig().getString("items.sell-axe.name", "&6&lEladó Balta")));
        List<Component> lore = new ArrayList<>();
        for (String line : plugin.getConfig().getStringList("items.sell-axe.lore")) lore.add(color(line));
        meta.lore(lore);
        meta.addEnchant(Enchantment.UNBREAKING, 3, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, SELL_AXE_KEY), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createShardBooster() {
        SyntrixItemsPlugin plugin = SyntrixItemsPlugin.getInstance();
        ItemStack item = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(color(plugin.getConfig().getString("items.shard-booster.name", "&b&lShard Booster")));
        List<Component> lore = new ArrayList<>();
        for (String line : plugin.getConfig().getStringList("items.shard-booster.lore")) lore.add(color(line));
        meta.lore(lore);
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, SHARD_BOOSTER_KEY), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isCustomItem(ItemStack item, String itemKey) {
        if (item == null || !item.hasItemMeta()) return false;
        NamespacedKey key = new NamespacedKey(SyntrixItemsPlugin.getInstance(), itemKey);
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }
}
