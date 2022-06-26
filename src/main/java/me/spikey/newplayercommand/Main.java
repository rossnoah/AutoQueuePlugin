package me.spikey.newplayercommand;

import com.google.common.collect.Lists;
import me.spikey.newplayercommand.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    public ArrayList<UUID> hasJoinedCache;
    private static String command = "";
    @Override
    public void onEnable() {
        saveDefaultConfig();
        DatabaseManager.initDatabase(this);
        SchedulerUtils.setPlugin(this);

        command = getConfig().getString("command");

        hasJoinedCache = Lists.newArrayList();
        getCommand("NPCReset").setExecutor(new ResetHadJoinedCommand(this));
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public void runCommandIfAllowed(UUID uuid) {
        if (hasJoinedCache.contains(uuid)) return;

        SchedulerUtils.runDatabaseAsync((connection -> {
            if (DAO.hasJoined(connection, uuid)) {
                return;
            }

            DAO.addJoin(connection, uuid);
            hasJoinedCache.add(uuid);
            if (hasJoinedCache.size() > 200) hasJoinedCache.remove(0);
            SchedulerUtils.runSync(() -> {
                String name = Bukkit.getPlayer(uuid).getName();
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.formatted(name));
            });
        }));
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        runCommandIfAllowed(event.getPlayer().getUniqueId());
    }
}
