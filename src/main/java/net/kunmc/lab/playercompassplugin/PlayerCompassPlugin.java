package net.kunmc.lab.playercompassplugin;

import net.kunmc.lab.playercompassplugin.Command.ChangeUpdatePeriodCommand;
import net.kunmc.lab.playercompassplugin.Command.CompassCommand;
import net.kunmc.lab.playercompassplugin.Command.ForKunCommand;
import net.kunmc.lab.playercompassplugin.Command.PositionCommand;
import net.kunmc.lab.playercompassplugin.PlayerCompass.PlayerCompass;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
                manager.registerCompass(new PlayerCompass(uuid, lastPoints.get(uuid), data.getUpdatePointPeriod()));
            }
        }

        getServer().getPluginCommand("playercompass").setExecutor(new CompassCommand());
        getServer().getPluginCommand("playerposition").setExecutor(new PositionCommand());
        getServer().getPluginCommand("changeupdateperiod").setExecutor(new ChangeUpdatePeriodCommand());
        getServer().getPluginCommand("changeupdateperiod").setTabCompleter(new ChangeUpdatePeriodCommand());

        CommandExecutor kuncommand = new ForKunCommand();
        getServer().getPluginCommand("kun").setExecutor(kuncommand);
        getServer().getPluginCommand("kunxyz").setExecutor(kuncommand);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
