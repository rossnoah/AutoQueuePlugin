package me.spikey.newplayercommand;

import me.spikey.newplayercommand.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    public static ArrayList<UUID> hasJoinedCache;
    private static String command = "";
    @Override
    public void onEnable() {
        saveDefaultConfig();
        DatabaseManager.initDatabase(this);
        SchedulerUtils.setPlugin(this);

        command = getConfig().getString("command");

        getCommand("removeautoqueue").setExecutor(new RemoveAutoJoinCommand(this));
        this.getCommand("autoqueue").setExecutor(new AutoQueueCommand());
        this.getCommand("autoqueue").setTabCompleter(new AutoQueueTabCompleter());
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public void runCommandIfAllowed(UUID uuid) {
        SchedulerUtils.runDatabaseAsync((connection -> {
            if (!DAO.hasJoined(connection, uuid)) {
                return;
            }
            SchedulerUtils.runSync(() -> {
                String name = Bukkit.getPlayer(uuid).getName();
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.formatted(name));
            });
        }));
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {

        SchedulerUtils.runLater(()->{
            SchedulerUtils.runDatabaseAsync((connection -> {
                if (!DAO.hasJoined(connection, event.getPlayer().getUniqueId())) {
                    return;
                }
                SchedulerUtils.runSync(()->{
                    event.getPlayer().sendMessage("§aAutoQueue will run in 15 seconds");
                    event.getPlayer().sendMessage("Use §a/autoqueue§a off§r to disable this");

                });
            }));
        },2*20);


        SchedulerUtils.runLater(()->{
            runCommandIfAllowed(event.getPlayer().getUniqueId());
        },17*20);
    }

}
