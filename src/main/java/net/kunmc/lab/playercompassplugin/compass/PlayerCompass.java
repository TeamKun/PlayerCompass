package net.kunmc.lab.playercompassplugin.compass;

import net.kunmc.lab.playercompassplugin.PlayerCompassPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class PlayerCompass extends ItemStack {
    private final UUID targetUUID;
    private Integer updaterTaskID;

    public static Component generateDisplayName(String name, Location loc) {
        return Component.text(String.format("%s( X:%.0f Y:%.0f Z:%.0f )", name, loc.getX(), loc.getY(), loc.getZ()));
    }

    public PlayerCompass(Player target, long updatePeriod) {
        super(Material.COMPASS);
        this.targetUUID = target.getUniqueId();

        CompassMeta compassMeta = ((CompassMeta) this.getItemMeta());
        Location loc = target.getLocation().clone();
        compassMeta.setLodestoneTracked(false);
        compassMeta.setLodestone(loc);

        Component displayName = generateDisplayName(target.getName(), loc);
        compassMeta.displayName(displayName);

        compassMeta.getPersistentDataContainer().set(PlayerCompassPlugin.getNamespacedKey(), PersistentDataType.STRING, target.getUniqueId().toString());

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