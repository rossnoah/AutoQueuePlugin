package me.spikey.newplayercommand;

import me.spikey.newplayercommand.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ResetHadJoinedCommand implements CommandExecutor {

    private Main plugin;

    public ResetHadJoinedCommand(Main plugin) {

        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage("Must be op.");
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage("Enter player name.");
            return true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(strings[0]);

        if (offlinePlayer == null) {
            commandSender.sendMessage("Invalid Player.");
            return true;
        }

        plugin.hasJoinedCache.remove(offlinePlayer.getUniqueId());

        SchedulerUtils.runDatabaseAsync(connection -> {
            DAO.removeJoin(connection, offlinePlayer.getUniqueId());
        });

        return true;
    }
}
