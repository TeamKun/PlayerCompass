package net.kunmc.lab.playercompassplugin.compass;

import net.kunmc.lab.playercompassplugin.PlayerCompassPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerCompass extends ItemStack {
    private final UUID targetUUID;
    private final String targetName;
    private Integer updaterTaskID;

    public static Component generateDisplayName(String name, Location loc) {
        return Component.text(String.format("%s%s( X:%.0f Y:%.0f Z:%.0f )", ChatColor.WHITE, name, loc.getX(), loc.getY(), loc.getZ()));
    }

    public static Component generateDisplayNameWithPlaneDistance(String name, Location dstLoc, Location srcLoc) {
        Location loc1 = dstLoc.clone();
        loc1.setY(0);
        Location loc2 = srcLoc.clone();
        loc2.setY(0);
        double distance = loc1.distance(loc2);
        return Component.text(String.format("%s%s( X:%.0f Y:%.0f Z:%.0f 距離:%.0fm )", ChatColor.WHITE, name, dstLoc.getX(), dstLoc.getY(), dstLoc.getZ(), distance));
    }

    public static UUID getUUIDFromMeta(ItemMeta meta) {
        String uuidStr = meta.getPersistentDataContainer().get(PlayerCompassPlugin.getNamespacedKey(), PersistentDataType.STRING);
        if (uuidStr == null) return null;
        return UUID.fromString(uuidStr);
    }

    public PlayerCompass(Player target, long updatePeriod) {
        super(Material.COMPASS);
        this.targetUUID = target.getUniqueId();
        this.targetName = target.getName();

        CompassMeta compassMeta = ((CompassMeta) this.getItemMeta());
        Location loc = target.getLocation().clone();
        compassMeta.setLodestoneTracked(false);
        compassMeta.setLodestone(loc);

        Component displayName = generateDisplayName(target.getName(), loc);
        compassMeta.displayName(displayName);

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.WHITE + "右クリックをすることで対象の座標がアクションバーに表示され,また対象が発光します."));
        lore.add(Component.text(ChatColor.WHITE + "もう一度右クリックをするとそれらを非表示にすることが出来ます."));
        compassMeta.lore(lore);

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

    public String getTargetName() {
        return this.targetName;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }
}
