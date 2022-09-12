package me.h3rzius.portableshop.exceptions;

import me.h3rzius.portableshop.PortableShop;
import org.bukkit.command.CommandException;

public class PortableShopCommandNotExistException extends CommandException {

    PortableShop pshop = new PortableShop();
    @Override
    public void printStackTrace() {
        pshop.getConfigStrings("commandNotExistException");
        super.printStackTrace();
    }
}
