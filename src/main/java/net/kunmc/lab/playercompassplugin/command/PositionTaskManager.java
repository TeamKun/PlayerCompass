package net.kunmc.lab.playercompassplugin.command;

import net.kunmc.lab.playercompassplugin.PlayerCompassPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class PositionTaskManager {
    private HashMap<UUID, Integer> taskIDs = new HashMap<>();
    private static final PositionTaskManager singleton = new PositionTaskManager();

    public static PositionTaskManager getInstance() {
        return singleton;
    }

    private PositionTaskManager() {
    }

    public void register(Player sender, Player target) {
        Integer oldTaskID = taskIDs.get(sender.getUniqueId());
        if (oldTaskID != null) Bukkit.getScheduler().cancelTask(oldTaskID);

        BukkitTask newTask = new ShowPosTask(sender, target).runTaskTimerAsynchronously(PlayerCompassPlugin.getInstance(), 0, 8);
        taskIDs.put(sender.getUniqueId(), newTask.getTaskId());
    }

    public void unregister(Player sender) {
        Integer oldTaskID = taskIDs.get(sender.getUniqueId());
        if (oldTaskID != null) Bukkit.getScheduler().cancelTask(oldTaskID);
        sender.sendActionBar(Component.text(""));
    }

    private class ShowPosTask extends BukkitRunnable {
        CommandSender sender;
        Player target;
        Location lastLoc;

        ShowPosTask(CommandSender sender, Player target) {
            this.sender = sender;
            this.target = target;
            this.lastLoc = target.getLocation();
        }

        @Override
        public void run() {
            if (target.isOnline()) {
                this.lastLoc = target.getLocation();
            }
            sender.sendActionBar(Component.text(String.format("%sの座標 X:%.0f Y:%.0f Z:%.0f", target.getName(), lastLoc.getX(), lastLoc.getY(), lastLoc.getZ())));
        }

        public String getTargetName() {
            return target.getName();
        }
    }
}
