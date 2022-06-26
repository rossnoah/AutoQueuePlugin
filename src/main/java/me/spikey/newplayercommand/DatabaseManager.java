package me.spikey.newplayercommand;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager {

    private static File databaseFile;
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void initDatabase(Plugin plugin) {

        File databaseFolder = new File(plugin.getDataFolder(), "core.db");
        if (!databaseFolder.exists()) {
            try {
                databaseFolder.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        databaseFile = databaseFolder;

        Statement statement = null;
        try (Connection connection = getConnection()){
            Class.forName("org.sqlite.JDBC");

            statement = connection.createStatement();
            String query = """
                    CREATE TABLE IF NOT EXISTS %s (\
                    uuid VARCHAR NOT NULL\
                    );
                    """.formatted("hasjoined");

            statement.execute(query);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
