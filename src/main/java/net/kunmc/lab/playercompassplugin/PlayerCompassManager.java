package net.kunmc.lab.playercompassplugin;

import net.kunmc.lab.playercompassplugin.PlayerCompass.PlayerCompass;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class PlayerCompassManager {
    private final HashMap<UUID, PlayerCompass> compassesCache = new HashMap<>();
    private final PlayerCompassPlugin plugin = PlayerCompassPlugin.getInstance();
    private final PlayerCompassPluginData data = PlayerCompassPlugin.getData();

    public void changeUpdatePointPeriod(long period) {
        for (PlayerCompass compass : compassesCache.values()) {
            compass.restartUpdaterTask(plugin, 0, period);
        }
        data.setUpdatePointPeriod(period);
    }

    public boolean compassExists(Player target) {
        return compassExists(target.getUniqueId());
    }

    public boolean compassExists(UUID targetUUID) {
        return compassesCache.get(targetUUID) != null;
    }

    public PlayerCompass getPlayerCompass(Player target) {
        return getPlayerCompass(target.getUniqueId());
    }

    public PlayerCompass getPlayerCompass(UUID targetUUID) {
        return compassesCache.get(targetUUID);
    }

    public void registerCompass(PlayerCompass compass) {
        compassesCache.put(compass.getTargetUUID(), compass);
        data.setLastPoint(compass.getTargetUUID(), compass.getCompassMeta().getLodestone());
    }

    public void updateCompassCache(PlayerCompass compass) {
        compassesCache.put(compass.getTargetUUID(), compass);
        data.setLastPoint(compass.getTargetUUID(), compass.getCompassMeta().getLodestone());
    }

    public Collection<PlayerCompass> getCompassList() {
        return new ArrayList<>(compassesCache.values());
    }
}
