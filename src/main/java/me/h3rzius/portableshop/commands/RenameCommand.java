package me.h3rzius.portableshop.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class RenameCommand implements CommandExecutor {

    private final File dataFolder;

    public RenameCommand(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /pshop rename <new shop name>");
            return true;
        }

        // Get the player's shop data
        UUID uuid = player.getUniqueId();
        File shopFile = new File(dataFolder, uuid.toString() + ".yml");

        if (!shopFile.exists()) {
            player.sendMessage(ChatColor.RED + "You don't have a shop yet. Use /pshop create to create one.");
            return true;
        }

        YamlConfiguration shopConfig = YamlConfiguration.loadConfiguration(shopFile);

        // Rename the shop
        String newShopName = args[0];
        shopConfig.set("shopName", newShopName);

        try {
            shopConfig.save(shopFile);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Error saving shop data.");
            e.printStackTrace();
            return true;
        }

        player.sendMessage(ChatColor.GREEN + "Your shop has been renamed to " + newShopName + ".");
        return true;
    }
}
