package me.h3rzius.portableshop.commands;

import me.h3rzius.portableshop.util.ShopData;
import me.h3rzius.portableshop.util.ShopPage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GuiShopCommand implements CommandExecutor, Listener {

    private final JavaPlugin plugin;

    public GuiShopCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1 || args.length > 1) {
            player.sendMessage("Usage: /pshop view [page]");
            return true;
        }

        // Get the UUID of the player and the shop name
        UUID uuid = player.getUniqueId();
        String shopName = getShopName(uuid);

        if (shopName == null) {
            player.sendMessage("You don't have a shop.");
            return true;
        }

        // Get the page number
        int page = 1;
        if (args.length == 1) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid page number.");
                return true;
            }
        }

        // Load the shop data from the YAML file
        Map<String, Object> shopData = loadShopData(uuid);

        if (shopData == null) {
            player.sendMessage("Failed to load shop data.");
            return true;
        }

        // Get the list of items for the specified page
        Map<String, Object> items = (Map<String, Object>) shopData.get("pages." + page + ".items");

        if (items == null) {
            player.sendMessage("Page not found.");
            return true;
        }

        // Create the inventory and add the items
        Inventory inventory = Bukkit.createInventory(null, 54, shopName);

        for (String slotString : items.keySet()) {
            int slot = Integer.parseInt(slotString);
            Map<String, Object> itemData = (Map<String, Object>) items.get(slotString);
            ItemStack itemStack = createItemStack(itemData);
            inventory.setItem(slot, itemStack);
        }

        // Add the next page and previous page items
        ItemStack nextItem = createNavigationItem(Material.PAPER, "Next page", true);
        inventory.setItem(45, nextItem);

        ItemStack prevItem = createNavigationItem(Material.PAPER, "Previous page", false);
        inventory.setItem(53, prevItem);

        // Add the gray stained glass
        ItemStack glass = createNavigationItem(Material.GRAY_STAINED_GLASS_PANE, "", false);
        for (int i = 46; i < 53; i++) {
            inventory.setItem(i, glass);
        }

        // Open the inventory for the player
        player.openInventory(inventory);

        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(title)) {
            return;
        }
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(getFile(uuid));
        int page = Integer.parseInt(title.substring(title.lastIndexOf(' ') + 1));
        int slot = event.getSlot();
        if (slot >= 45 && slot <= 52) {
            event.setCancelled(true);
            return;
        }
        if (slot == 53) {
            // Next page
            int nextPage = page + 1;
            if (config.contains("pages." + nextPage)) {
                player.openInventory(createGui(player, config, nextPage));
            }
            event.setCancelled(true);
            return;
        } else if (slot == 44) {
            // Previous page
            int previousPage = page - 1;
            if (previousPage > 0 && config.contains("pages." + previousPage)) {
                player.openInventory(createGui(player, config, previousPage));
            }
            event.setCancelled(true);
            return;
        }
        int index = ((page - 1) * 27) + slot;
        String itemName = config.getString("pages." + page + ".items." + index + ".itemName");
        if (itemName != null) {
            ItemStack item = createItemStack(itemName);
            if (item != null) {
                player.getInventory().addItem(item);
                config.set("pages." + page + ".items." + index, null);
                try {
                    config.save(getFile(uuid));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        event.setCancelled(true);
        player.updateInventory();
    }

    private void createGui(Player player, YamlConfiguration config, int pageNumber) {
        List<ShopPage> pages = ShopData.fromConfig(config).getPages();
        int numPages = pages.size();
        if (pageNumber > numPages) {
            pageNumber = numPages;
        } else if (pageNumber < 1) {
            pageNumber = 1;
        }

        ShopPage page = pages.get(pageNumber - 1);
        String shopName = ShopData.fromConfig(config).getShopName();

        Inventory gui = Bukkit.createInventory(null, 54, shopName);

        // Add items to GUI
        for (ShopPage.ShopItem item : page.getItems()) {
            gui.setItem(item.getSlot(), createItemStack(item.getItemName(), item.getAmount(), item.getCost()));
        }

        // Add previous page button
        if (pageNumber > 1) {
            gui.setItem(45, createItemStack("minecraft:paper", 1, 0, "&cPrevious Page"));
        }

        // Add next page button
        if (pageNumber < numPages) {
            gui.setItem(53, createItemStack("minecraft:paper", 1, 0, "&aNext Page"));
        }

        // Add border
        ItemStack border = createItemStack("minecraft:gray_stained_glass", 1, 0);
        for (int i = 46; i < 53; i++) {
            gui.setItem(i, border);
        }

        player.openInventory(gui);
    }

    private File getFile(UUID uuid) {
        File dataFolder = plugin.getDataFolder();
        return new File(dataFolder, uuid.toString() + ".yml");
    }

    public static ItemStack createItemStack(String itemName) {
        Material material = Material.getMaterial(itemName);
        if (material == null) {
            return null;
        }

        ItemStack itemStack = new ItemStack(material, 1);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(itemName);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
