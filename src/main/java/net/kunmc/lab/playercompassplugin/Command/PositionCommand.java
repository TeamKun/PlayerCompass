package net.kunmc.lab.playercompassplugin.Command;

import net.kunmc.lab.playercompassplugin.PlayerCompassPlugin;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class PositionCommand implements CommandExecutor {
    private HashMap<UUID, BukkitRunnable> tasks = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーから実行してください.");
            return true;
        }

        if (args.length == 0) {
            UUID uuid = ((Player) sender).getUniqueId();
            BukkitRunnable task = tasks.get(uuid);
            if (task != null) task.cancel();
            sender.sendMessage(ChatColor.GREEN+"座標を非表示にしました.");
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + targetName + "はログインしていません.");
            return true;
        }

        BukkitRunnable task = new ShowPosTask(sender, target);
        task.runTaskTimerAsynchronously(PlayerCompassPlugin.getInstance(), 0, 10);
        tasks.put(target.getUniqueId(), task);
        sender.sendMessage(ChatColor.GREEN+targetName+"の座標をアクションバーに表示しました.");
        return true;
    }

    private class ShowPosTask extends BukkitRunnable {
        CommandSender sender;
        Player target;

        ShowPosTask(CommandSender sender, Player target) {
            this.sender = sender;
            this.target = target;
        }

        @Override
        public void run() {
            if (target.isOnline()){
                Location loc = target.getLocation();
                sender.sendActionBar(Component.text(String.format("%sの座標 X:%.0f Y:%.0f Z:%.0f", target.getName(), loc.getX(), loc.getY(), loc.getZ())));
            }
        }

        public String getTargetName() {
            return target.getName();
        }
    }
}
