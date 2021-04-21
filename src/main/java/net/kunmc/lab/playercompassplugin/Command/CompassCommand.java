package net.kunmc.lab.playercompassplugin.Command;

import net.kunmc.lab.playercompassplugin.PlayerCompass.PlayerCompass;
import net.kunmc.lab.playercompassplugin.PlayerCompassManager;
import net.kunmc.lab.playercompassplugin.PlayerCompassPlugin;
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

        PlayerCompass compass = manager.getPlayerCompass(target);
        if (compass == null) {
            compass = new PlayerCompass(target,PlayerCompassPlugin.getData().getUpdatePointPeriod());
            manager.registerCompass(compass);
        }
        ((Player) sender).getInventory().addItem(compass);
        return true;
    }
}
