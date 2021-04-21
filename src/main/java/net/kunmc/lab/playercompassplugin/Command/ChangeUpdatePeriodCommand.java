package net.kunmc.lab.playercompassplugin.Command;

import net.kunmc.lab.playercompassplugin.PlayerCompassManager;
import net.kunmc.lab.playercompassplugin.PlayerCompassPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChangeUpdatePeriodCommand implements CommandExecutor, TabCompleter {
    PlayerCompassManager manager = PlayerCompassPlugin.getManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) return false;
        long period;
        try {
            period = Long.parseLong(args[0]);
            if (period < 0) {
                sender.sendMessage(ChatColor.RED + "<period>は0以上の数値を指定してください.");
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "<period>は0以上の数値を指定してください.");
            return true;
        }
        manager.changeUpdatePointPeriod(period);
        sender.sendMessage(String.format(ChatColor.GREEN + "PlayerCompassのアップデート間隔を%dに変更しました.", period));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return Collections.singletonList("<period>");
        return new ArrayList<>();
    }
}
