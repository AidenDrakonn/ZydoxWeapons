package me.drakonn.zydoxweapons.command.ammo;

import me.drakonn.zydoxweapons.datamanagers.MessageManager;
import me.drakonn.zydoxweapons.weapons.ammo.AmmoGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AmmoCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if(!command.getLabel().equalsIgnoreCase("ammo"))
            return true;

        if(!(commandSender instanceof Player))
            return true;

        Player player = (Player)commandSender;

        if(!player.hasPermission("zweapons.ammo.buy"))
        {
            player.sendMessage(MessageManager.noPermission);
            return true;
        }

        player.openInventory(AmmoGui.getAmmoGuiInventory());
        return true;
    }
}
