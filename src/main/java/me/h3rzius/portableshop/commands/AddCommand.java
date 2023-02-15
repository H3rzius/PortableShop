package me.h3rzius.portableshop.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AddCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1 && args[0].equalsIgnoreCase("add")) {
                UUID uuid = player.getUniqueId();
                File file = new File("plugins/MyPlugin/shops/" + uuid + ".yml");
                if (!file.exists()) {
                    player.sendMessage("Error: You have not created a shop yet. Use '/pshop create {shop name}' to create one.");
                    return true;
                }
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                int cost;
                try {
                    cost = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage("Error: Invalid cost. Usage: '/pshop add {cost}'.");
                    return true;
                }
                ItemStack itemStack = player.getInventory().getItemInHand();
                if (itemStack.getType() == Material.AIR) {
                    player.sendMessage("Error: You must be holding an item to add it to your shop.");
                    return true;
                }
                int page = config.getConfigurationSection("pages").getKeys(false).size() + 1;
                String path = "pages." + page + ".items";
                config.createSection(path);
                int slot = getNextAvailableSlot(config, page);
                String itemName = itemStack.getType().name().toString().replace("minecraft:", "");
                int amount = itemStack.getAmount();
                config.set(path + "." + slot + ".slot", slot);
                config.set(path + "." + slot + ".itemName", itemName);
                config.set(path + "." + slot + ".amount", amount);
                config.set(path + "." + slot + ".cost", cost);
                player.getInventory().removeItem(itemStack);
                try {
                    config.save(file);
                    player.sendMessage("Item added to shop successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                    player.sendMessage("Error: Unable to add item to shop.");
                }
                return true;
            }
        }
        return false;
    }

    private int getNextAvailableSlot(YamlConfiguration config, int page) {
        int slot = 0;
        for (int i = 0; i < 27; i++) {
            if (config.contains("pages." + page + ".items." + i)) {
                slot++;
            } else {
                break;
            }
        }
        return slot;
    }
}
