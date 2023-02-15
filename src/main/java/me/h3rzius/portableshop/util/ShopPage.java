package me.h3rzius.portableshop.util;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ShopPage {
    private final Map<Integer, ShopItem> items;

    public ShopPage() {
        this.items = new HashMap<>();
    }

    public Map<Integer, ShopItem> getItems() {
        return items;
    }

    public void addItem(int slot, ItemStack itemStack, int cost) {
        items.put(slot, new ShopItem(itemStack, cost, slot));
    }

    public boolean removeItem(int slot) {
        return items.remove(slot) != null;
    }

    public ItemStack getItem(int slot) {
        ShopItem shopItem = items.get(slot);
        if (shopItem == null) {
            return null;
        }

        return shopItem.getItemStack().clone();
    }

    public static class ShopItem {
        private final ItemStack itemStack;
        private final int cost;
        private final int slot;

        public ShopItem(ItemStack itemStack, int cost, int slot) {
            this.itemStack = itemStack;
            this.cost = cost;
            this.slot = slot;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public int getCost() {
            return cost;
        }

        public int getSlot() {
            return slot;
        }
    }
}
