package net.kunmc.lab.playercompassplugin;

import org.bukkit.Location;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class PlayerCompassPluginData {
    private final FileConfiguration config;
    private final PlayerCompassPlugin plugin = PlayerCompassPlugin.getInstance();

    PlayerCompassPluginData(FileConfiguration config) {
        this.config = config;
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.saveConfig();
            }
        }.runTaskTimerAsynchronously(plugin, 0, getSavePeriod());
    }

    public long getUpdatePointPeriod() {
        return config.getInt("UpdatePointPeriod");
    }

    public void setUpdatePointPeriod(long period) {
        set("UpdatePointPeriod", period);
    }

    public void set(String key, Object value) {
        boolean needSave = config.get(key) == null;
        config.set(key, value);
        if (needSave) plugin.saveConfig();
    }

    public Map<String, Location> getLastPoints() {
        MemorySection lastPointsSection = ((MemorySection) config.get("LastPoints"));
        if (lastPointsSection == null) return null;

        Map<String, Location> lastPoints = new HashMap<>();
        for (String key : lastPointsSection.getKeys(false)) {
            Location loc = ((Location) lastPointsSection.get(key));
            lastPoints.put(key, loc);
        }
        return lastPoints;
    }

    private long getSavePeriod() {
        return config.getLong("SavePeriod");
    }

    public String getKunName() {
        return config.getString("KunName");
    }

    public Location getLastPoint(String name) {
        return config.getLocation("LastPoints." + name);
    }

    public void setLastPoint(String name, Location loc) {
        set("LastPoints." + name, loc);
    }
}
