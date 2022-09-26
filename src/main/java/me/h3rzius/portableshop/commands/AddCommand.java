package me.h3rzius.portableshop.commands;

import me.h3rzius.portableshop.PortableShop;
import me.h3rzius.portableshop.listeners.CreateShopListener;
import org.bukkit.entity.Player;

public class AddCommand {
    PortableShop portableShop = new PortableShop();
    CreateShopListener createShopListener = new CreateShopListener();

    public void addItem(Player player) {
        if(createShopListener.isShopCreated()) {
            player.sendMessage(portableShop.getConfigStrings("alreadycreated"));
        }

    }
}
