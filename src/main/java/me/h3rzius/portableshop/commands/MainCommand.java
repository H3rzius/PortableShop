package me.h3rzius.portableshop.commands;

import me.h3rzius.portableshop.PortableShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MainCommand implements CommandExecutor {
    private PortableShop pshop;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.sendMessage(pshop.getConfig().getString("About"));
        }
        return true;
    }
}
