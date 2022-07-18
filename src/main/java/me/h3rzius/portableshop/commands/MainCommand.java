package me.h3rzius.portableshop.commands;
import me.h3rzius.portableshop.PortableShop;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VersionCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MainCommand implements CommandExecutor {
    private PortableShop pshop;
    private CreateCommand createCmd;
    private RenameCommand renameCmd;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String about = ChatColor.GREEN + "Portable Shop made by H3rzius.";
            if (args.length == 0 ) {
                p.sendMessage(about);
            } else {
                String subCommand = args[1];
                switch (subCommand) {
                    case "about":
                        p.sendMessage(about);
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
                        p.sendMessage(about);
                }
            }

            //p.sendMessage(pshop.getConfig().getString("About"));
        }
        return true;
    }
}
