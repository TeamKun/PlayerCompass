package net.kunmc.lab.playercompassplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CompassCommand implements CommandExecutor {
    PlayerCompassManager manager = PlayerCompassPlugin.getManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) return false;
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーから実行してください.");
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + targetName + "はログインしていません.");
            return true;
        }
        Player holder = ((Player) sender);
        holder.getInventory().addItem(manager.getPlayerCompass(target, holder));
        return true;
    }
}
