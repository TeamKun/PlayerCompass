package net.kunmc.lab.playercompassplugin.Command;

import net.kunmc.lab.playercompassplugin.PlayerCompass.PlayerCompass;
import net.kunmc.lab.playercompassplugin.PlayerCompassManager;
import net.kunmc.lab.playercompassplugin.PlayerCompassPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.CompassMeta;
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
        Player target = Bukkit.getPlayer(targetName);
        PlayerCompass compass;
        if (target == null) {
            UUID uuid = Bukkit.getPlayerUniqueId(targetName);
            if (uuid == null) {
                sender.sendMessage(ChatColor.RED+targetName+"は存在しません..");
                return true;
            }
            compass = manager.getPlayerCompass(uuid);
            if (compass == null) {
                compass = new PlayerCompass(uuid, new Location(((Player) sender).getWorld(), 0,0,0), PlayerCompassPlugin.getData().getUpdatePointPeriod());
            }
        } else {
            compass = manager.getPlayerCompass(target);
            if (compass == null) {
                compass = new PlayerCompass(target,PlayerCompassPlugin.getData().getUpdatePointPeriod());
            }
        }

        CompassMeta meta = compass.getCompassMeta();
        TextComponent displayName = ((TextComponent) meta.displayName());
        if (displayName.content().equals("PlayerCompass(null)")) {
            meta.displayName(Component.text("PlayerCompass(" + targetName + ")"));
        }
        Bukkit.getLogger().info(displayName.toString());
        compass.setCompassMeta(meta);
        manager.registerCompass(compass);
        ((Player) sender).getInventory().addItem(compass);
        return true;
    }
}
