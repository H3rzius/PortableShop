package me.h3rzius.portableshop.commands;
import me.h3rzius.portableshop.PortableShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    PortableShop pshop = new PortableShop();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //PortableShopCommandNotExistException pscnee = new PortableShopCommandNotExistException();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0 ) {
                p.sendMessage(pshop.getConfigStrings("About"));
            }

            //p.sendMessage(pshop.getConfig().getString("About"));
        }
        return true;
    }
}
