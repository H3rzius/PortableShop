package me.h3rzius.portableshop.commands;
import me.h3rzius.portableshop.PortableShop;
import me.h3rzius.portableshop.exceptions.PortableShopCommandNotExistException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VersionCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sound.sampled.Port;

public class MainCommand implements CommandExecutor {
    PortableShop pshop = new PortableShop();
    private CreateCommand createCmd;
    private RenameCommand renameCmd;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PortableShopCommandNotExistException pscnee = new PortableShopCommandNotExistException();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0 ) {
                p.sendMessage(pshop.getConfigStrings("About"));
            } else {
                String subCommand = args[1];
                switch (subCommand) {
                    case "about":
                        p.sendMessage(pshop.getConfigStrings("About"));
                        /*
                    case "create":
                        return false;
                    case "rename":
                        return false;
                    case "add":
                        return false;
                    case "view":
                        return false;
                    case "page":
                        return false;

                         */
                    default:
                        pscnee.printStackTrace();
                }
            }

            //p.sendMessage(pshop.getConfig().getString("About"));
        }
        return true;
    }
}
