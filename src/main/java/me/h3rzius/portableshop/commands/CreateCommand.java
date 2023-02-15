package me.h3rzius.portableshop.commands;

import me.h3rzius.portableshop.PortableShop;
import me.h3rzius.portableshop.listeners.CreateShopListener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class CreateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
                String shopName = args[1];
                UUID uuid = player.getUniqueId();
                File file = new File("plugins/PortableShop/shops/" + uuid + ".yml");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("id", uuid.toString());
                config.set("shopName", shopName);
                config.set("description", "");
                config.createSection("pages.1.items");
                try {
                    config.save(file);
                    player.sendMessage("Shop created successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                    player.sendMessage("Error: Unable to create shop.");
                }
                return true;
            }
        }
        return false;
    }
}