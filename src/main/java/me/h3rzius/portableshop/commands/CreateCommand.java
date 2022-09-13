package me.h3rzius.portableshop.commands;

import me.h3rzius.portableshop.PortableShop;
import me.h3rzius.portableshop.listeners.CreateShopListener;
import org.bukkit.entity.Player;

public class CreateCommand {
    PortableShop portableShop = new PortableShop();
    CreateShopListener csl = new CreateShopListener();

    public void createShop(Player player) {
        csl.isShopCreated();
    }
}
