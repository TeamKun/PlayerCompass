package net.kunmc.lab.playercompassplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class PlayerCompassPointUpdater extends BukkitRunnable {
    PlayerCompass compass;
    PlayerCompassManager manager = PlayerCompassPlugin.getManager();

    PlayerCompassPointUpdater(PlayerCompass compass) {
        this.compass = compass;
    }

    @Override
    public void run() {
        if (Bukkit.getPlayer(compass.getTargetUUID()) == null) return;
        manager.updateCompassPoint(compass);

        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerInventory inv = p.getInventory();
            Map<Integer, ? extends ItemStack> oldCompasses = inv.all(Material.COMPASS);
            if (oldCompasses.isEmpty()) continue;

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
}