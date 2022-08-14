package me.spikey.newplayercommand;

import me.spikey.newplayercommand.utils.SchedulerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AutoQueueCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("Only players can use this command!");
            return true;
        }


        Player p = (Player) commandSender;
        UUID uuid = p.getUniqueId();

        if(strings.length<1){
            commandSender.sendMessage("§cYou must select if you want to turn §aon§c or §4off§c AutoRequeue!");
            commandSender.sendMessage("Example: §a/"+s+" on");
            return true;
        }

        if(strings[0].equalsIgnoreCase("on")||strings[0].equalsIgnoreCase("enable")){


            SchedulerUtils.runDatabaseAsync(connection -> {
                DAO.addAutoQueue(connection, p.getUniqueId());
                SchedulerUtils.runSync(() -> {
                    p.sendMessage("§aAutoQueue has been enabled!");
                });
            });


            return true;
        }

        if(strings[0].equalsIgnoreCase("off")||strings[0].equalsIgnoreCase("disable")){
            SchedulerUtils.runDatabaseAsync((connection -> {

                if (DAO.hasAutoQueue(connection, uuid)) {
                    DAO.removeAutoQueue(connection, uuid);
                }
                SchedulerUtils.runSync(() -> {
                    p.sendMessage("§cAutoQueue has been disabled!");
                });

            }));
            return true;
        }



        commandSender.sendMessage("§cYou must select if you want to turn §aon§c or §4off§c AutoRequeue!");
        commandSender.sendMessage("Example: §a/"+s+" on");
        return true;
    }
}
