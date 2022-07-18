package me.h3rzius.portableshop;

import me.h3rzius.portableshop.commands.MainCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PortableShop extends JavaPlugin {
    Player player;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("portableshop").setExecutor(new MainCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
