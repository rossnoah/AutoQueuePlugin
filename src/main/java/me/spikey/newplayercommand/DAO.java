package me.spikey.newplayercommand;


import me.spikey.newplayercommand.utils.UUIDUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class DAO {

    public static boolean hasJoined(Connection connection, UUID uuid) {

        try {
            Class.forName("org.sqlite.JDBC");

            String query = """
                    SELECT * FROM hasjoined WHERE uuid=?;
                    """;
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void removeJoin(Connection connection, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    DELETE FROM hasjoined WHERE uuid=?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addJoin(Connection connection, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    INSERT INTO hasjoined (uuid) \
                    VALUES\
                    (?);
                    """;
            statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
