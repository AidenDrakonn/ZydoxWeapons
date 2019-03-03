package me.drakonn.zydoxweapons.command;

import me.drakonn.zydoxweapons.ZydoxWeapons;
import me.drakonn.zydoxweapons.datamanagers.MessageManager;
import me.drakonn.zydoxweapons.util.Util;
import me.drakonn.zydoxweapons.weapons.ZydoxWeapon;
import me.drakonn.zydoxweapons.weapons.ammo.Ammo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ZWeaponsCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if(!command.getLabel().equalsIgnoreCase("zweapons"))
            return true;

        if(commandSender instanceof Player)
        {
            Player player = (Player)commandSender;
            if(!player.hasPermission("zweapons.admin"))
            {
                player.sendMessage(MessageManager.noPermission);
                return true;
            }
        }

        if(args.length == 0)
        {
            for(String string : MessageManager.help)
                commandSender.sendMessage(string);
            return true;
        }

        if(args[0].equalsIgnoreCase("give"))
        {
            if (!(args.length == 3 || args.length == 4))
                return true;

            Player target = Bukkit.getPlayer(args[1]);
            if(target == null)
                return true;

            if(ZydoxWeapon.getZydoxWeapon(args[2]) == null && !args[2].equalsIgnoreCase("ammo"))
            {
                commandSender.sendMessage("§c§l(!)§f That is not a valid weapon, valid weapons include: ammo, cobwebbow, flamethrower, minionstaff, witchstaff");
                return true;
            }

            ItemStack item;
            if(args[2].equalsIgnoreCase("ammo"))
                item = Ammo.getAmmoItem();
            else item = ZydoxWeapon.getZydoxWeapon(args[2]).getSetItem();

            if(args.length == 4 && Util.isInt(args[3]))
                item.setAmount(Integer.valueOf(args[3]));

            Util.givePlayerItem(target, item);
            target.sendMessage("§f§lYou just got " + Integer.toString(item.getAmount()) + " " + item.getItemMeta().getDisplayName() + "§f§l!");
            return true;
        }

        if(args[0].equalsIgnoreCase("reload"))
        {
            ZydoxWeapons.getInstance().saveConfig();
            ZydoxWeapons.getInstance().reloadConfig();
            ZydoxWeapons.getInstance().load();
            commandSender.sendMessage("§c§l(!)§f ZydoxWeapons has been reloaded");
            return true;
        }

        if(args[0].equalsIgnoreCase("help"))
        {
            for(String string : MessageManager.help)
                commandSender.sendMessage(string);
            return true;
        }

        commandSender.sendMessage(MessageManager.invalidCommand);
        return true;
    }
}
