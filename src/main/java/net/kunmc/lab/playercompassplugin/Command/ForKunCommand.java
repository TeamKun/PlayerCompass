package net.kunmc.lab.playercompassplugin.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class ForKunCommand implements CommandExecutor {
    HashMap<UUID, Boolean> isKunPositionShown = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはプレイヤーから実行してください.");
            return true;
        }

        switch (command.getName()) {
            case "kun":{
                CommandExecutor executor = Bukkit.getPluginCommand("playercompass").getExecutor();
                executor.onCommand(sender, command, "playercompass", new String[]{"roadhog_kun"});
                sender.sendMessage(ChatColor.GREEN+"Kunの方向を指すコンパスを配布しました.");
                break;
            }
            case "kunxyz":{
                UUID uuid = ((Player) sender).getUniqueId();
                isKunPositionShown.putIfAbsent(uuid, false);
                CommandExecutor executor = Bukkit.getPluginCommand("playerposition").getExecutor();
                if (isKunPositionShown.get(uuid)) {
                    executor.onCommand(sender, command, "playerposition", new String[]{});
                    isKunPositionShown.put(uuid, false);
                } else {
                    executor.onCommand(sender, command, "playerposition", new String[]{"roadhog_kun"});
                    isKunPositionShown.put(uuid, true);
                }
                break;
            }
        }
        return true;
    }
}
