package net.kunmc.lab.playercompassplugin.command;

import net.kunmc.lab.playercompassplugin.PlayerCompassManager;
import net.kunmc.lab.playercompassplugin.PlayerCompassPlugin;
import net.kunmc.lab.playercompassplugin.compass.PlayerCompass;
import net.kunmc.lab.playercompassplugin.player.FakePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

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
        UUID uuid = Bukkit.getPlayerUniqueId(targetName);
        if (uuid == null) {
            sender.sendMessage(ChatColor.RED + targetName + "は存在しません.");
            return true;
        }

        PlayerCompass compass = manager.getPlayerCompass(uuid);
        if (compass != null) {
            ((Player) sender).getInventory().addItem(compass);
            return true;
        }

        Player target = Bukkit.getPlayer(uuid);
        if (target == null) {
            FakePlayer p = new FakePlayer(targetName, uuid, new Location(((Player) sender).getWorld(), 0, 0, 0));
            compass = new PlayerCompass(p, PlayerCompassPlugin.getData().getUpdatePointPeriod());
        } else {
            compass = new PlayerCompass(target, PlayerCompassPlugin.getData().getUpdatePointPeriod());
        }
        
        manager.registerCompass(compass);
        ((Player) sender).getInventory().addItem(compass);
        return true;
    }
}
