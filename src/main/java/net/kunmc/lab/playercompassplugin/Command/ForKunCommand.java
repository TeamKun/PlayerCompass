package net.kunmc.lab.playercompassplugin.Command;

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
            case "kun":{
                CommandExecutor executor = Bukkit.getPluginCommand("playercompass").getExecutor();
                command.setName("playercompass");
                executor.onCommand(sender, command, "playercompass", new String[]{kunName});
                sender.sendMessage(ChatColor.GREEN+"Kunの方向を指すコンパスを配布しました.");
                break;
            }
            case "kunxyz":{
                UUID uuid = ((Player) sender).getUniqueId();
                isKunPositionShown.putIfAbsent(uuid, false);
                CommandExecutor executor = Bukkit.getPluginCommand("playerposition").getExecutor();
                if (isKunPositionShown.get(uuid)) {
                    executor.onCommand(sender, new PluginsCommand("playerpositionoff"), "playerpositionoff", new String[]{});
                    isKunPositionShown.put(uuid, false);
                } else {
                    command.setName("playerposition");
                    executor.onCommand(sender, new PluginsCommand("playerposition"), "playerposition", new String[]{kunName});
                    sender.sendMessage(ChatColor.GREEN+"非表示にするにはもう一度コマンドを実行してください.");
                    isKunPositionShown.put(uuid, true);
                }
                break;
            }
        }
        return true;
    }
}
