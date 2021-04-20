package net.kunmc.lab.playercompassplugin;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class PlayerCompassManager {
    private final HashMap<UUID, PlayerCompass> compasses = new HashMap<>();
    private final PlayerCompassPlugin plugin = PlayerCompassPlugin.getInstance();

    public void changeUpdatePos1Period(long period) {
        for (PlayerCompass compass : compasses.values()) {
            compass.restartUpdatePosTask(plugin, 0, period);
        }
    }

    public PlayerCompass unregisterCompass(UUID uuid) {
        return compasses.remove(uuid);
    }

    public PlayerCompass getPlayerCompass(Player target, Player holder) {
        compasses.putIfAbsent(target.getUniqueId(), new PlayerCompass(target, holder));
        PlayerCompass compass = compasses.get(target.getUniqueId());
        compass.addHolder(holder);
        return compass;
    }

    public Collection<PlayerCompass> getRegisteredCompassList() {
        return new ArrayList<>(compasses.values());
    }
}
