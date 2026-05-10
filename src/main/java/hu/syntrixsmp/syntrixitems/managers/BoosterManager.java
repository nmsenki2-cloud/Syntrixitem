package hu.syntrixsmp.syntrixitems.managers;

import hu.syntrixsmp.syntrixitems.SyntrixItemsPlugin;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoosterManager {
    private final SyntrixItemsPlugin plugin;
    private final Map<UUID, Long> boosterExpiry = new HashMap<>();
    private BukkitTask boosterTask;
    private File dataFile;
    private FileConfiguration dataConfig;

    public BoosterManager(SyntrixItemsPlugin plugin) {
        this.plugin = plugin;
        loadData();
        startBoosterTask();
    }

    private void startBoosterTask() {
        int pointsPerMinute = plugin.getConfig().getInt("shard-booster.points-per-minute", 4);
        boosterTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            long now = System.currentTimeMillis();
            boosterExpiry.entrySet().removeIf(entry -> {
                if (entry.getValue() <= now) {
                    Player p = Bukkit.getPlayer(entry.getKey());
                    if (p != null) p.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&b&l[Booster] &eA Shard Boostere &clejárt&e!"));
                    return true;
                }
                Player p = Bukkit.getPlayer(entry.getKey());
                if (p != null && p.isOnline()) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "points give " + p.getName() + " " + pointsPerMinute);
                return false;
            });
        }, 1200L, 1200L); // 1200 tick = 1 perc
    }

    public boolean hasActiveBooster(Player player) {
        Long expiry = boosterExpiry.get(player.getUniqueId());
        if (expiry == null) return false;
        if (expiry <= System.currentTimeMillis()) { boosterExpiry.remove(player.getUniqueId()); return false; }
        return true;
    }

    public void activateBooster(Player player) {
        int durationSeconds = plugin.getConfig().getInt("shard-booster.duration-seconds", 86400);
        boosterExpiry.put(player.getUniqueId(), System.currentTimeMillis() + (durationSeconds * 1000L));
        saveData();
    }

    public long getRemainingSeconds(Player player) {
        Long expiry = boosterExpiry.get(player.getUniqueId());
        if (expiry == null) return 0;
        return Math.max(0, (expiry - System.currentTimeMillis()) / 1000);
    }

    public String getFormattedRemaining(Player player) {
        long secs = getRemainingSeconds(player);
        if (secs <= 0) return "Lejárt";
        return String.format("%d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);
    }

    public void saveData() {
        dataConfig.set("boosters", null);
        for (Map.Entry<UUID, Long> entry : boosterExpiry.entrySet())
            dataConfig.set("boosters." + entry.getKey().toString(), entry.getValue());
        try { dataConfig.save(dataFile); } catch (IOException e) { plugin.getLogger().warning("Mentési hiba!"); }
    }

    private void loadData() {
        dataFile = new File(plugin.getDataFolder(), "boosters.yml");
        if (!dataFile.exists()) try { dataFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        if (dataConfig.isConfigurationSection("boosters")) {
            long now = System.currentTimeMillis();
            for (String uuidStr : dataConfig.getConfigurationSection("boosters").getKeys(false)) {
                long expiry = dataConfig.getLong("boosters." + uuidStr);
                if (expiry > now) boosterExpiry.put(UUID.fromString(uuidStr), expiry);
            }
        }
    }

    public void stop() { if (boosterTask != null) boosterTask.cancel(); saveData(); }
}
