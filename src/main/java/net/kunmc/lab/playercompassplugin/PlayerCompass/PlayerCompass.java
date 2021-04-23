package net.kunmc.lab.playercompassplugin.PlayerCompass;

import net.kunmc.lab.playercompassplugin.PlayerCompassPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class PlayerCompass extends ItemStack {
    private final UUID targetUUID;
    private Integer updaterTaskID;

    public PlayerCompass(Player target, long updatePeriod) {
        super(Material.COMPASS);
        this.targetUUID = target.getUniqueId();

        CompassMeta compassMeta = ((CompassMeta) this.getItemMeta());
        Location loc = target.getLocation().clone();
        compassMeta.setLodestoneTracked(false);
        compassMeta.setLodestone(loc);

        Component displayName = Component.text("PlayerCompass(" + target.getName() + ")");
        compassMeta.displayName(displayName);

        compassMeta.getPersistentDataContainer().set(PlayerCompassPlugin.getNamespacedKey(), PersistentDataType.STRING, displayName.toString());

        setCompassMeta(compassMeta);

        this.updaterTaskID = new PlayerCompassPointUpdater(this).runTaskTimerAsynchronously(PlayerCompassPlugin.getInstance(), 0, updatePeriod).getTaskId();
    }

    public PlayerCompass(UUID targetUUID, Location loc, long updatePeriod) {
        super(Material.COMPASS);
        this.targetUUID = targetUUID;

        String targetName;
        Player target = Bukkit.getPlayer(targetUUID);
        if (target != null) {
            targetName = target.getName();
        } else {
            targetName = Bukkit.getOfflinePlayer(targetUUID).getName();
        }

        CompassMeta compassMeta = ((CompassMeta) this.getItemMeta());
        compassMeta.setLodestoneTracked(false);
        compassMeta.setLodestone(loc);

        Component displayName = Component.text("PlayerCompass(" + targetName + ")");
        compassMeta.displayName(displayName);

        compassMeta.getPersistentDataContainer().set(PlayerCompassPlugin.getNamespacedKey(), PersistentDataType.STRING, targetUUID.toString());

        setCompassMeta(compassMeta);

        this.updaterTaskID = new PlayerCompassPointUpdater(this).runTaskTimerAsynchronously(PlayerCompassPlugin.getInstance(), 0, updatePeriod).getTaskId();
    }

    public static boolean isPlayerCompass(ItemStack compass) {
        return compass.getItemMeta() != null && compass.getItemMeta().getPersistentDataContainer().get(PlayerCompassPlugin.getNamespacedKey(), PersistentDataType.STRING) != null;
    }

    public static boolean equals(ItemStack compass1, ItemStack compass2) {
        if (!(isPlayerCompass(compass1) && isPlayerCompass(compass2))) return false;
        String data1 = compass1.getItemMeta().getPersistentDataContainer().get(PlayerCompassPlugin.getNamespacedKey(), PersistentDataType.STRING);
        String data2 = compass2.getItemMeta().getPersistentDataContainer().get(PlayerCompassPlugin.getNamespacedKey(), PersistentDataType.STRING);
        return data1.equals(data2);
    }

    public void restartUpdaterTask(PlayerCompassPlugin plugin, long delay, long period) {
        Bukkit.getScheduler().cancelTask(updaterTaskID);
        updaterTaskID = new PlayerCompassPointUpdater(this).runTaskTimerAsynchronously(plugin, delay, period).getTaskId();
    }

    public CompassMeta getCompassMeta() {
        return ((CompassMeta) getItemMeta());
    }

    public void setCompassMeta(CompassMeta meta) {
        setItemMeta(meta);
    }

    public Player getTarget() {
        return Bukkit.getPlayer(targetUUID);
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }
}
