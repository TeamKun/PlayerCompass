package net.kunmc.lab.playercompassplugin;

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
    }

    public PlayerCompass getPlayerCompass(Player target, long updatePeriod) {
        compassesCache.putIfAbsent(target.getUniqueId(), new PlayerCompass(target, updatePeriod));
        return compassesCache.get(target.getUniqueId());
    }

    public PlayerCompass registerCompassByUUID(UUID targetUUID, Location loc, long updatePeriod) {
        compassesCache.putIfAbsent(targetUUID, new PlayerCompass(targetUUID, loc, updatePeriod));
        return compassesCache.get(targetUUID);
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
