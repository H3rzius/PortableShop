package me.h3rzius.portableshop.commands;
import me.h3rzius.portableshop.PortableShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    PortableShop pshop = new PortableShop();
    private CreateCommand createCmd = new CreateCommand();
    private RenameCommand renameCmd = new RenameCommand();
    private AddCommand addCmd = new AddCommand();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //PortableShopCommandNotExistException pscnee = new PortableShopCommandNotExistException();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0 ) {
                p.sendMessage(pshop.getConfigStrings("About"));
            } else {
                String subCommand = args[1];
                switch (subCommand) {
                    case "about":
                        p.sendMessage(pshop.getConfigStrings("About"));
                    case "create":
                        createCmd.createShop(p);
                    case "rename":
                        renameCmd.renameShop(p);
                    case "add":
                        addCmd.addItem(p);
                        /*
                    case "view":
                        return false;
                    case "page":
                        return false;
                         */
                    default:
                        //pscnee.printStackTrace();
                }
            }

            //p.sendMessage(pshop.getConfig().getString("About"));
        }
        return true;
    }
}
