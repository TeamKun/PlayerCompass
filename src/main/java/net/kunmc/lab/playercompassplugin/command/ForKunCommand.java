package net.kunmc.lab.playercompassplugin.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class ForKunCommand implements CommandExecutor {
    HashMap<UUID, Boolean> isKunPositionShown = new HashMap<>();
    String kunName = "roadhog_kun";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはプレイヤーから実行してください.");
            return true;
        }

        switch (command.getName()) {
            case "kun": {
                CommandExecutor executor = Bukkit.getPluginCommand("compass").getExecutor();
                command.setName("compass");
                executor.onCommand(sender, command, "compass", new String[]{kunName});
                sender.sendMessage(ChatColor.GREEN + "Kunの方向を指すコンパスを配布しました.");
                break;
            }
            case "kunxyz": {
                UUID uuid = ((Player) sender).getUniqueId();
                isKunPositionShown.putIfAbsent(uuid, false);
                CommandExecutor executor = Bukkit.getPluginCommand("playerpos").getExecutor();
                if (isKunPositionShown.get(uuid)) {
                    executor.onCommand(sender, new PluginsCommand("playerposoff"), "playerposoff", new String[]{});
                    isKunPositionShown.put(uuid, false);
                } else {
                    command.setName("playerpos");
                    executor.onCommand(sender, new PluginsCommand("playerpos"), "playerpos", new String[]{kunName});
                    sender.sendMessage(ChatColor.GREEN + "非表示にするにはもう一度コマンドを実行してください.");
                    isKunPositionShown.put(uuid, true);
                }
                break;
            }
        }
        return true;
    }
}
