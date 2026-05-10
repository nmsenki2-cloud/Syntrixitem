package hu.syntrixsmp.syntrixitems;

import hu.syntrixsmp.syntrixitems.commands.SyntrixItemCommand;
import hu.syntrixsmp.syntrixitems.listeners.*;
import hu.syntrixsmp.syntrixitems.managers.BoosterManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SyntrixItemsPlugin extends JavaPlugin {

    private static SyntrixItemsPlugin instance;
    private Economy economy;
    private BoosterManager boosterManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        if (!setupEconomy()) {
            getLogger().severe("[SyntrixItems] Vault/Economy nem található! Plugin leáll.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        boosterManager = new BoosterManager(this);
        getServer().getPluginManager().registerEvents(new AmethystPickaxeListener(this), this);
        getServer().getPluginManager().registerEvents(new AmethystAxeListener(this), this);
        getServer().getPluginManager().registerEvents(new SellAxeListener(this), this);
        getServer().getPluginManager().registerEvents(new ShardBoosterListener(this), this);
        getCommand("syntrixitem").setExecutor(new SyntrixItemCommand(this));
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new SyntrixItemsPlaceholder(this).register();
        }
        getLogger().info("[SyntrixItems] Plugin sikeresen betöltve!");
    }

    @Override
    public void onDisable() {
        if (boosterManager != null) boosterManager.stop();
        getLogger().info("[SyntrixItems] Plugin leállítva.");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return economy != null;
    }

    public static SyntrixItemsPlugin getInstance() { return instance; }
    public Economy getEconomy() { return economy; }
    public BoosterManager getBoosterManager() { return boosterManager; }
}
