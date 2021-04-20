package net.kunmc.lab.playercompassplugin;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerCompassPlugin extends JavaPlugin implements Listener {
    private static PlayerCompassPlugin INSTANCE;
    private static NamespacedKey namespacedKey;
    private static PlayerCompassManager manager;
    private static PlayerCompassPluginSaveData saveData;
    public static PlayerCompassManager getManager() {
        return manager;
    }

    public static PlayerCompassPlugin getInstance() {
        return INSTANCE;
    }

    public static NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public static PlayerCompassPluginSaveData getSaveData() {
        return saveData;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        namespacedKey = new NamespacedKey(this, NamespacedKey.BUKKIT);
        manager = new PlayerCompassManager();
        saveDefaultConfig();
        saveData = new PlayerCompassPluginSaveData(getConfig());

        getServer().getPluginCommand("playercompass").setExecutor(new CompassCommand());
        getServer().getPluginCommand("playerposition").setExecutor(new PositionCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
