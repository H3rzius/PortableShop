package me.h3rzius.portableshop;

import me.h3rzius.portableshop.commands.MainCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class PortableShop extends JavaPlugin {
    Player player;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("portableshop").setExecutor(new MainCommand());

        String dbUrl = getConfigStrings("url");
        String dbName = getConfigStrings("username");
        String dbPassword = getConfigStrings("password");

        try {
            Connection connection = DriverManager.getConnection(dbUrl, dbName, dbPassword);
            System.out.println("Database connected");

            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS player_info(uuid varchar(36) primary_key, has_shop bool, pages int)";
            statement.execute(sql);

        } catch (SQLException e) {
            System.out.println("Cannot connect to database:");
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String getConfigStrings(String str) {
        getConfig().getString(str);
        return str;
    }

}
