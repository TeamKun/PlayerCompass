package net.kunmc.lab.playercompassplugin;

import net.kunmc.lab.playercompassplugin.PlayerCompass.PlayerCompass;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.CompassMeta;

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
        return  compassExists(target.getUniqueId());
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

    public void updateCompassPoint(PlayerCompass compass) {
        Player target = compass.getTarget();
        Location loc = target.getLocation().clone();
        CompassMeta meta = compass.getCompassMeta();
        meta.setLodestone(loc);
        compass.setCompassMeta(meta);
        updateCompassCache(target, compass);
    }

    private void updateCompassCache(Player target, PlayerCompass compass) {
        updateCompassCache(target.getUniqueId(), compass);
    }

    private void updateCompassCache(UUID targetUUID, PlayerCompass compass) {
        compassesCache.put(targetUUID, compass);
        data.setLastPoint(targetUUID, compass.getCompassMeta().getLodestone());
    }

    public Collection<PlayerCompass> getCompassList() {
        return new ArrayList<>(compassesCache.values());
    }
}
