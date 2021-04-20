package net.kunmc.lab.playercompassplugin;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PlayerCompassPlugin extends JavaPlugin implements Listener {
    private static PlayerCompassPluginData data;
    private static PlayerCompassPlugin INSTANCE;
    private static NamespacedKey namespacedKey;
    private static PlayerCompassManager manager;

    public static PlayerCompassManager getManager() {
        return manager;
    }

    public static PlayerCompassPlugin getInstance() {
        return INSTANCE;
    }

    public static NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public static PlayerCompassPluginData getData() {
        return data;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        saveDefaultConfig();
        data = new PlayerCompassPluginData(getConfig());
        namespacedKey = new NamespacedKey(this, NamespacedKey.BUKKIT);
        manager = new PlayerCompassManager();

        Map<UUID, Location> lastPoints = data.getLastPoints();
        if (lastPoints != null) {
            for (UUID uuid : lastPoints.keySet()) {
                manager.registerCompassByUUID(uuid, lastPoints.get(uuid));
            }
        }

        getServer().getPluginCommand("playercompass").setExecutor(new CompassCommand());
        getServer().getPluginCommand("playerposition").setExecutor(new PositionCommand());
        getServer().getPluginCommand("changeupdateperiod").setExecutor(new ChangeUpdatePeriodCommand());
        getServer().getPluginCommand("changeupdateperiod").setTabCompleter(new ChangeUpdatePeriodCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
