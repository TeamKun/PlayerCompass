package net.kunmc.lab.playercompassplugin;

import org.bukkit.configuration.file.FileConfiguration;

public class PlayerCompassPluginSaveData {
    private final FileConfiguration config;
    private final PlayerCompassPlugin plugin = PlayerCompassPlugin.getInstance();

    PlayerCompassPluginSaveData(FileConfiguration config) {
        this.config = config;
    }

    public long getUpdatePosPeriod() {
        return config.getInt("updatePosPeriod");
    }

    private void setValue(String key, Object value) {
        config.set(key, value);
        plugin.saveConfig();
    }

    public void setUpdatePosPeriod(long period) {
        setValue("updatePosPeriod", period);
    }
}
