package me.h3rzius.portableshop.util;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopData {
    private final String id;
    private String shopName;
    private String description;
    private final Map<Integer, ShopPage> pages;

    public ShopData(String id, String shopName, String description, Map<Integer, List<ShopPage.ShopItem>> pages) {
        this.id = id;
        this.shopName = shopName;
        this.description = "";
        this.pages = new HashMap<>();
    }

    public static ShopData fromConfig(YamlConfiguration config) {
        String id = config.getString("id");
        String shopName = config.getString("shopName");
        String description = config.getString("description");
        Map<Integer, List<ShopPage.ShopItem>> pages = new HashMap<>();
        ConfigurationSection pagesSection = config.getConfigurationSection("pages");
        if (pagesSection != null) {
            for (String pageNumber : pagesSection.getKeys(false)) {
                int page = Integer.parseInt(pageNumber);
                List<ShopPage.ShopItem> items = new ArrayList<>();
                ConfigurationSection itemsSection = pagesSection.getConfigurationSection(pageNumber + ".items");
                if (itemsSection != null) {
                    for (String slot : itemsSection.getKeys(false)) {
                        ConfigurationSection itemSection = itemsSection.getConfigurationSection(slot);
                        if (itemSection != null) {
                            String itemName = itemSection.getString("itemName");
                            int amount = itemSection.getInt("amount");
                            int cost = itemSection.getInt("cost");
                            ItemStack itemStack = new ItemStack(Material.matchMaterial(itemName), amount);
                            items.add(new ShopPage.ShopItem(itemStack, cost, Integer.parseInt(slot)));
                        }
                    }
                }
                pages.put(page, items);
            }
        }
        return new ShopData(id, shopName, description, pages);
    }

    public String getId() {
        return id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<Integer, ShopPage> getPages() {
        return pages;
    }

    public void addItem(int page, int slot, ItemStack itemStack, int cost) {
        ShopPage shopPage = pages.getOrDefault(page, new ShopPage());
        shopPage.addItem(slot, itemStack, cost);
        pages.put(page, shopPage);
    }

    public boolean removeItem(int page, int slot) {
        ShopPage shopPage = pages.get(page);
        if (shopPage == null) {
            return false;
        }

        boolean removed = shopPage.removeItem(slot);
        if (removed && shopPage.getItems().isEmpty()) {
            pages.remove(page);
        }

        return removed;
    }

    public ItemStack getItem(int page, int slot) {
        ShopPage shopPage = pages.get(page);
        if (shopPage == null) {
            return null;
        }

        return shopPage.getItem(slot);
    }
}
