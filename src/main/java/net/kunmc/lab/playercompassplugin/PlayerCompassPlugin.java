package net.kunmc.lab.playercompassplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import net.kunmc.lab.playercompassplugin.command.*;
import net.kunmc.lab.playercompassplugin.compass.PlayerCompass;
import net.kunmc.lab.playercompassplugin.player.FakePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class PlayerCompassPlugin extends JavaPlugin implements Listener {
    private static PlayerCompassPluginData data;
    private static PlayerCompassPlugin INSTANCE;
    private static NamespacedKey namespacedKey;
    private static PlayerCompassManager manager;
    private static ProtocolManager protocolManager;

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

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        saveDefaultConfig();
        data = new PlayerCompassPluginData(getConfig());
        namespacedKey = new NamespacedKey(this, NamespacedKey.BUKKIT);
        manager = new PlayerCompassManager();
        protocolManager = ProtocolLibrary.getProtocolManager();

        Map<String, Location> lastPoints = data.getLastPoints();
        if (lastPoints != null) {
            for (String name : lastPoints.keySet()) {
                FakePlayer p = new FakePlayer(name, Bukkit.getPlayerUniqueId(name), lastPoints.get(name));
                manager.registerCompass(new PlayerCompass(p, data.getUpdatePointPeriod()));
            }
        }

        getServer().getPluginCommand("compass").setExecutor(new CompassCommand());
        getServer().getPluginCommand("playerpos").setExecutor(new ShowPositionCommand());
        getServer().getPluginCommand("playerposoff").setExecutor(new HidePositionCommand());
        getServer().getPluginCommand("changeupdateperiod").setExecutor(new ChangeUpdatePeriodCommand());
        getServer().getPluginCommand("changeupdateperiod").setTabCompleter(new ChangeUpdatePeriodCommand());

        CommandExecutor kuncommand = new ForKunCommand();
        getServer().getPluginCommand("kun").setExecutor(kuncommand);
        getServer().getPluginCommand("kunxyz").setExecutor(kuncommand);

        getServer().getPluginManager().registerEvents(new CompassClickListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
