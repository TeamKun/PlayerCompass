package net.kunmc.lab.playercompassplugin.PlayerCompass;

import net.kunmc.lab.playercompassplugin.PlayerCompassManager;
import net.kunmc.lab.playercompassplugin.PlayerCompassPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class PlayerCompassPointUpdater extends BukkitRunnable {
    PlayerCompass compass;
    PlayerCompassManager manager = PlayerCompassPlugin.getManager();

    PlayerCompassPointUpdater(PlayerCompass compass) {
        this.compass = compass;
    }

    @Override
    public void run() {
        if (Bukkit.getPlayer(compass.getTargetUUID()) == null) return;
        Player target = compass.getTarget();
        Location loc = target.getLocation().clone();
        CompassMeta compassMeta = compass.getCompassMeta();
        compassMeta.setLodestone(loc);
        compassMeta.displayName(PlayerCompass.generateDisplayName(target.getName(), loc));
        compass.setCompassMeta(compassMeta);
        manager.updateCompassCache(compass);

        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerInventory inv = p.getInventory();
            Map<Integer, ? extends ItemStack> oldCompasses = inv.all(Material.COMPASS);
            if (!oldCompasses.isEmpty()) {
                for (Map.Entry<Integer, ? extends ItemStack> entry : oldCompasses.entrySet()) {
                    ItemStack oldCompass = entry.getValue();
                    if (PlayerCompass.equals(oldCompass, compass)) {
                        compass.setAmount(oldCompass.getAmount());
                        p.getInventory().setItem(entry.getKey(), compass);
                    }
                }

                ItemStack offHand = inv.getItemInOffHand();
                if (PlayerCompass.equals(offHand, compass)) {
                    inv.setItemInOffHand(compass);
                }
            }
        }

        //item_frame内のPlayerCompassを更新
        List<ItemFrame> itemFrames = Bukkit.selectEntities(compass.getTarget(), "@e[type=item_frame, nbt={Item:{id:\"minecraft:compass\"}}]").stream()
                .map(x -> ((ItemFrame) x))
                .filter(x -> PlayerCompass.isPlayerCompass(x.getItem()))
                .collect(Collectors.toList());
        for (ItemFrame frame : itemFrames) {
            ItemStack item = frame.getItem();
            if (PlayerCompass.isPlayerCompass(item) && PlayerCompass.equals(item, compass)) {
                frame.setItem(compass, false);
                frame.setCustomName(compassMeta.displayName().toString());
            }
        }
    }
}
