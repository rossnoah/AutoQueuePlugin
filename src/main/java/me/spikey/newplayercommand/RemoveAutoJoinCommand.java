package me.spikey.newplayercommand;

import me.spikey.newplayercommand.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RemoveAutoJoinCommand implements CommandExecutor {

    private Main plugin;

    public RemoveAutoJoinCommand(Main plugin) {

        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length == 0) {
            commandSender.sendMessage("§cEnter player name.");
            return true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(strings[0]);

        if (offlinePlayer == null) {
            commandSender.sendMessage("§cInvalid Player.");
            return true;
        }
        SchedulerUtils.runDatabaseAsync(connection -> {
            DAO.removeAutoQueue(connection, offlinePlayer.getUniqueId());
            SchedulerUtils.runSync(() -> {
                commandSender.sendMessage("§aDisabled AutoJoin for "+offlinePlayer.getName());
            });
        });

        return true;
    }
}
