package net.kunmc.lab.playercompassplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerCompass extends ItemStack {
    private final UUID targetUUID;
    private final Set<UUID> holdersUUID = new HashSet<>();
    private final PlayerCompass instance;
    private BukkitTask updatePosTask;

    PlayerCompass(Player target, Player holder) {
        super(Material.COMPASS);
        this.targetUUID = target.getUniqueId();
        this.holdersUUID.add(holder.getUniqueId());
        this.instance = this;

        CompassMeta compassMeta = ((CompassMeta) this.getItemMeta());
        Location loc = target.getLocation().clone();
        loc.setY(0);
        compassMeta.setLodestoneTracked(false);
        compassMeta.setLodestone(loc);

        Component displayName = Component.text("PlayerCompass(" + target.getName() + ")");
        compassMeta.displayName(displayName);

        compassMeta.getPersistentDataContainer().set(PlayerCompassPlugin.getNamespacedKey(), PersistentDataType.STRING, displayName.toString());

        setCompassMeta(compassMeta);

        this.updatePosTask = new updatePosTask().runTaskTimerAsynchronously(PlayerCompassPlugin.getInstance(), 0, 8);
    }

    private class updatePosTask extends BukkitRunnable {
        @Override
        public void run() {
            Player target = Bukkit.getPlayer(targetUUID);
            if (target == null) return;

            Location loc = target.getLocation().clone();
            loc.setY(0);
            CompassMeta meta = instance.getCompassMeta();
            meta.setLodestone(loc);
            setCompassMeta(meta);

            for (UUID holderUUID : holdersUUID) {
                Player holder = Bukkit.getPlayer(holderUUID);
                if (holder == null) continue;

                PlayerInventory inv = holder.getInventory();
                Map<Integer, ? extends ItemStack> oldCompasses = inv.all(Material.COMPASS);
                if (oldCompasses.isEmpty()) continue;

                for (Map.Entry<Integer, ? extends ItemStack> entry : oldCompasses.entrySet()) {
                    ItemStack oldCompass = entry.getValue();
                    if (PlayerCompass.equals(oldCompass,instance)) {
                        instance.setAmount(oldCompass.getAmount());
                        holder.getInventory().setItem(entry.getKey(), instance);
                    }
                }
                ItemStack offHand = inv.getItemInOffHand();
                if (PlayerCompass.equals(offHand,instance)) {
                    inv.setItemInOffHand(instance);
                }
            }
        }
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

    public void restartUpdatePosTask(PlayerCompassPlugin plugin, long delay, long period) {
        updatePosTask.cancel();
        this.updatePosTask = new updatePosTask().runTaskTimerAsynchronously(plugin, delay, period);
    }

    public CompassMeta getCompassMeta() {
        return ((CompassMeta) this.getItemMeta());
    }

    public void setCompassMeta(CompassMeta meta) {
        this.setItemMeta(meta);
    }

    public Player getTarget() {
        return Bukkit.getPlayer(targetUUID);
    }

    public UUID getTargetUUID() { return targetUUID; }

    public Set<Player> getHolders() {
        return new HashSet<>(holdersUUID).stream().map(Bukkit::getPlayer).collect(Collectors.toSet());
    }

    public Set<UUID> getHoldersUUID() {
        return new HashSet<>(holdersUUID);
    }

    public void addHolder(Player holder) {
        holdersUUID.add(holder.getUniqueId());
    }

    public void addHolders(Collection<Player> holders) {
        holders.forEach(this::addHolder);
    }

    public void removeHolder(Player holder) {
        holdersUUID.remove(holder.getUniqueId());
    }
}
